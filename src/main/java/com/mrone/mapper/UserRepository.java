package com.mrone.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mrone.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Mr.One
 * @program canal-demo
 * @description
 * @create 2024-06-20 20:44
 **/
@Mapper
public interface UserRepository extends BaseMapper<User> {

}
