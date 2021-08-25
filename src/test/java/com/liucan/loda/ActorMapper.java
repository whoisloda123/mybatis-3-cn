package com.liucan.loda;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author liucan
 * @version 2021/8/2
 */
public interface ActorMapper {

  Actor findUserById(Integer id);

  List<Actor> selectListUsers();

  Actor findUserByFirstname(String name);

  @Select("select * from actor")
  @ResultMap("actorResultMap")
  List<Actor> selectList();

  void insertUseGeneratedKeys(@Param("actor") Actor actor);

  void insertUseSelectKeys(@Param("actor") Actor actor);

  List<FilmActorMap> selectListByActorId(Integer actorId);
}
