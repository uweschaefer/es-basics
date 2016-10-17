package com.mercateo.edu.foobank.evt;

import java.util.UUID;

import com.mercateo.edu.infra.base.Event;

import lombok.Value;

@Value
public class WithdrawnEvent implements Event {
	UUID aggregateId;
	int amount;

}
