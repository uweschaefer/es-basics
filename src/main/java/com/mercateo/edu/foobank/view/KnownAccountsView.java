package com.mercateo.edu.foobank.view;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mercateo.edu.infra.evt.EventStore;
import com.mercateo.edu.infra.view.PullView;

@Component
public class KnownAccountsView extends PullView {

    @Autowired
    public KnownAccountsView(EventStore es) {
        super(es);
    }

    private final Set<UUID> knownAccounts = new HashSet<>();

    // TODO consume Events

    public boolean exists(UUID id) {
        return false; // TODO
    }
}
