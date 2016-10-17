package com.mercateo.edu.infra.cmd;

import java.util.Arrays;
import java.util.stream.Stream;

import com.mercateo.edu.infra.base.Command;
import com.mercateo.edu.infra.base.Event;
import com.mercateo.edu.infra.base.Message;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Effects {

	@NonNull
	@Getter
	private final Message[] msgs;

	public static Effects of(@NonNull Message... messages) {
		return new Effects(messages);
	}

	public Stream<Command> getCommands() {
		return Arrays.stream(msgs).filter(m -> m instanceof Command).map(Command.class::cast);
	}

	public Stream<Event> getEvents() {
		return Arrays.stream(msgs).filter(m -> m instanceof Event).map(Event.class::cast);
	}

	public static Effects none() {
		return new Effects(new Message[0]);
	}

}
