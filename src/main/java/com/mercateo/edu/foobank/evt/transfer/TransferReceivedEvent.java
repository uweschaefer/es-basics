package com.mercateo.edu.foobank.evt.transfer;

import java.util.UUID;

import com.mercateo.edu.infra.base.Event;

import lombok.Value;

@Value
public class TransferReceivedEvent implements Event {
    UUID aggregateId;

    int amount;
}
