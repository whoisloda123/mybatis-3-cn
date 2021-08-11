package com.liucan.loda;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户信息
 *
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

    private LocalDateTime lastUpdate;
}
