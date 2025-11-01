package com.medicalstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/test-db")
    @ResponseBody
    public String testDbConnection() {
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
            return "✅ Connected to PostgreSQL! Users in DB: " + count;
        } catch (Exception e) {
            return "❌ Database connection failed: " + e.getMessage();
        }
    }
}