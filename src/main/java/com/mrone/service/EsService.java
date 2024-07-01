package com.mrone.service;

import com.mrone.entity.User;

import java.util.List;

/**
 * @author Mr.One
 * @program canal-demo
 * @description
 * @create 2024-06-20 20:44
 **/
public interface EsService {
     void insert(User user);

     void initIndex(List<User> userList);

     void update(User user);

    void delete(User user);

}
