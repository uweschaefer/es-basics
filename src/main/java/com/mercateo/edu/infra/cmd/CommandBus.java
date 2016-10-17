package com.mercateo.edu.infra.cmd;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mercateo.edu.infra.base.Command;
import com.mercateo.edu.infra.evt.EventStore;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
/**
 * used to post (sync or async) a command. Posting a command means to find the
 * appropriate Command Handler and passing it along, recodgin its effects, if it
 * was not rejected.
 */
public class CommandBus {
	@Autowired
	public CommandBus(@NonNull EventStore es) {
		this.eventStore = es;
	}

	private final EventStore eventStore;
	private final Map<Class<?>, CommandHandler<?>> handlers = new LinkedHashMap<>();
	private final ExecutorService execService = Executors.newSingleThreadExecutor();

	@SuppressWarnings("unchecked")
	public synchronized <T extends Command> void post(@NonNull T cmd) throws RuntimeException {
		CommandHandler<T> h = (CommandHandler<T>) handlers.get(cmd.getClass());
		if (h == null)
			throw new IllegalArgumentException("No handler for " + cmd.getClass().getSimpleName());

		Effects effects = h.apply(cmd); // can throw RTEs
		publish(effects);
	}

	// should be transactional from here
	private void publish(@NonNull Effects effects) {
		// post all commands (async)
		effects.getCommands().forEach(this::postAsync);
		// publish events
		eventStore.publish(effects.getEvents().collect(Collectors.toList()));
	}

	private void postAsync(Command c) {
		// obviously improvable :)
		try {
			execService.execute(() -> post(c));
		} catch (RuntimeException e) {
			log.error("did not apply {}, due to {}", c, e.getMessage(), e);
		}

	}

	<C extends Command> void register(@NonNull Class<C> class1, @NonNull CommandHandler<C> ch) {
		log.info("registration of " + ch.getClass().getSimpleName() + " for type " + class1.getSimpleName());
		handlers.put(class1, ch);
	}

	@PreDestroy
	public void cleanup() {
		execService.shutdownNow();
		try {
			execService.awaitTermination(5, TimeUnit.SECONDS);
			log.info("Shutdown complete");
		} catch (InterruptedException e) {
			log.error("Shutdown failed ", e);
		}
	}
}
