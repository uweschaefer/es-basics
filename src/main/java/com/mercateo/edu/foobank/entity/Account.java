package com.mercateo.edu.foobank.entity;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Account {
    private final UUID id;

    private final String firstName;

    private final String lastName;

    private int balance = 0;

    // to credit = gutschreiben
    public void credit(int amount) {
        balance += amount;
    }

    // to debit = belasten
    public void debit(int amount) {
        balance -= amount;
    }
}
