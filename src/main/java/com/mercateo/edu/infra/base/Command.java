package com.mercateo.edu.infra.base;

/**
 * Tells someone to do something. Commands can be rejected, if they are not
 * valid or the System is in a state, where this command must not be accepted
 *
 */
public interface Command extends Message {

}
