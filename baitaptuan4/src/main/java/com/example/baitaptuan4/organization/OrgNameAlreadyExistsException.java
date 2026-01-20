package com.example.baitaptuan4.organization;

public class OrgNameAlreadyExistsException extends RuntimeException {
    public OrgNameAlreadyExistsException() {
        super("Organization Name already exists");
    }
}
