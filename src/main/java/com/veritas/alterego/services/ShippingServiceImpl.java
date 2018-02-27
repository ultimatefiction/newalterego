package com.veritas.alterego.services;

import com.veritas.alterego.repositories.GeneralRepository;
import com.veritas.alterego.repositories.ShippingRepository;

import java.util.Collection;

public class ShippingServiceImpl implements ShippingService {

    private GeneralRepository gr;
    private ShippingRepository sr;

    public ShippingServiceImpl(GeneralRepository gr, ShippingRepository sr) {
        this.gr = gr;
        this.sr = sr;
    }

    @Override
    public String getNewPair(long groupdId, int now) {
        long first = gr.getRandomMemberFromGroup(groupdId);
        long second = gr.getRandomMemberFromGroup(groupdId);
        while (first == second) {
            second = gr.getRandomMemberFromGroup(groupdId);
        }
        String result = String.format("%d:%d", first, second);
        sr.addFireTimeById(groupdId, now);
        sr.addPairToList(result, groupdId);
        return result;
    }

    @Override
    public String getCurrentPair(long groupdId) {
        return sr.getLastPair(groupdId);
    }

    @Override
    public boolean isReady(long groupId, int now) {
        return (now / 86400 != sr.getFireTimeById(groupId) / 86400);
    }

    @Override
    public Collection<String> getLatestPairs(long groupId) {
        return sr.getListOfPairs(groupId);
    }

}
