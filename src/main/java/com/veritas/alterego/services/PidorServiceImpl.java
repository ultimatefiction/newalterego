package com.veritas.alterego.services;

import com.veritas.alterego.repositories.GeneralRepository;
import com.veritas.alterego.repositories.PidorRepository;
import org.redisson.client.protocol.ScoredEntry;

import java.util.Collection;

public class PidorServiceImpl implements PidorService {

    private GeneralRepository gr;
    private PidorRepository pr;

    public PidorServiceImpl(GeneralRepository gr, PidorRepository pr) {
        this.gr = gr;
        this.pr = pr;
    }

    @Override
    public long selectNew(long groupId, int now) {
        long newPidorId = gr.getRandomMemberFromGroup(groupId);
        pr.addIdToList(newPidorId, groupId);
        pr.addFireTimeById(groupId, now);
        if (pr.topContainsId(newPidorId, groupId)) {
            double score = pr.getScoreFromTopById(newPidorId, groupId);
            pr.addScoreAndIdToTop(newPidorId, score + 1, groupId);
        } else {
            pr.addScoreAndIdToTop(newPidorId, 1, groupId);
        }
        return newPidorId;
    }

    @Override
    public long getCurrent(long groupId) {
        return pr.getLast(groupId);
    }

    @Override
    public boolean isReady(long groupId, int now) {
        return (now / 86400 != pr.getFireTimeById(groupId) / 86400);
    }

    @Override
    public Collection<Long> getLatest(long groupId) {
        return pr.getList(groupId);
    }

    @Override
    public Collection<ScoredEntry<Long>> getTop(long groupId, int count) {
        return pr.getNFromTop(groupId, count, false);
    }

}
