package com.example.baitaptuan4.organization;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrganizationServiceTest {

    @Test
    void create_whenDuplicate_throwException() {
        OrganizationRepository repo = mock(OrganizationRepository.class);
        when(repo.existsByOrgNameIgnoreCase("abc")).thenReturn(true);

        OrganizationService service = new OrganizationService(repo);

        OrganizationForm f = new OrganizationForm();
        f.setOrgName("abc");

        OrgNameAlreadyExistsException ex = assertThrows(OrgNameAlreadyExistsException.class, () -> service.create(f));

        assertEquals("Organization Name already exists", ex.getMessage());
        verify(repo, never()).save(any());
    }

    @Test
    void create_whenValid_saveSuccessfully() {
        OrganizationRepository repo = mock(OrganizationRepository.class);
        when(repo.existsByOrgNameIgnoreCase("abc")).thenReturn(false);

        Organization saved = new Organization();
        saved.setOrgId(1);
        saved.setOrgName("abc");

        when(repo.save(any())).thenReturn(saved);

        OrganizationService service = new OrganizationService(repo);

        OrganizationForm f = new OrganizationForm();
        f.setOrgName(" abc ");

        Organization out = service.create(f);

        assertEquals(1, out.getOrgId());
        assertEquals("abc", out.getOrgName());
        verify(repo).save(any());
    }
}
