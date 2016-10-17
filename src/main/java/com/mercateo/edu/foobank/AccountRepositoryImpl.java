package com.mercateo.edu.foobank;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mercateo.edu.foobank.entity.Account;
import com.mercateo.edu.foobank.view.AccountView;
import com.mercateo.edu.infra.evt.EventStore;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountRepositoryImpl implements AccountRepository {
	final EventStore es;

	@Override
	public Account find(UUID id) {
		return new AccountView(es, id).getAccount();
	}

}
