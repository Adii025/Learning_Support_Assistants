package com.habotconnect.service;

import com.habotconnect.dto.UserResponse;
import com.habotconnect.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository users;
    private final ParentRepository parents;
    private final StudentRepository students;
    private final LsaProfileRepository lsas;
    private final MatchingRequestRepository requests;
    private final MatchRepository matches;
    private final SessionRepository sessions;

    public UserService(UserRepository users, ParentRepository parents, StudentRepository students,
                        LsaProfileRepository lsas, MatchingRequestRepository requests,
                        MatchRepository matches, SessionRepository sessions) {
        this.users = users;
        this.parents = parents;
        this.students = students;
        this.lsas = lsas;
        this.requests = requests;
        this.matches = matches;
        this.sessions = sessions;
    }

    public List<UserResponse> list() {
        return users.findAll().stream().map(UserResponse::from).toList();
    }

    public Map<String, Long> dashboard() {
        return Map.of(
                "users", users.count(),
                "parents", parents.count(),
                "students", students.count(),
                "lsa", lsas.count(),
                "requests", requests.count(),
                "matches", matches.count(),
                "sessions", sessions.count()
        );
    }
}
