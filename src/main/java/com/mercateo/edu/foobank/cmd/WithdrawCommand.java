package com.mercateo.edu.foobank.cmd;

import java.util.UUID;

import com.mercateo.edu.infra.base.Command;

import lombok.Value;

@Value
public class WithdrawCommand implements Command {
	UUID id;
	int amount;
}