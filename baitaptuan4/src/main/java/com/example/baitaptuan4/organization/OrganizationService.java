package com.example.baitaptuan4.organization;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationService {

    private final OrganizationRepository repo;

    public OrganizationService(OrganizationRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Organization create(OrganizationForm form) {
        String name = trimToNull(form.getOrgName());
        if (repo.existsByOrgNameIgnoreCase(name)) {
            throw new OrgNameAlreadyExistsException();
        }

        Organization o = new Organization();
        o.setOrgName(name);
        o.setAddress(trimToNull(form.getAddress()));
        o.setPhone(trimToNull(form.getPhone()));
        o.setEmail(trimToNull(form.getEmail()));
        return repo.save(o);
    }

    private String trimToNull(String s) {
        if (s == null)
            return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
