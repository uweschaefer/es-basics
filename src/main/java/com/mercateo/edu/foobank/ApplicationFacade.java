package com.mercateo.edu.foobank;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mercateo.edu.foobank.cmd.CreateAccountCommand;
import com.mercateo.edu.foobank.cmd.DepositCommand;
import com.mercateo.edu.foobank.cmd.WithdrawCommand;
import com.mercateo.edu.infra.cmd.CommandBus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationFacade {

    final CommandBus bus;

    public UUID createAccount(String first, String last) {
        UUID id = UUID.randomUUID();
        bus.post(new CreateAccountCommand(id, first, last));
        return id;
    }

    public void deposit(UUID id, int amount) {
        bus.post(new DepositCommand(id, amount));
    }

    public void withdraw(UUID id, int amount) {
        bus.post(new WithdrawCommand(id, amount));
    }

}
