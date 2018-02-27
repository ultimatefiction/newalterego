package com.veritas.alterego.bots;

import com.veritas.alterego.modules.BotModule;
import com.veritas.alterego.modules.HelpModule;
import com.veritas.alterego.modules.PidorBotModule;
import com.veritas.alterego.modules.ShippingModule;
import com.veritas.alterego.repositories.GeneralRepository;
import com.veritas.alterego.repositories.GeneralRepositoryImpl;
import com.veritas.alterego.repositories.PidorRepository;
import com.veritas.alterego.repositories.PidorRepositoryImpl;
import com.veritas.alterego.repositories.ShippingRepository;
import com.veritas.alterego.repositories.ShippingRepositoryImpl;
import com.veritas.alterego.services.GeneralService;
import com.veritas.alterego.services.GeneralServiceImpl;
import com.veritas.alterego.util.ModuleResponse;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.LinkedList;

public class AlterEgoBot extends TelegramLongPollingBot{

    private LinkedList<BotModule> botModules;
    private GeneralService generalService;

    private final String BOT_NAME = "alter_egobot";
    private final String BOT_TOKEN = "token";

    public AlterEgoBot() {
        PidorRepository pr = new PidorRepositoryImpl();
        ShippingRepository sr = new ShippingRepositoryImpl();
        GeneralRepository gr = new GeneralRepositoryImpl();
        generalService = new GeneralServiceImpl(gr);
        botModules = new LinkedList<>();
        botModules.add(new PidorBotModule(gr, pr));
        botModules.add(new HelpModule());
        botModules.add(new ShippingModule(gr, sr));
    }

    @Override
    public void onUpdateReceived(Update update) {
        generalService.basicUpdate(update);

        //System.out.printf("[<] (%s) %s: %s%n",
        //        update.getMessage().getChat().getTitle(),
        //        update.getMessage().getFrom().getFirstName(),
        //        Messages.getMessageType(update.getMessage()));

        try {
            for (BotModule module : botModules) {
                ModuleResponse response = module.accept(update);
                if (response.hasMessage()) {
                    execute(response.getMessage());
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

}
