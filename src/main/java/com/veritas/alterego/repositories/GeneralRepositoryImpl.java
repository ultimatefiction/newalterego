package com.veritas.alterego.repositories;

import com.veritas.alterego.util.RedissonClientProvider;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

import java.util.Collection;

public class GeneralRepositoryImpl implements GeneralRepository {

    private final String GROUPS_SET = "com.veritas.alterego:groups";
    private final String GROUP_NAME_MAP = "com.veritas.alterego:groups:group_names";
    private final String USERNAME_MAP = "com.veritas.alterego:users:usernames";
    private final String FIRST_NAME_MAP = "com.veritas.alterego:users:first_names";

    private final String MEMBERS_SET = "com.veritas.alterego:groups:%s:members";

    private RedissonClient client;
    private RSet<Long> groupSet;
    private RMap<Long, String> usernameMap;
    private RMap<Long, String> firstNameMap;
    private RMap<Long, String> groupNameMap;

    public GeneralRepositoryImpl() {
        client = new RedissonClientProvider().getClient();
        groupSet = client.getSet(GROUPS_SET);
        usernameMap = client.getMap(USERNAME_MAP);
        firstNameMap = client.getMap(FIRST_NAME_MAP);
        groupNameMap = client.getMap(GROUP_NAME_MAP);
    }

    @Override
    public Collection<Long> getAllGroupIds() {
        return groupSet.readAll();
    }

    @Override
    public void addGroup(long id) {
        groupSet.add(id);
    }

    @Override
    public String getGroupNameById(long id) {
        return groupNameMap.get(id);
    }

    @Override
    public void addGroupNameById(String groupName, long id) {
        groupNameMap.put(id, groupName);
    }

    @Override
    public void removeGroupNameById(long id) {
        groupNameMap.remove(id);
    }

    @Override
    public String getFirstNameById(long id) {
        return firstNameMap.get(id);
    }

    @Override
    public void addFirstNameById(String firstName, long id) {
        firstNameMap.put(id, firstName);
    }

    @Override
    public void removeFirstNameById(long id) {
        firstNameMap.remove(id);
    }

    @Override
    public String getUserNameById(long id) {
        return usernameMap.get(id);
    }

    @Override
    public void addUserNameById(String userName, long id) {
        usernameMap.put(id, userName);
    }

    @Override
    public boolean userNameExistsForId(long id) {
        return usernameMap.containsKey(id);
    }

    @Override
    public void removeUserNameById(long id) {
        usernameMap.remove(id);
    }

    @Override
    public void addMemberToGroup(long groupId, long userId) {
        RSet<Long> memberSet = client.getSet(String.format(MEMBERS_SET, groupId));
        memberSet.add(userId);
    }

    @Override
    public Long getRandomMemberFromGroup(long groupId) {
        RSet<Long> memberSet = client.getSet(String.format(MEMBERS_SET, groupId));
        return memberSet.random();
    }

    @Override
    public void removeMemberFromGroup(long groupId, long userId) {
        RSet<Long> memberSet = client.getSet(String.format(MEMBERS_SET, groupId));
        memberSet.remove(userId);
    }

    @Override
    public Collection<Long> getAllMembersFromGroup(long groupId) {
        RSet<Long> memberSet = client.getSet(String.format(MEMBERS_SET, groupId));
        return memberSet.readAll();
    }

    @Override
    public int getNumberOfMembersInGroup(long groupId) {
        RSet<Long> memberSet = client.getSet(String.format(MEMBERS_SET, groupId));
        return memberSet.size();
    }

}
