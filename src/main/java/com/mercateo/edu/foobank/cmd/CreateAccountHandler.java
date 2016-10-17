package com.mercateo.edu.foobank.cmd;

import org.springframework.stereotype.Component;

import com.mercateo.edu.foobank.evt.AccountCreatedEvent;
import com.mercateo.edu.infra.cmd.CommandHandler;
import com.mercateo.edu.infra.cmd.Effects;

@Component
public class CreateAccountHandler extends CommandHandler<CreateAccountCommand> {

	@Override
	public Effects apply(CreateAccountCommand c) {
		return Effects.of(new AccountCreatedEvent(c.getId(), c.getFirst(), c.getLast()));
	}
}
