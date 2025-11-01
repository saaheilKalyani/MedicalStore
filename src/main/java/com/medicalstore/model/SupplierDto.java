package com.medicalstore.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SupplierDto {
    private Integer userId;

    @NotBlank(message = "Username required")
    @Size(min = 3, max = 100, message = "Username must be 3-100 characters")
    private String username;

    @NotBlank(message = "Password required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Email required")
    @Email(message = "Invalid email format")
    private String email;

    @Size(max = 255, message = "Address too long")
    private String address;

    // Constructors
    public SupplierDto() {}

    public SupplierDto(Integer userId, String username, String password, String email, String address) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
    }

    // getters & setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}