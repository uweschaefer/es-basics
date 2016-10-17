package com.mercateo.edu.foobank.evt;

import java.util.UUID;

import com.mercateo.edu.infra.base.Event;

import lombok.Value;

@Value
public class DepositedEvent implements Event {
	UUID aggregateId;
	int amount;
}
