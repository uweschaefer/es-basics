package com.mercateo.edu.foobank.view;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.mercateo.edu.foobank.evt.DepositedEvent;
import com.mercateo.edu.infra.evt.EventStore;
import com.mercateo.edu.infra.view.PullView;

public class ValuedCustomerReportView extends PullView {

    public ValuedCustomerReportView(EventStore es) {
        super(es);
        pullEvents();
    }

    final Set<UUID> candidates = new HashSet<>();

    final Set<UUID> valuedCustomers = new HashSet<>();

    @EventConsumer
    public void apply(DepositedEvent evt) {
        if (evt.getAmount() >= 1000) {

            UUID accountId = evt.getAggregateId();

            if (isValuedCustomer(accountId))
                // all done
                return;

            if (candidates.contains(accountId)) {
                // promote to valued
                candidates.remove(accountId);
                valuedCustomers.add(accountId);
            } else
                candidates.add(accountId);
        }
    }

    public final Collection<UUID> getValuedCustomers() {
        return Collections.unmodifiableSet(valuedCustomers);
    }

    public final boolean isValuedCustomer(UUID accountId) {
        return getValuedCustomers().contains(accountId);
    }

}
