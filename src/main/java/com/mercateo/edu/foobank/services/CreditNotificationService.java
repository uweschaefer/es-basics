package com.mercateo.edu.foobank.services;

import java.util.UUID;

public interface CreditNotificationService {

	public void sendEmailTo(UUID accountId, int amountRecieved);

}
