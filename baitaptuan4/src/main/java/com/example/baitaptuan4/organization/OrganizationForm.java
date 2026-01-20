package com.example.baitaptuan4.organization;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class OrganizationForm {

    @NotBlank(message = "Organization Name is required")
    @Length(min = 3, max = 255, message = "Organization Name length must be 3–255")
    private String orgName;

    @Length(max = 255, message = "Address max length is 255")
    private String address;

    @Pattern(regexp = "^$|^[0-9]{9,12}$", message = "Phone must be digits only and length 9–12")
    private String phone;

    @Email(message = "Email is invalid")
    private String email;
}
