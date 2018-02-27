package com.veritas.alterego.repositories;

import com.veritas.alterego.util.RedissonClientProvider;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.protocol.ScoredEntry;

import java.util.Collection;

public class PidorRepositoryImpl implements PidorRepository {

    private final String FIRE_TIME_MAP = "com.veritas.alterego:groups:fire_times";
    private final String TOP_SSET = "com.veritas.alterego:groups:%s:top";
    private final String LAST_LIST = "com.veritas.alterego:groups:%s:last";

    private RedissonClient client;
    private RMap<Long, Integer> fireTimeMap;

    public PidorRepositoryImpl() {
        client = new RedissonClientProvider().getClient();
        fireTimeMap = client.getMap(FIRE_TIME_MAP);
    }

    @Override
    public int getFireTimeById(long id) {
        if (fireTimeMap.get(id) == null) {
            return 0;
        } else {
            return fireTimeMap.get(id);
        }
    }

    @Override
    public void addFireTimeById(long id, int fireTime) {
        fireTimeMap.put(id, fireTime);
    }

    @Override
    public void removeFireTimeById(long id) {
        fireTimeMap.remove(id);
    }

    @Override
    public boolean topContainsId(long userId, long groupId) {
        RScoredSortedSet<Long> topSet = client.getScoredSortedSet(String.format(TOP_SSET, groupId));
        return topSet.contains(userId);
    }

    @Override
    public double getScoreFromTopById(long userId, long groupId) {
        RScoredSortedSet<Long> topSet = client.getScoredSortedSet(String.format(TOP_SSET, groupId));
        return topSet.getScore(userId);
    }

    @Override
    public void addScoreAndIdToTop(long userId, double score, long groupId) {
        RScoredSortedSet<Long> topSet = client.getScoredSortedSet(String.format(TOP_SSET, groupId));
        topSet.add(score, userId);
    }

    @Override
    public void removeByIdFromTop(long userId, long groupId) {
        RScoredSortedSet<Long> topSet = client.getScoredSortedSet(String.format(TOP_SSET, groupId));
        topSet.remove(userId);
    }

    @Override
    public Collection<ScoredEntry<Long>> getNFromTop(long groupId, int count, boolean isReversed) {
        RScoredSortedSet<Long> topSet = client.getScoredSortedSet(String.format(TOP_SSET, groupId));
        if (isReversed) {
            return topSet.entryRange(0, count);
        } else {
            return topSet.entryRangeReversed(0, count);
        }
    }

    @Override
    public void addIdToList(long userId, long groupId) {
        RList<Long> lastSelected = client.getList(String.format(LAST_LIST, groupId));
        lastSelected.add(userId);
        if (lastSelected.size() > 10) {
            lastSelected.remove(0);
        }
    }

    @Override
    public Long getLast(long groupId) {
        RList<Long> lastSelected = client.getList(String.format(LAST_LIST, groupId));
        return lastSelected.get(lastSelected.size() - 1);
    }

    @Override
    public Collection<Long> getList(long groupId) {
        RList<Long> lastSelected = client.getList(String.format(LAST_LIST, groupId));
        return lastSelected.readAll();
    }
}
