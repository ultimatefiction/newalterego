package com.veritas.alterego.modules;

import com.veritas.alterego.repositories.GeneralRepository;
import com.veritas.alterego.repositories.PidorRepository;
import com.veritas.alterego.services.GeneralService;
import com.veritas.alterego.services.GeneralServiceImpl;
import com.veritas.alterego.services.PidorService;
import com.veritas.alterego.services.PidorServiceImpl;
import com.veritas.alterego.util.Command;
import com.veritas.alterego.util.ModuleResponse;
import org.redisson.client.protocol.ScoredEntry;
import org.telegram.telegrambots.api.objects.Update;

import java.util.Collection;

public class PidorBotModule implements BotModule {

    private PidorService ps;
    private GeneralService gs;

    private final String PIDOR_CALL = "Пидором дня торжественно объявляется:\n\uD83D\uDD25%s\uD83D\uDD25";
    private final String REPEATATIVE_CALL = "Сегодняшний пидор дня: %s!";

    private final int MAX_TOP_LIST = 10;

    public PidorBotModule(GeneralRepository gr, PidorRepository pr) {
        ps = new PidorServiceImpl(gr, pr);
        gs = new GeneralServiceImpl(gr);
    }

    @Override
    public ModuleResponse accept(Update update) {
        if (update.getMessage().hasText() && update.getMessage().getChatId()<0) {
            String messageText = update.getMessage().getText();
            switch (messageText) {
                case "/pidor":
                    return doPidor(update);
                case "/pidor_top":
                    return doTop(update);
                case "/pidor_last":
                    return doLast(update);
            }
            switch (messageText) {
                case "/pidor@alter_egobot":
                    return doPidor(update);
                case "/pidor_top@alter_egobot":
                    return doTop(update);
                case "/pidor_last@alter_egobot":
                    return doLast(update);
            }
        }
        return new ModuleResponse();
    }

    @Command(pattern = "/pidor")
    private ModuleResponse doPidor(Update update) {
        System.out.println("[>] ACTIVATED PIDOR");
        String message;
        long chatId = update.getMessage().getChat().getId();
        int now = update.getMessage().getDate();
        if (ps.isReady(chatId, now)) {
            message = String.format(PIDOR_CALL, gs.getRef(ps.selectNew(chatId, now), true));
        } else {
            message = String.format(REPEATATIVE_CALL, gs.getRef(ps.getCurrent(chatId), false));
        }
        return new ModuleResponse().setReplyMessage(message, update);
    }

    @Command(pattern = "/pidor_top")
    private ModuleResponse doTop(Update update) {
        System.out.println("[>] ACTIVATED PIDOR TOP");
        Collection<ScoredEntry<Long>> values = ps.getTop(update.getMessage().getChatId(), MAX_TOP_LIST);
        StringBuilder sb = new StringBuilder();
        sb.append("Десять самых больших пидоров за последнее время:\n");
        int count = 1;
        for (ScoredEntry<Long> e : values) {
            sb.append(String.format("%s. %s -- %s раз\n", count, gs.getRef(e.getValue(), false), e.getScore()));
            count++;
        }
        return new ModuleResponse().setReplyMessage(sb.toString(), update);
    }

    @Command(pattern = "/pidor_last")
    private ModuleResponse doLast(Update update) {
        System.out.println("[>] ACTIVATED PIDOR LAST");
        Collection<Long> latest = ps.getLatest(update.getMessage().getChatId());
        StringBuilder sb = new StringBuilder();
        sb.append("Десять последних избранных пидоров:\n");
        int count = 1;
        for (long userId : latest) {
            sb.append(String.format("%s. %s\n", count, gs.getRef(userId, false)));
            count++;
        }
        return new ModuleResponse().setReplyMessage(sb.toString(), update);
    }

}
