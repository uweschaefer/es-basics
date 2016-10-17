package com.mercateo.edu.infra.view;

import java.util.UUID;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.mercateo.edu.infra.evt.EventStore;

import lombok.NonNull;

public abstract class PushView extends View implements ApplicationContextAware {

	public PushView(@NonNull EventStore es) {
		super(es);
	}

	public PushView(@NonNull EventStore es, UUID aggregateId) {
		super(es, aggregateId);
	}

	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		applicationContext.getBean(EventStore.class).subscribe(this, aggregateId, getTypeFilter());
	}
}
