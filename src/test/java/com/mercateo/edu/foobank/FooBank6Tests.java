package com.mercateo.edu.foobank;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.mercateo.edu.foobank.FooBank6Tests.MockNotificationService;
import com.mercateo.edu.foobank.entity.Account;
import com.mercateo.edu.foobank.services.CreditNotificationService;
import com.mercateo.edu.foobank.services.CreditNotificationServiceImpl;
import com.mercateo.edu.infra.EventsourcingInfraConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { MockNotificationService.class,
        EventsourcingInfraConfiguration.class, FooBankConfiguration.class })
@DirtiesContext
public class FooBank6Tests {

    @Autowired
    ApplicationFacade facade;

    @Autowired
    AccountRepository repo;

    @Autowired
    CreditNotificationService notification;

    @Test
    public void transfer() {
        UUID uweId = facade.createAccount("uwe", "schaefer");
        UUID andreasId = facade.createAccount("andreas", "heyder");

        facade.deposit(uweId, 100);
        facade.transfer(uweId, andreasId, 30);

        Account uwe = repo.find(uweId);
        Account andreas = repo.find(andreasId);

        assertEquals(70, uwe.getBalance());
        assertEquals(30, andreas.getBalance());

        try{
        	Thread.sleep(200); // yes, do not do that at home
        }catch(InterruptedException e){}
        
        verify(notification, Mockito.times(1)).sendEmailTo(andreasId, 30);
        verifyNoMoreInteractions(notification);

    }

    @Configuration
    public static class MockNotificationService {

        @Bean
        @Primary
        public CreditNotificationService creditNotificationService() {
            return Mockito.spy(new CreditNotificationServiceImpl());
        }
    }

}
