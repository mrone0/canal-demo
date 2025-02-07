package com.mrone.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @program: studyweb
 * @description:
 * @author: Mr.One
 * @create: 2022-09-10 15:43
 **/

@Data
public class CanalMessage<T> {

    /**
     * 更新后的数据
     */
    private List<T> data;
    /**
     * 数据库名
     */
    private String database;
    /**
     * binlog executeTime, 执行耗时
     */
    private long es;
    /**
     * id
     */
    private int id;
    /**
     * 标识是否是ddl语句，比如create table/drop table
     */
    private boolean isDdl;

    /**
     * 更新前的有变更的列的数据
     */
    private List<Map<String, Object>> old;
    /**
     * 主键字段名
     */
    private List<String> pkNames;
    /**
     * ddl/query的sql语句
     */
    private String sql;

    /**
     * 表名
     */
    private String table;
    /**
     * dml build timeStamp
     */
    private long ts;
    /**
     * 事件类型:INSERT/UPDATE/DELETE
     */
    private String type;
}
