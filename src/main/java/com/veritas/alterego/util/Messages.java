package com.veritas.alterego.util;

import org.telegram.telegrambots.api.objects.Message;

public class Messages {

    public static String getMessageType(Message message) {

        if (message.hasText()) {
            return message.getText();
        }
        if (message.hasDocument()) {
            return "<document: '" +
                    message.getDocument().getFileName() +
                    "'>";
        }
        if (message.hasPhoto()) {
            return "<photo: '" +
                    message.getCaption() +
                    "'>";
        }
        if (message.getVoice() != null) {
            return "<voice message>";
        }
        if (message.getAudio() != null) {
            return "<audio: '" +
                    message.getAudio().getPerformer() +
                    " - " +
                    message.getAudio().getTitle() +
                    "'>";
        }
        if (message.getSticker() != null) {
            return "<sticker: '" +
                    message.getSticker().getEmoji() +
                    "'>";
        }
        return "<unknown attachment type>";
    }

}
