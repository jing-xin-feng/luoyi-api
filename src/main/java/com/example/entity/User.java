package com.example.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}