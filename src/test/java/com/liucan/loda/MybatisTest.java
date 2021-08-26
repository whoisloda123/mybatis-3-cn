package com.liucan.loda;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.time.LocalDateTime;
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
    Actor penelope = mapper.findUserByIdAndName(1, "PENELOPE");
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

  @Test
  public void testInsertUseGeneratedKeys() {
    SqlSession sqlSession = this.sqlSessionFactory.openSession();
    ActorMapper mapper = sqlSession.getMapper(ActorMapper.class);
    Actor actor = new Actor();
    actor.setFirstName("LIU");
    actor.setLastName("CAN");
    actor.setLastUpdate(LocalDateTime.now());
    mapper.insertUseGeneratedKeys(actor);
    sqlSession.close();
    System.out.println(actor);
  }

  @Test
  public void testInsertUseSelectKeys() {
    SqlSession sqlSession = this.sqlSessionFactory.openSession();
    ActorMapper mapper = sqlSession.getMapper(ActorMapper.class);
    Actor actor = new Actor();
    actor.setFirstName("LIU");
    actor.setLastName("CAN");
    actor.setLastUpdate(LocalDateTime.now());
    mapper.insertUseSelectKeys(actor);
    System.out.println(actor);
    sqlSession.close();
  }

  @Test
  public void testLayzLoad() {
    SqlSession sqlSession = this.sqlSessionFactory.openSession();
    ActorMapper mapper = sqlSession.getMapper(ActorMapper.class);
    List<Actor> actors = mapper.selectListUsers();
    Actor actor1 = actors.get(0);
    System.out.println(actor1.getFilmIds());
    Integer actorId = actor1.getActorId();
    System.out.println(actor1.getFilmIds());
    sqlSession.close();
  }
}
