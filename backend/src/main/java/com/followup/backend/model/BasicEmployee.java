package com.followup.backend.model;

import jakarta.persistence.*;
import lombok.ToString;

@Entity
@DiscriminatorValue("BASIC_EMPLOYEE")
@ToString(callSuper = true)

public class BasicEmployee extends User {
    @Override
    public String getRole() {
        return "BASIC_EMPLOYEE";
    }
}

// The BasicEmployee class extends the User class and represents a basic employee user in the application.
// It inherits the properties of the User class and specifies its role as "BASIC_EMPLOYEE" using the @DiscriminatorValue annotation.
// The getRole() method is overridden to return the string "BASIC_EMPLOYEE", indicating the role of this user type.