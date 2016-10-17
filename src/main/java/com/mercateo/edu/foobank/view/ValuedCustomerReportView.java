package com.mercateo.edu.foobank.view;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import org.assertj.core.util.Sets;

import com.mercateo.edu.foobank.evt.DepositedEvent;
import com.mercateo.edu.infra.evt.EventStore;
import com.mercateo.edu.infra.view.PullView;

public class ValuedCustomerReportView extends PullView {

    public ValuedCustomerReportView(EventStore es) {
        super(es);
    }

    final Set<UUID> candidates = Sets.newHashSet();

    final Set<UUID> valuedCustomers = Sets.newHashSet();

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
        pullEvents();
        return Collections.unmodifiableSet(valuedCustomers);
    }

    public final boolean isValuedCustomer(UUID accountId) {
        return getValuedCustomers().contains(accountId);
    }

}
