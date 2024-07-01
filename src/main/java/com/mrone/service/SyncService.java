package com.mrone.service;

import com.mrone.entity.CanalMessage;

/**
 * @author Mr.One
 * @program canal-demo
 * @description
 * @create 2024-06-20 21:43
 **/
public interface SyncService<T> {
    void syncDb2Es(String jsonBody,CanalMessage<T> canalMessage);
}
