package com.mercateo.edu.foobank.cmd.transfer;

import org.springframework.stereotype.Component;

import com.mercateo.edu.infra.cmd.CommandHandler;
import com.mercateo.edu.infra.cmd.Effects;

@Component
public class TransferHandler extends CommandHandler<TransferCommand> {

    @Override
    public Effects apply(TransferCommand t) throws RuntimeException {
        // TODO
        return Effects.none();
    }

}
