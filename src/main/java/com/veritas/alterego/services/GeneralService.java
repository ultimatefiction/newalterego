package com.veritas.alterego.services;

import org.telegram.telegrambots.api.objects.Update;

public interface GeneralService {

    void basicUpdate(Update update);

    int getMembersCount(long groupId);

    String getRef(long userId, boolean tagged);

}
