package com.mercateo.edu.foobank.cmd.transfer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mercateo.edu.foobank.services.CreditNotificationService;
import com.mercateo.edu.infra.cmd.CommandHandler;
import com.mercateo.edu.infra.cmd.Effects;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CreditNotificationHandler extends CommandHandler<CreditNotificationCommand> {

    final CreditNotificationService s;

    @Override
    public Effects apply(CreditNotificationCommand t) {
        s.sendEmailTo(t.getReciever(), t.getAmount());
        // probably, we also want to emit an event saying the mail was sent, but
        // that is not the point.
        return Effects.none();
    }

}
