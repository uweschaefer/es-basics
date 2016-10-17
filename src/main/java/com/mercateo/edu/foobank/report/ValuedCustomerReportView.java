package com.mercateo.edu.foobank.report;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.assertj.core.util.Sets;

import com.mercateo.edu.infra.evt.EventStore;
import com.mercateo.edu.infra.view.PullView;

public class ValuedCustomerReportView extends PullView {

    public ValuedCustomerReportView(EventStore es) {
        super(es);
    }

    // define appropriate Datamodel for aggregation, just a suggestion:
    final Set<UUID> candidates = Sets.newHashSet();

    final Set<UUID> valuedCustomers = new HashSet<>();

    // TODO create handle method for Events we want to listen to
    // see AccountView how to do that

    public final Collection<UUID> getValuedCustomers() {
        return Collections.unmodifiableSet(valuedCustomers);
    }

    public final boolean isValuedCustomer(UUID accountId) {
        return this.valuedCustomers.contains(accountId);
    }

}
