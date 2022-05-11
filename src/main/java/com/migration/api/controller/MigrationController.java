package com.migration.api.controller;

import com.migration.service.MigrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller for migration
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class MigrationController {
    private final MigrationService migrationService;

    @PostMapping("api/migration/all")
    public ResponseEntity<Void> migrateAll() {
        migrationService.migrateAll();
        return ResponseEntity.noContent().build();
    }

}
