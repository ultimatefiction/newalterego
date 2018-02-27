package com.veritas.alterego.repositories;

import java.util.Collection;

public interface GeneralRepository {

    Collection<Long> getAllGroupIds();
    void addGroup(long id);

    String getGroupNameById(long id);
    void addGroupNameById(String groupName, long id);
    void removeGroupNameById(long id);

    String getFirstNameById(long id);
    void addFirstNameById(String firstName, long id);
    void removeFirstNameById(long id);

    String getUserNameById(long id);
    void addUserNameById(String userName, long id);
    boolean userNameExistsForId(long id);
    void removeUserNameById(long id);

    void addMemberToGroup(long groupId, long userId);
    Long getRandomMemberFromGroup(long groupId);
    void removeMemberFromGroup(long groupId, long userId);
    Collection<Long> getAllMembersFromGroup(long groupId);
    int getNumberOfMembersInGroup(long groupId);

}
