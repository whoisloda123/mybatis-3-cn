package com.liucan.loda;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 如果入参只有一个且是基础类型则不需要用 {@code Param} 注解，如果是入参只有一个但是pojo对象，则需要，
 * 如果入参是多个不管是否是基础则也需要
 * @author liucan
 * @version 2021/8/2
 */
public interface ActorMapper {

  Actor findUserById(Integer id);

  Actor findUserByIdAndName(@Param("id") Integer id, @Param("name") String name);

  List<Actor> selectListUsers();

  Actor findUserByFirstname(String name);

  @Select("select * from actor")
  @ResultMap("actorResultMap")
  List<Actor> selectList();

  void insertUseGeneratedKeys(@Param("actor") Actor actor);

  void insertUseSelectKeys(@Param("actor") Actor actor);

  List<FilmActorMap> selectListByActorId(Integer actorId);
}
