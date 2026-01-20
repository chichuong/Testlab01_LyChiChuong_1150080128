package com.example.baitaptuan4.organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {

    @Query("select count(o) > 0 from Organization o where lower(o.orgName) = lower(:name)")
    boolean existsByOrgNameIgnoreCase(@Param("name") String name);
}
