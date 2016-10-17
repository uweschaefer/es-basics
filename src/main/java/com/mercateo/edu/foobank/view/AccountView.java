package com.mercateo.edu.foobank.view;

import java.util.UUID;

import com.mercateo.edu.foobank.entity.Account;
import com.mercateo.edu.foobank.evt.AccountCreatedEvent;
import com.mercateo.edu.foobank.evt.DepositedEvent;
import com.mercateo.edu.foobank.evt.WithdrawnEvent;
import com.mercateo.edu.infra.evt.EventStore;
import com.mercateo.edu.infra.view.PullView;

import lombok.Getter;

public class AccountView extends PullView {

    @Getter
    private Account account;

    public AccountView(EventStore es, UUID aggregateId) {
        super(es, aggregateId);
        pullEvents(); // pull relevant events from the EventStore <---- OH LOOK!
    }

    @EventConsumer
    public void handle(AccountCreatedEvent evt) {
        this.account = new Account(evt.getAggregateId(), evt.getFirstName(), evt.getLastName(), 0);
    }

    @EventConsumer
    public void handle(DepositedEvent evt) {
        this.account.credit(evt.getAmount());
    }

    @EventConsumer
    public void handle(WithdrawnEvent evt) {
        this.account.debit(evt.getAmount());
    }
}