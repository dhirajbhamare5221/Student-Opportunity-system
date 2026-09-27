package com.platform.opportunity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for verifying system health and setup baseline.
 * Used during Phase 1 testing to confirm server, database, and API layers are working.
 */
@RestController
@RequestMapping("/api")
public class SystemCheckController {

    @Autowired
    private Environment environment;

    @Autowired(required = false)
    private DataSource dataSource;

    @Value("${spring.application.name:OpportunityHub}")
    private String appName;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", appName);
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("appName", appName);
        status.put("status", "ONLINE");
        status.put("version", "1.0.0 (Phase 1 Baseline)");
        status.put("javaVersion", System.getProperty("java.version"));
        status.put("serverTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        // Active Spring profile
        String[] activeProfiles = environment.getActiveProfiles();
        status.put("activeProfile", activeProfiles.length > 0 ? activeProfiles[0] : "default (H2 / local file)");

        // Database status check
        Map<String, Object> dbInfo = new HashMap<>();
        if (dataSource != null) {
            try (Connection conn = dataSource.getConnection()) {
                DatabaseMetaData meta = conn.getMetaData();
                dbInfo.put("connected", true);
                dbInfo.put("databaseProduct", meta.getDatabaseProductName());
                dbInfo.put("databaseVersion", meta.getDatabaseProductVersion());
                dbInfo.put("driverName", meta.getDriverName());
            } catch (Exception e) {
                dbInfo.put("connected", false);
                dbInfo.put("error", e.getMessage());
            }
        } else {
            dbInfo.put("connected", false);
            dbInfo.put("error", "No DataSource configured");
        }
        status.put("database", dbInfo);

        return ResponseEntity.ok(status);
    }
}
