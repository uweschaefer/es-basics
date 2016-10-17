package com.mercateo.edu.foobank.cmd;

import org.springframework.stereotype.Component;

import com.mercateo.edu.foobank.evt.WithdrawnEvent;
import com.mercateo.edu.infra.cmd.CommandHandler;
import com.mercateo.edu.infra.cmd.Effects;

@Component
public class WithdrawalHandler extends CommandHandler<WithdrawCommand> {

	@Override
	public Effects apply(WithdrawCommand c) {
		return Effects.of(new WithdrawnEvent(c.getId(), c.getAmount()));
	}
}
