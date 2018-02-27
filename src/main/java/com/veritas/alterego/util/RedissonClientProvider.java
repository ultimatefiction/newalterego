package com.veritas.alterego.util;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissonClientProvider {

    private Config config;

    private int dataBase;
    private String address;
    private String password;

    public RedissonClientProvider() {
        this.dataBase = 0;
        this.address = "127.0.0.1:6379";
        this.password = "msxfpwd";
        init();
    }

    public void init() {
        config = new Config();
        config.useSingleServer()
                .setAddress(address)
                .setPassword(password)
                .setDatabase(dataBase);
    }

    public RedissonClient getClient() {
        return Redisson.create(config);
    }

    public int getDataBase() {
        return dataBase;
    }

    public RedissonClientProvider setDataBase(int dataBase) {
        this.dataBase = dataBase;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public RedissonClientProvider setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RedissonClientProvider setPassword(String password) {
        this.password = password;
        return this;
    }

}
