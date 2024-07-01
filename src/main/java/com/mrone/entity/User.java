package com.mrone.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Mr.One
 * @program canal-demo
 * @description
 * @create 2024-06-20 20:49
 **/
@Data
@Getter
@Setter
public class User {
    private int id;
    private String name;
    private int age;
    private String phone;
}
