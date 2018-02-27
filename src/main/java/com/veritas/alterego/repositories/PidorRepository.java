package com.veritas.alterego.repositories;

import org.redisson.client.protocol.ScoredEntry;

import java.util.Collection;

public interface PidorRepository {

    int getFireTimeById(long id);
    void addFireTimeById(long id, int fireTime);
    void removeFireTimeById(long id);

    boolean topContainsId(long userId, long groupId);
    double getScoreFromTopById(long userId, long groupId);
    void addScoreAndIdToTop(long userId, double score, long groupId);
    void removeByIdFromTop(long userId, long groupId);
    Collection<ScoredEntry<Long>> getNFromTop(long groupId, int count, boolean isReversed);

    void addIdToList(long userId, long groupId);
    Long getLast(long groupId);
    Collection<Long> getList(long groupId);

}
