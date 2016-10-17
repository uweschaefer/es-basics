package com.mercateo.edu.foobank.view;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mercateo.edu.foobank.AccountRepository;
import com.mercateo.edu.foobank.evt.transfer.TransferReceivedEvent;
import com.mercateo.edu.infra.evt.EventStore;
import com.mercateo.edu.infra.view.PushView;

@Component
// note this being a PushView rather than a pull-View!
public class GoldCustomersView extends PushView {

    private final AccountRepository repo;

    @Autowired
    public GoldCustomersView(EventStore es, AccountRepository repo) {
        super(es);
        this.repo = repo;
    }

    // TODO define query-optimized datastructure

    @EventConsumer
    public void handle(TransferReceivedEvent e) {

        // <don't touch this>
        try {
            // lets just assume this takes a while.
            // for demonstration purposes only
            Thread.sleep(200);
        } catch (InterruptedException e1) {
        }
        // </don't touch this>

        // TODO
    }

    public Collection<String> createGoldCustomerReport() {
        // TODO
        return Collections.emptyList();
    }

}
