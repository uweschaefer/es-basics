package com.mercateo.edu.foobank.cmd.transfer;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mercateo.edu.foobank.AccountRepository;
import com.mercateo.edu.foobank.cmd.NotifyCommand;
import com.mercateo.edu.foobank.evt.transfer.TransferReceivedEvent;
import com.mercateo.edu.foobank.evt.transfer.TransferSentEvent;
import com.mercateo.edu.foobank.view.KnownAccountsView;
import com.mercateo.edu.infra.cmd.CommandHandler;
import com.mercateo.edu.infra.cmd.Effects;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransferHandler extends CommandHandler<TransferCommand> {

    final AccountRepository repo;

    final KnownAccountsView knownAccounts;

    @Override
    public Effects apply(TransferCommand t) throws RuntimeException {

        // Criteria 1
        if (!exists(t.getFrom()) || !exists(t.getTo()))
            throw new AccountUnknownException();

        // Criteria 2
        if (repo.find(t.getFrom()).getBalance() < t.getAmount())
            throw new UnfundedTransferException();

        return Effects.of(//
                new TransferSentEvent(t.getFrom(), t.getAmount()), //
                new TransferReceivedEvent(t.getTo(), t.getAmount()),
                new NotifyCommand(t.getTo(), t.getAmount())
        		
        		);
    }

    private boolean exists(UUID id) {
        return knownAccounts.exists(id);
    }

}
