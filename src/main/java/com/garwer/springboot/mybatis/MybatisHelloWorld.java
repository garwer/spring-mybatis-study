package com.garwer.springboot.mybatis;

import com.garwer.springboot.mybatis.mapper.UserMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Garwer
 * @Date: 19/3/13 上午12:39
 * @Version 1.0
 * getMapper实现属于动态代理方式
 */
public class MybatisHelloWorld {
    public static void main(String[] args) throws IOException {
        String resource = "mybatis/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
              //方式1
        //    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
   //         List<HashMap<String, Object>> userList = userMapper.getAll();
//            for (HashMap<String, Object> map : userList) {
//                for(Map.Entry<String, Object> entry : map.entrySet()) {
//                    System.out.println(entry.getKey());
//                    System.out.println(entry.getValue());
//                }
//            }

            //方式2

            Map<Object, Object> map2 = sqlSession.selectMap("com.garwer.springboot.mybatis.mapper.UserMapper.getAll","");
           System.out.println(map2);
        } finally {
            sqlSession.close();
        }
    }

}
