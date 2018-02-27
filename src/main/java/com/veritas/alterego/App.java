package com.veritas.alterego;

import com.veritas.alterego.bots.AlterEgoBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

public class App {

    public static void main(String[] args) {

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new AlterEgoBot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

    }

}
