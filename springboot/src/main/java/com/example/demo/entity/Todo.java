package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("todo")
@Data
public class Todo {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String task;

    private Boolean completed;

    private Integer userId; // 关联用户ID

    private transient User user; // 用于存储关联的用户信息
}