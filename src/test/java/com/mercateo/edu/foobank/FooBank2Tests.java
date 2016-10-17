package com.mercateo.edu.foobank;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.mercateo.edu.foobank.report.ValuedCustomerReportView;
import com.mercateo.edu.infra.EventsourcingInfraConfiguration;
import com.mercateo.edu.infra.evt.EventStore;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { EventsourcingInfraConfiguration.class,
        FooBankConfiguration.class })
public class FooBank2Tests {

    @Autowired
    ApplicationFacade facade;

    @Autowired
    AccountRepository repo;

    @Autowired
    EventStore es;

    @Test
    public void valuedCustomerReport() {
        UUID uweId = facade.createAccount("uwe", "schaefer");
        UUID andreasId = facade.createAccount("andreas", "heyder");

        facade.deposit(uweId, 100);
        facade.deposit(andreasId, 100);
        facade.withdraw(uweId, 8);
        facade.withdraw(uweId, 2);

        ValuedCustomerReportView report1 = new ValuedCustomerReportView(es);
        assertTrue(report1.getValuedCustomers().isEmpty());

        facade.deposit(uweId, 1000);
        facade.deposit(andreasId, 900);
        facade.withdraw(uweId, 8);
        facade.withdraw(uweId, 2);

        ValuedCustomerReportView report2 = new ValuedCustomerReportView(es);
        assertTrue(report2.getValuedCustomers().isEmpty());

        facade.deposit(uweId, 1000);
        facade.deposit(andreasId, 10000);
        facade.withdraw(uweId, 8);
        facade.withdraw(uweId, 2);

        ValuedCustomerReportView report3 = new ValuedCustomerReportView(es);
        assertFalse(report3.getValuedCustomers().isEmpty());
        assertTrue(report3.isValuedCustomer(uweId));

        facade.deposit(uweId, 2000);
        facade.deposit(andreasId, 7000);

        ValuedCustomerReportView report4 = new ValuedCustomerReportView(es);
        assertTrue(report4.isValuedCustomer(uweId));
        assertTrue(report4.isValuedCustomer(andreasId));

    }

}
