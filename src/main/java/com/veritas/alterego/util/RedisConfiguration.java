package com.veritas.alterego.util;

public class RedisConfiguration {

    public static final String GROUPS_SET = "com.veritas.alterego:groups";
    public static final String USERNAME_MAP = "com.veritas.alterego:users:usernames";
    public static final String FIRST_NAME_MAP = "com.veritas.alterego:users:first_names";

    public static final String PIDOR_LAST_FIRED_MAP = "com.veritas.alterego:modules:last_fired";

    private static final String GROUP_MEMBER_SET = "com.veritas.alterego:groups:%s:members";
    private static final String GROUP_PIDOR_TOP_SSET = "com.veritas.alterego:groups:%s:pidor_top";
    private static final String GROUP_PIDOR_LAST_LIST = "com.veritas.alterego:groups:%s:pidor_last";

    public static String getMemberSet(long chatId) {
        return String.format(GROUP_MEMBER_SET, chatId);
    }

    public static String getPidorTop(long chatId) {
        return String.format(GROUP_PIDOR_TOP_SSET, chatId);
    }

    public static String getPidorList(long chatId) {
        return String.format(GROUP_PIDOR_LAST_LIST, chatId);
    }

}
