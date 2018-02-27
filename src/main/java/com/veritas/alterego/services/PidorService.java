package com.veritas.alterego.services;

import org.redisson.client.protocol.ScoredEntry;

import java.util.Collection;

public interface PidorService {

    long selectNew(long groupId, int fireTime);

    long getCurrent(long groupId);

    boolean isReady(long groupId, int now);

    Collection<Long> getLatest(long groupId);

    Collection<ScoredEntry<Long>> getTop(long groupId, int count);

}
