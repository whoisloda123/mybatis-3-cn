package com.liucan.loda;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * @author liucan
 * @version 2021/8/2
 */
public class MybatisTest {

    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void init() throws Exception {
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        InputStream inputStream = Resources.getResourceAsStream("MybatisConfig.xml");
        this.sqlSessionFactory = builder.build(inputStream);
    }

    @Test
    public void testFindByUserId() throws Exception {
        SqlSession sqlSession = this.sqlSessionFactory.openSession();
        ActorMapper mapper = sqlSession.getMapper(ActorMapper.class);
        Actor userById = mapper.findUserById(1);
        System.out.println(userById);
        userById = mapper.findUserById(1);
        List<Actor> users = mapper.selectList();
        System.out.println(users);
        sqlSession.close();
    }

    @Test
    public void testTwoLevelCache() throws Exception {
        SqlSession sqlSession1 = this.sqlSessionFactory.openSession();
        SqlSession sqlSession2 = this.sqlSessionFactory.openSession();
        ActorMapper mapper1 = sqlSession1.getMapper(ActorMapper.class);
        ActorMapper mapper2 = sqlSession2.getMapper(ActorMapper.class);

        Actor userById = mapper1.findUserById(1);
        System.out.println(userById);
        sqlSession1.close();

        userById = mapper2.findUserById(1);
        System.out.println(userById);
        sqlSession2.close();
    }
}
