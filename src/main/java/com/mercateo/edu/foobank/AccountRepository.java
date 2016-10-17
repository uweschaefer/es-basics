package com.mercateo.edu.foobank;

import java.util.UUID;

import com.mercateo.edu.foobank.entity.Account;

public interface AccountRepository {
	Account find(UUID id);

}
