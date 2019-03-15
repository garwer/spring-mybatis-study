package com.garwer.springboot.mybatis.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Garwer
 * @Date: 19/3/13 下午10:04
 * @Version 1.0
 */
public interface UserMapper {
    List<HashMap<String,Object>> getAll();
}
