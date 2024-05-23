package com.ecommerce.library.dto;

import com.ecommerce.library.model.City;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    @Size(min = 3, max = 10, message = "First name must have 3-10 characters")
    private String firstName;

    @Size(min = 3, max = 10, message = "Last name must have 3-10 characters")
    private String lastName;
    private String username;
    @Size(min = 3, max = 15, message = "Password must have 3-10 characters")
    private String password;

    @Size(min = 10, max = 15, message = "Phone number must have 10-15 characters")
    private String phoneNumber;

    private String address;
    private String confirmPassword;
    private City city;

    private String country;

}