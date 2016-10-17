package com.mercateo.edu.foobank;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.mercateo.edu.foobank.view.GoldCustomersView;
import com.mercateo.edu.infra.EventsourcingInfraConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { EventsourcingInfraConfiguration.class,
        FooBankConfiguration.class })
public class FooBank7Tests {

    @Autowired
    ApplicationFacade facade;

    @Autowired
    AccountRepository repo;

    @Autowired
    GoldCustomersView gold;

    @Test
    public void goldCustomerReport() {
        UUID uweId = facade.createAccount("uwe", "schaefer");
        UUID andreasId = facade.createAccount("andreas", "heyder");
        UUID joergId = facade.createAccount("joerg", "adler");
        UUID torstenId = facade.createAccount("torsten", "blum");
        UUID peterId = facade.createAccount("peter", "weber"); // :)

        facade.deposit(uweId, 100000);
        facade.transfer(uweId, andreasId, 30);
        facade.transfer(uweId, joergId, 10000);
        facade.transfer(uweId, torstenId, 20000);
        facade.transfer(uweId, peterId, 500);

        Collection<String> reportNotYetReady = gold.createGoldCustomerReport();
        assertTrue(reportNotYetReady.isEmpty());

        // eventually (:D) the report has to catch up

        try {
            // don't do this at home
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        Collection<String> reportUpToDate = gold.createGoldCustomerReport();

        assertEquals(2, reportUpToDate.size());
        assertTrue(reportUpToDate.contains("adler, joerg"));
        assertTrue(reportUpToDate.contains("blum, thorsten"));

    }

}
