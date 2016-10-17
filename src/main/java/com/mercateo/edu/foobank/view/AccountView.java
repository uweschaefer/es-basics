package com.mercateo.edu.foobank.view;

import java.util.UUID;

import com.mercateo.edu.foobank.entity.Account;
import com.mercateo.edu.foobank.evt.AccountCreatedEvent;
import com.mercateo.edu.infra.evt.EventStore;
import com.mercateo.edu.infra.view.PullView;

import lombok.Getter;

public class AccountView extends PullView {

    @Getter
    private Account account;

    public AccountView(EventStore es, UUID foo1) {
        super(es, foo1);
        pullEvents(); // pull relevant events from the EventStore
    }

    @EventConsumer
    public void handle(AccountCreatedEvent evt) {
        this.account = new Account(evt.getAggregateId(), evt.getFirstName(), evt.getLastName(), 0);
    }

    // TODO add @EventConsumer annotated methods here, to also handle different
    // Events
}