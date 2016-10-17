package com.mercateo.edu.foobank;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.mercateo.edu.foobank.entity.Account;
import com.mercateo.edu.infra.EventsourcingInfraConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { EventsourcingInfraConfiguration.class, FooBankConfiguration.class })
public class FooBank1Tests {

	@Autowired
	ApplicationFacade facade;

	@Autowired
	AccountRepository repo;

	@Test
	public void createAccountRoundTrip() {

		// Arrange

		UUID uweId = facade.createAccount("uwe", "schaefer");
		UUID andreasId = facade.createAccount("andreas", "heyder");

		// Act

		Account uwe = repo.find(uweId);
		Account andreas = repo.find(andreasId);

		// Assert

		assertNotNull(uwe);
		assertNotNull(andreas);

		assertEquals(0, uwe.getBalance());
		assertEquals(0, andreas.getBalance());

	}

	@Test
	public void depositRoundTrip() {
		UUID uweId = facade.createAccount("uwe", "schaefer");

		facade.deposit(uweId, 100);

		Account uwe = repo.find(uweId);
		assertEquals(100, uwe.getBalance());
	}

	@Test
	public void withdrawRoundTrip() {
		UUID uweId = facade.createAccount("uwe", "schaefer");

		facade.deposit(uweId, 100);
		facade.withdraw(uweId, 80);

		Account uwe = repo.find(uweId);
		assertEquals(20, uwe.getBalance());
	}

}
