package com.habotconnect.service;

import com.habotconnect.entity.AuditLog;
import com.habotconnect.repository.AuditLogRepository;
import com.habotconnect.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Central place for writing to audit_logs. Every mutating action in the
 * other services calls record(...) so there's an actual trail behind
 * the "Audit Logs" screen in the admin UI (previously nothing ever
 * wrote to this table).
 */
@Service
public class AuditLogService {

    private final AuditLogRepository logs;
    private final UserRepository users;

    public AuditLogService(AuditLogRepository logs, UserRepository users) {
        this.logs = logs;
        this.users = users;
    }

    public void record(String action, String entityName, Long entityId) {
        AuditLog log = new AuditLog();
        log.userId = currentUserId();
        log.action = action;
        log.entityName = entityName;
        log.entityId = entityId;
        logs.save(log);
    }

    public List<AuditLog> list() {
        return logs.findAll();
    }

    /**
     * The JWT filter authenticates requests using the user's email as the
     * principal name. Resolve that back to a userId for the log row.
     * Returns null for system/unauthenticated actions (e.g. registration,
     * before a user technically "exists" as an authenticated principal).
     */
    private Long currentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null) {
            return null;
        }

        return users.findByEmailIgnoreCase(auth.getName())
                .map(u -> u.id)
                .orElse(null);
    }
}
