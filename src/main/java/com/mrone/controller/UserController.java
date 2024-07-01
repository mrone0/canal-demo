package com.mrone.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mrone.entity.User;
import com.mrone.mapper.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mr.One
 * @program canal-demo
 * @description
 * @create 2024-06-20 22:02
 **/
@RestController
public class UserController {
    @Autowired
    private UserRepository repository;
    @PostMapping("/insert")
    public void insert(@RequestBody User user){
        repository.insert(user);
    }

    @PostMapping("/update")
    public void update(@RequestBody User user){
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.set("age",user.getAge()).set("name",user.getAge()).set("phone",user.getPhone()).eq("id",user.getId());
        repository.update(user,wrapper);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody User user){
        repository.delete(new QueryWrapper<User>().eq("id",user.getId()));
    }
}
