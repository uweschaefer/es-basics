package com.mercateo.edu.foobank.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CreditNotificationServiceImpl implements CreditNotificationService {

    @Override
    public void sendEmailTo(UUID accountId, int amountRecieved) {
        log.info("i just sent an Email to " + accountId + " about the credit of " + amountRecieved);
    }

}
