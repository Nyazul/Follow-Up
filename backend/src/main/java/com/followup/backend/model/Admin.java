package com.followup.backend.model;

import jakarta.persistence.*;
import lombok.ToString;

@Entity
@DiscriminatorValue("ADMIN") 
@ToString(callSuper = true)
public class Admin extends User {


    @Column(name = "is_super", nullable = true)
    private boolean isSuper= false;

    @Override
    public String getRole() {
        return "ADMIN";
    }
}

// The Admin class extends the User class and represents an admin user in the application.
// It inherits the properties of the User class and specifies its role as "ADMIN" using the @DiscriminatorValue annotation.
// The getRole() method is overridden to return the string "ADMIN", indicating the role of this user type.