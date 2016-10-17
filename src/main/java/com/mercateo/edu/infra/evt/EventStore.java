package com.mercateo.edu.infra.evt;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.mercateo.edu.infra.base.Event;
import com.mercateo.edu.infra.view.PushView;

public interface EventStore {

	Stream<Event> fetch(UUID aggregateIdOrNull, Predicate<Class<? extends Event>> typeFilter, Event e);

	void subscribe(PushView view, UUID aggregateId, Predicate<Class<? extends Event>> typeFilter);

	void publish(List<Event> events);
}
