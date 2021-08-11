package com.liucan.loda;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author liucan
 * @version 2021/8/2
 */
public interface ActorMapper {

    Actor findUserById(Integer id) throws Exception;

    Actor findUserByFirstname(String name) throws Exception;

    @Select("select * from actor")
    @ResultMap("actorResultMap")
    List<Actor> selectList();
}
