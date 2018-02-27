package com.veritas.alterego.modules;

import com.veritas.alterego.util.ModuleResponse;
import org.telegram.telegrambots.api.objects.Update;

public class HelpModule implements BotModule {

    private final String helpText = "/man - помощь\n" +
            "\n" +
            "/pidor - выбрать пидора дня\n" +
            "/pidor_list - показать 10 последних пидоров дня\n" +
            "/pidor_top - показать десять самых больших пидоров в чате\n" +
            "\n" +
            "/ship - выбрать пейринг\n" +
            "/ship_last - показать десять последних пейрингов";

    @Override
    public ModuleResponse accept(Update update) {
        if (update.getMessage().hasText() && update.getMessage().getText().contains("/man")) {
            return new ModuleResponse().setReplyMessage(helpText, update);
        }
        return new ModuleResponse();
    }

}
