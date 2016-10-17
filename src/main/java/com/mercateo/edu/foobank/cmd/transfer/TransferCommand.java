package com.mercateo.edu.foobank.cmd.transfer;

import java.util.UUID;

import com.mercateo.edu.infra.base.Command;

import lombok.Value;

@Value
public class TransferCommand implements Command {
    UUID from;

    UUID to;

    int amount;
}
