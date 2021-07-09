package com.patrickreplogle.bugtracker.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "userroles")
@IdClass(UserRolesId.class)
public class UserRoles extends Auditable implements Serializable {
    // === fields ===
    @Id
    @ManyToOne
    @JoinColumn(name = "userid")
    @JsonIgnoreProperties(value = "roles",
            allowSetters = true)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "roleid")
    @JsonIgnoreProperties(value = "users",
            allowSetters = true)
    private Role role;

    // === constructors ===
    public UserRoles() {
    }

    public UserRoles(User user, Role role) {
        this.user = user;
        this.role = role;
    }

    // === getters/setters ===
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // === override methods ===
    @Override
    public int hashCode() {
        return 33;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof UserRoles)) {
            return false;
        }
        UserRoles that = (UserRoles) o;

        return ((user == null) ? 0 : user.getUserid()) == ((that.user == null) ? 0 : that.user.getUserid()) &&
                ((role == null) ? 0 : role.getRoleid()) == ((that.role == null) ? 0 : that.role.getRoleid());
    }
}
