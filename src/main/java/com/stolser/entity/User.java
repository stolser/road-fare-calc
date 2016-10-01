package com.stolser.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.concurrent.atomic.AtomicLong;

@Document(collection = "users")
public class User {
    private static final AtomicLong nextId = new AtomicLong(1);
    @Id private String id;
    @Indexed(unique = true, name = "systemId", direction = IndexDirection.ASCENDING)
    private long systemId;
    private String firstName;
    private String lastName;
    private String email;
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

    public String toFullString() {
        return String.format("User{_id: %s, systemId: %d, firstName: %s, lastName: %s," +
                "email: %s)}", id, systemId, firstName, lastName, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return systemId == user.systemId;

    }

    @Override
    public int hashCode() {
        return (int) (systemId ^ (systemId >>> 32));
    }

    @Override
    public String toString() {

        return String.format("User{%s %s (id: %d; %s)}", lastName, firstName, systemId, email);
    }
}
