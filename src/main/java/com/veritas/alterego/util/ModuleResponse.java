package com.veritas.alterego.util;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

public class ModuleResponse {

    private SendMessage message;

    public ModuleResponse() {
    }

    public boolean hasMessage() {
        return (message != null);
    }

    public SendMessage getMessage() {
        return message;
    }

    public ModuleResponse setReplyMessage(String text, Update update) {
        message = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setReplyToMessageId(update.getMessage().getMessageId())
                .setText(text);
        return this;
    }

}
