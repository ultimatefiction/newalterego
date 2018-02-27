package com.veritas.alterego.util;

import com.veritas.alterego.repositories.GeneralRepository;
import com.veritas.alterego.repositories.GeneralRepositoryImpl;
import com.veritas.alterego.repositories.PidorRepository;
import com.veritas.alterego.repositories.PidorRepositoryImpl;
import com.veritas.alterego.services.GeneralService;
import com.veritas.alterego.services.GeneralServiceImpl;
import org.redisson.client.protocol.ScoredEntry;

import java.util.Collection;

public class RedisViewer {

    public static void main(String[] args) {

        PidorRepository pr = new PidorRepositoryImpl();
        GeneralRepository gr = new GeneralRepositoryImpl();
        GeneralService gs = new GeneralServiceImpl(gr);

        viewAllMembers(gr, gs);
        viewAllPidorLists(gr, pr, gs);
        viewAllPidorTops(gr, pr, gs);

    }

    private static void viewAllMembers(GeneralRepository gr, GeneralService gs) {
        System.out.println("MEMBERS");
        Collection<Long> chatIds = gr.getAllGroupIds();
        for (long chatId : chatIds) {
            System.out.printf("%s (%s)%n", gr.getGroupNameById(chatId), chatId);
            Collection<Long> memberIds = gr.getAllMembersFromGroup(chatId);
            for (long userId : memberIds) {
                System.out.printf("> %s %s%n", gs.getRef(userId, true), userId);
            }
            System.out.println();
        }
    }

    private static void viewAllPidorLists(GeneralRepository gr, PidorRepository pr, GeneralService gs) {
        System.out.println("LATEST PIDORS");
        Collection<Long> chatIds = gr.getAllGroupIds();
        for (long chatId : chatIds) {
            System.out.printf("%s (%s)%n", gr.getGroupNameById(chatId), chatId);
            Collection<Long> pidorList = pr.getList(chatId);
            for (long userId : pidorList) {
                System.out.printf("> %s %s%n", gs.getRef(userId, true), userId);
            }
            System.out.println();
        }
    }

    private static void viewAllPidorTops(GeneralRepository gr, PidorRepository pr, GeneralService gs){
        System.out.println("PIDOR TOPS");
        Collection<Long> chatIds = gr.getAllGroupIds();
        for (long chatId : chatIds) {
            System.out.printf("%s (%s)%n", gr.getGroupNameById(chatId), chatId);
            Collection<ScoredEntry<Long>> pidorTop = pr.getNFromTop(chatId, 10, false);
            for (ScoredEntry<Long> entry : pidorTop) {
                System.out.printf("> %s : %s %s%n", entry.getScore(), gs.getRef(entry.getValue(), true), entry.getValue());
            }
            System.out.println();
        }
    }

}
