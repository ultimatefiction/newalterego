package com.veritas.alterego.services;

import java.util.Collection;

public interface ShippingService {

    String getNewPair(long groupdId, int now);

    String getCurrentPair(long groupdId);

    boolean isReady(long groupId, int now);

    Collection<String> getLatestPairs(long groupId);

}
