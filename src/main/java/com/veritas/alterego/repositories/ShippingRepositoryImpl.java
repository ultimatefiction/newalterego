package com.veritas.alterego.repositories;

import com.veritas.alterego.util.RedissonClientProvider;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.Collection;

public class ShippingRepositoryImpl implements ShippingRepository {

    private final String FIRE_TIME_MAP = "com.veritas.alterego:groups:shipping_fire_times";
    private final String LAST_PAIR_LIST = "com.veritas.alterego:groups:%s:shipping_last";

    private RedissonClient client;
    private RMap<Long, Integer> fireTimeMap;

    public ShippingRepositoryImpl() {
        client = new RedissonClientProvider().getClient();
        fireTimeMap = client.getMap(FIRE_TIME_MAP);
    }

    @Override
    public int getFireTimeById(long groupId) {
        if (fireTimeMap.get(groupId) == null) {
            return 0;
        } else {
            return fireTimeMap.get(groupId);
        }
    }

    @Override
    public void addFireTimeById(long groupId, int fireTime) {
        fireTimeMap.put(groupId, fireTime);
    }

    @Override
    public void removeFireTimeById(long groupId) {
        fireTimeMap.remove(groupId);
    }

    @Override
    public void addPairToList(String pair, long groupId) {
        RList<String> list = client.getList(String.format(LAST_PAIR_LIST, groupId));
        list.add(pair);
        if (list.size() > 10) {
            list.remove(0);
        }
    }

    @Override
    public String getLastPair(long groupId) {
        RList<String> list = client.getList(String.format(LAST_PAIR_LIST, groupId));
        return list.get(list.size() - 1);
    }

    @Override
    public Collection<String> getListOfPairs(long groupId) {
        RList<String> list = client.getList(String.format(LAST_PAIR_LIST, groupId));
        return list.readAll();
    }

}
