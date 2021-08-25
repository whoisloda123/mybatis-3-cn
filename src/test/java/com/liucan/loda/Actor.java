package com.liucan.loda;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户信息
 * @author liucan
 */
@Data
public class Actor implements Serializable {
  /**
   * 用户id
   */
  private Integer actorId;
  /**
   * 用户名
   */
  private String firstName;

  private String lastName;

  /**
   * 对应的 film id 列表
   */
  private List<FilmActorMap> filmIds;

  private LocalDateTime lastUpdate;
}
