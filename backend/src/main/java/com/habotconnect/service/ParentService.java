package com.habotconnect.service;

import com.habotconnect.dto.ParentDTO;
import com.habotconnect.entity.Parent;
import com.habotconnect.repository.ParentRepository;
import com.habotconnect.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ParentService {

    private final ParentRepository parents;
    private final UserRepository users;
    private final AuditLogService audit;

    public ParentService(ParentRepository parents, UserRepository users, AuditLogService audit) {
        this.parents = parents;
        this.users = users;
        this.audit = audit;
    }

    public List<ParentDTO> list() {
        return parents.findAll().stream().map(ParentDTO::from).toList();
    }

    public ParentDTO create(ParentDTO dto) {
        Parent x = new Parent();
        x.user = users.findById(dto.userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + dto.userId));
        x.phone = dto.phone;
        x.address = dto.address;

        Parent saved = parents.save(x);
        audit.record("CREATE_PARENT", "Parent", saved.id);
        return ParentDTO.from(saved);
    }
}
