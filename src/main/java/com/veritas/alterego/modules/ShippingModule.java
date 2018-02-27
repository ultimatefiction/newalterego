package com.veritas.alterego.modules;

import com.veritas.alterego.repositories.GeneralRepository;
import com.veritas.alterego.repositories.ShippingRepository;
import com.veritas.alterego.services.GeneralService;
import com.veritas.alterego.services.GeneralServiceImpl;
import com.veritas.alterego.services.ShippingService;
import com.veritas.alterego.services.ShippingServiceImpl;
import com.veritas.alterego.util.Command;
import com.veritas.alterego.util.ModuleResponse;
import org.telegram.telegrambots.api.objects.Update;

import java.util.Collection;

public class ShippingModule implements BotModule {

    private GeneralService gs;
    private ShippingService ss;

    private final String NEW_SHIP_MESSAGE = "Сегодняшний пейринг дня:\n❤️ %s + %s ❤️";
    private final String REPEAT_SHIP_MESSAGE = "Сегодняшний фанфик уже написан, главные герои:\n%s + %s = ❤️";
    private final String NOT_ENOUGH_MEMBERS = "Извините, но я пока знаю слишком мало людей в этом чате чтоб сделать из них пейринг. Попобуйте попозже! ❤️";

    public ShippingModule(GeneralRepository gr, ShippingRepository sr) {
        this.gs = new GeneralServiceImpl(gr);
        this.ss = new ShippingServiceImpl(gr, sr);
    }

    @Override
    public ModuleResponse accept(Update update) {
        if (update.getMessage().hasText() && update.getMessage().getChatId()<0) {
            String messageText = update.getMessage().getText();
            switch (messageText) {
                case "/ship":
                    return doShip(update);
                case "/ship_list":
                    return doShipList(update);
            }
            switch (messageText) {
                case "/ship@alter_egobot":
                    return doShip(update);
                case "/ship_list@alter_egobot":
                    return doShipList(update);
            }
        }
        return new ModuleResponse();
    }

    @Command(pattern = "/ship")
    private ModuleResponse doShip(Update update) {
        long chatId = update.getMessage().getChatId();
        int now = update.getMessage().getDate();
        String message = "";
        if (gs.getMembersCount(chatId) >= 2) {
            if (ss.isReady(chatId, now)) {
                long[] lovers = parseLovers(ss.getNewPair(chatId, now));
                message = String.format(NEW_SHIP_MESSAGE,
                        gs.getRef(lovers[0], true),
                        gs.getRef(lovers[1], true));
            } else {
                long[] lovers = parseLovers(ss.getCurrentPair(chatId));
                message = String.format(REPEAT_SHIP_MESSAGE,
                        gs.getRef(lovers[0], false),
                        gs.getRef(lovers[1], false));
            }
        } else {
            message = NOT_ENOUGH_MEMBERS;
        }
        return new ModuleResponse().setReplyMessage(message, update);
    }

    @Command(pattern = "/ship_last")
    private ModuleResponse doShipList(Update update) {
        Collection<String> latest = ss.getLatestPairs(update.getMessage().getChatId());
        StringBuilder sb = new StringBuilder();
        sb.append("Десять последних пейрингов:\n");
        int count = 1;
        for (String pair : latest) {
            long[] lovers = parseLovers(pair);
            sb.append(
                    String.format("%s. %s + %s\n", count,
                    gs.getRef(lovers[0], false),
                    gs.getRef(lovers[1], false)));
            count++;
        }
        return new ModuleResponse().setReplyMessage(sb.toString(), update);
    }

    private long[] parseLovers(String pair) {
        long[] lovers = new long[2];
        String[] parts = pair.split(":");
        lovers[0] = Long.parseLong(parts[0]);
        lovers[1] = Long.parseLong(parts[1]);
        return lovers;
    }

}
