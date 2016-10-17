package com.mercateo.edu.foobank.cmd.transfer;

import java.util.UUID;

import com.mercateo.edu.infra.base.Command;

import lombok.Value;

@Value
public class CreditNotificationCommand implements Command {
	UUID reciever;
	int amount;
}