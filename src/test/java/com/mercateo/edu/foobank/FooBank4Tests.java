package com.mercateo.edu.foobank;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.mercateo.edu.foobank.cmd.transfer.AccountUnknownException;
import com.mercateo.edu.foobank.cmd.transfer.UnfundedTransferException;
import com.mercateo.edu.foobank.entity.Account;
import com.mercateo.edu.infra.EventsourcingInfraConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { EventsourcingInfraConfiguration.class,
        FooBankConfiguration.class })
@DirtiesContext
public class FooBank4Tests {

    @Autowired
    ApplicationFacade facade;

    @Autowired
    AccountRepository repo;

    @Test(expected = AccountUnknownException.class)
    public void transferToUnknown() {
        UUID uweId = facade.createAccount("uwe", "schaefer");

        facade.deposit(uweId, 10000);
        facade.transfer(uweId, UUID.randomUUID(), 1000);

        fail("transfer to unknown succeeded");

    }

    @Test(expected = UnfundedTransferException.class)
    public void transferUnfunded() {
        UUID uweId = facade.createAccount("uwe", "schaefer");
        UUID andreasId = facade.createAccount("andreas", "heyder");

        facade.deposit(uweId, 100);
        facade.transfer(uweId, andreasId, 1000);

        fail("transfer went into debt");

    }

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

    }

}
