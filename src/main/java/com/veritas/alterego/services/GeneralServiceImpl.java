package com.veritas.alterego.services;

import com.veritas.alterego.repositories.GeneralRepository;
import org.telegram.telegrambots.api.objects.Update;

public class GeneralServiceImpl implements GeneralService {

    private GeneralRepository r;

    public GeneralServiceImpl(GeneralRepository repository) {
        this.r = repository;
    }

    @Override
    public void basicUpdate(Update update) {
        if (update.getMessage().getChatId() < 0) {
            r.addGroup(update.getMessage().getChat().getId());
            r.addMemberToGroup(update.getMessage().getChat().getId(), update.getMessage().getFrom().getId());
            r.addGroupNameById(update.getMessage().getChat().getTitle(), update.getMessage().getChat().getId());
            r.addFirstNameById(update.getMessage().getFrom().getFirstName(), update.getMessage().getFrom().getId());
            if (update.getMessage().getFrom().getUserName() != null) {
                r.addUserNameById(update.getMessage().getFrom().getUserName(), update.getMessage().getFrom().getId());
            }
        }
    }

    @Override
    public int getMembersCount(long groupId) {
        return r.getNumberOfMembersInGroup(groupId);
    }

    @Override
    public String getRef(long userId, boolean tagged) {
        StringBuilder sb = new StringBuilder().append(r.getFirstNameById(userId));
        if (r.userNameExistsForId(userId)) {
            sb.append(" (");
            if (tagged) {
                sb.append("@");
            }
            sb.append(r.getUserNameById(userId)).append(")");
        }
        return sb.toString();
    }
}
