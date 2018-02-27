package com.veritas.alterego.repositories;

import java.util.Collection;

public interface ShippingRepository {

    int getFireTimeById(long groupId);
    void addFireTimeById(long groupId, int fireTime);
    void removeFireTimeById(long groupId);

    void addPairToList(String pair, long groupId);
    String getLastPair(long groupId);
    Collection<String> getListOfPairs(long groupId);

}
