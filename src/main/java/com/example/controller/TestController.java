package com.example.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")  //
public class TestController {

    private final JdbcTemplate jdbcTemplate;

    // 构造器注入JdbcTemplate（正确的用法）
    public TestController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/db")
    public String testDatabaseConnection() {
        try {
            // 用法1：执行简单查询，返回Map结果
            Map<String, Object> result = jdbcTemplate.queryForMap("SELECT 1 as test_value");
            return "数据库连接成功: " + result.get("test_value");
        } catch (Exception e) {
            return "数据库连接失败: " + e.getMessage();
        }
    }

    // 用法2：查询用户数量
    @GetMapping("/users/count")
    public String getUserCount() {
        try {
            // 用法2：查询单个值
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM sys_user", Integer.class);
            return "用户数量: " + count;
        } catch (Exception e) {
            return "查询用户数量失败: " + e.getMessage();
        }
    }

    // 用法3：查询用户列表
    @GetMapping("/users/list")
    public String getUserList() {
        try {
            // 用法3：查询多条记录
            String userList = jdbcTemplate.queryForObject(
                    "SELECT GROUP_CONCAT(username) FROM sys_user", String.class);
            return "用户列表: " + userList;
        } catch (Exception e) {
            return "查询用户列表失败: " + e.getMessage();
        }
    }
}