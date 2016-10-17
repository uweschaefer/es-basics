package com.mercateo.edu.infra.view;

import java.util.UUID;

import com.mercateo.edu.infra.evt.EventStore;

import lombok.NonNull;

public abstract class PullView extends View {

	public PullView(@NonNull EventStore es) {
		super(es);
	}

	public PullView(@NonNull EventStore es, UUID aggregateId) {
		super(es, aggregateId);
	}

	public void pullEvents() {
		es.fetch(aggregateId, getTypeFilter(), last).forEachOrdered(this::accept);
	}

}
