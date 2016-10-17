package com.mercateo.edu.foobank.cmd;

import org.springframework.stereotype.Component;

import com.mercateo.edu.foobank.evt.DepositedEvent;
import com.mercateo.edu.infra.cmd.CommandHandler;
import com.mercateo.edu.infra.cmd.Effects;

@Component
public class DepositionHandler extends CommandHandler<DepositCommand> {

	@Override
	public Effects apply(DepositCommand c) {
		return Effects.of(new DepositedEvent(c.getId(), c.getAmount()));
	}
}
