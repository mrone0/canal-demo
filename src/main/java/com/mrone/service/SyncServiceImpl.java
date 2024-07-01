package com.mrone.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.reflect.TypeToken;
import com.mrone.entity.CanalMessage;
import com.mrone.entity.User;
import com.mrone.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mr.One
 * @program canal-demo
 * @description
 * @create 2024-06-20 21:43
 **/
@Service
public class SyncServiceImpl<T> implements SyncService<T> {

    @Autowired
    private EsService esService;

    @Override
    public void syncDb2Es(String jsonBody, CanalMessage<T> canalMessage) {
        if ("canal".equals(canalMessage.getDatabase()) && "user".equals(canalMessage.getTable())) {
            String data = JSON.parseObject(jsonBody).getString("data");
            List<User> userList = GsonUtil.gson.fromJson(data, new TypeToken<List<User>>() {
            }.getType());
            userList.forEach(user -> {
                switch (canalMessage.getType()) {
                    case "UPDATE" -> {
                        esService.update(user);
                    }
                    case "INSERT" -> {
                        esService.insert(user);
                    }
                    case "DELETE" -> {
                        esService.delete(user);
                    }
                }
            });
        }
    }
}
