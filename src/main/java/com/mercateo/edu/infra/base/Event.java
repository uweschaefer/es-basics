package com.mercateo.edu.infra.base;

import java.util.UUID;

/**
 * Expresses a Fact of the past – a thing that has happened and cannot be
 * changed.
 */
public interface Event extends Message {

	/**
	 * @return the ID of the aggregate root this Event belongs to, used for
	 *         filtering.
	 */
	UUID getAggregateId();
}
