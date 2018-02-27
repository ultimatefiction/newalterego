package com.veritas.alterego.modules;

import com.veritas.alterego.util.ModuleResponse;
import org.telegram.telegrambots.api.objects.Update;

public class LazyBotModule implements BotModule {

    @Override
    public ModuleResponse accept(Update update) {
        if (update.getMessage().hasText() && update.getMessage().getText().contains("/ping")) {
            System.out.println("[>] pong");
            return new ModuleResponse().setReplyMessage("pong", update);
        }
        return new ModuleResponse();
    }

}
