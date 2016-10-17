package com.mercateo.edu.infra.evt;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.mercateo.edu.infra.base.Event;
import com.mercateo.edu.infra.view.PushView;

import lombok.NonNull;

@Component
public class EventStoreImpl implements EventStore {

	private final List<Event> eventLog = new CopyOnWriteArrayList<>();
	private final ExecutorService fanService = Executors.newFixedThreadPool(4);
	private final Map<PushView, Predicate<Event>> subscribers = new IdentityHashMap<>();

	@Override
	public void publish(@NonNull List<Event> events) {
		eventLog.addAll(events);
		fanOut(events);
	}

	private void fanOut(List<Event> events) {
		fanService.execute(() -> {
			events.forEach(evt -> subscribers.entrySet().forEach(e -> {
				if (e.getValue().test(evt))
					e.getKey().accept(evt);
			}));
		});
	}

	@Override
	public Stream<Event> fetch(UUID aggregateIdOrNull, @NonNull Predicate<Class<? extends Event>> typeFilter,
			Event lastOrNull) {

		Stream<Event> s = eventLog.stream();
		if (lastOrNull != null) {
			AtomicBoolean seekMode = new AtomicBoolean(true);
			s = s.filter(e -> {
				if (seekMode.get()) {
					seekMode.set(lastOrNull != e);
					return false;
				} else
					return true;
			});
		}

		s = s.filter(e -> typeFilter.test(e.getClass()));
		s = s.filter(e -> aggregateIdOrNull == null || aggregateIdOrNull.equals(e.getAggregateId()));

		return s;

	}

	@Override
	public void subscribe(@NonNull PushView view, UUID aggregateIdOrNull,
			@NonNull Predicate<Class<? extends Event>> typeFilter) {
		Predicate<Event> p = e -> typeFilter.test(e.getClass());
		if (aggregateIdOrNull != null) {
			p = p.and(e -> e.getAggregateId().equals(aggregateIdOrNull));
		}
		subscribers.put(view, p);
	}

}
