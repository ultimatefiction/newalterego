package com.veritas.alterego.modules;

import com.veritas.alterego.util.ModuleResponse;
import org.telegram.telegrambots.api.objects.Update;

public interface BotModule {

    ModuleResponse accept(Update update);

}
