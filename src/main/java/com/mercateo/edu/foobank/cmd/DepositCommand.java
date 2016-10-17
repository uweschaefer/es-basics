package com.mercateo.edu.foobank.cmd;

import java.util.UUID;

import com.mercateo.edu.infra.base.Command;

import lombok.Value;

@Value
public class DepositCommand implements Command {
	UUID id;
	int amount;
}