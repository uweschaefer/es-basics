package com.mercateo.edu.foobank;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.mercateo.edu.foobank.view.KnownAccountsView;
import com.mercateo.edu.infra.EventsourcingInfraConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { EventsourcingInfraConfiguration.class,
        FooBankConfiguration.class })
@DirtiesContext
public class FooBank5Tests {

    @Autowired
    KnownAccountsView known;

    @Autowired
    ApplicationFacade facade;

    @Test
    public void testKnownAccounts() throws Exception {
        UUID randomId = UUID.randomUUID();
        assertFalse(known.exists(randomId));

        UUID uwe = facade.createAccount("uwe", "schaefer");
        assertTrue(known.exists(uwe));
        assertFalse(known.exists(randomId));

    }
}
