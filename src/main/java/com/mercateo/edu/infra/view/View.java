package com.mercateo.edu.infra.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.mercateo.edu.infra.base.Event;
import com.mercateo.edu.infra.evt.EventStore;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class View implements Consumer<Event> {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface EventConsumer {
    }

    protected final EventStore es;

    private final Map<Class<?>, Consumer<Event>> eventHandlers = new HashMap<>();

    @Getter
    protected UUID aggregateId = null;

    protected Event last;

    public View(EventStore es) {
        this(es, null);
    }

    public View(@NonNull EventStore es, UUID id) {
        this.es = es;
        this.aggregateId = id;
        Collection<Method> methods = Arrays.asList(this.getClass().getMethods());
        methods.stream().filter(isHandleMethod()).forEach(m -> {
            Class<?> type = m.getParameterTypes()[0];
            eventHandlers.put(type, e -> {
                try {
                    m.invoke(this, e);
                } catch (IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException e1) {
                    log.error("Reflective invocation failed", e);
                }
            });
        });
    }

    private Predicate<? super Method> isHandleMethod() {
        return m -> (m.getAnnotation(EventConsumer.class) != null) && (m
                .getParameterTypes().length == 1) && (Event.class.isAssignableFrom(m
                        .getParameterTypes()[0])) && (m.getReturnType().equals(void.class));
    }

    protected Predicate<Class<? extends Event>> getTypeFilter() {
        return c -> eventHandlers.containsKey(c);
    }

    @Override
    public void accept(Event e) {
        last = e;
        log.debug("applying " + e + " to " + getClass().getSimpleName());
        Consumer<Event> method = eventHandlers.get(e.getClass());
        method.accept(e);
    }
}
