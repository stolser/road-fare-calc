package com.stolser.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.concurrent.atomic.AtomicLong;

@Document(collection = "users")
public class User {
    private static final AtomicLong nextId = new AtomicLong(1);
    @Id
    private String id;
    private long systemId;
    private String firstName;
    private String lastName;
    private String email;
    private double discount;
    @Version private Long version;

    public User(String firstName, String lastName, String email) {
        this.systemId = nextId.getAndIncrement();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public long getSystemId() {
        return systemId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String toFullString() {
        return String.format("User{_id: %s, systemId: %d, firstName: %s, lastName: %s," +
                "email: %s, discount: %.2f)}", id, systemId, firstName, lastName, email, discount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return id != null ? id.equals(user.id) : user.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("User{%s %s (systemId: %d)}", lastName, firstName, systemId);
    }
}
