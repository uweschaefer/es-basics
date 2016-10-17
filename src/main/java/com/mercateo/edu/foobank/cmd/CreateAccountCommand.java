package com.mercateo.edu.foobank.cmd;

import java.util.UUID;

import com.mercateo.edu.infra.base.Command;

import lombok.Value;

@Value
public class CreateAccountCommand implements Command {
	UUID id;
	String first;
	String last;
}