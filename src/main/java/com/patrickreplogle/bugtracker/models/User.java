package com.patrickreplogle.bugtracker.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends Auditable {
    // === fields ===
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userid;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = true)
    private String company;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = {"user"}, allowSetters = true)
    private Set<UserRoles> roles = new HashSet<>();

    // a user can have many projects and a project can have many users
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "userprojects",
            joinColumns = { @JoinColumn(name = "userid")},
            inverseJoinColumns = { @JoinColumn(name = "projectid")})
    @JsonIgnoreProperties(value = {"users", "tickets", "projectOwner", "assignedUser"}, allowSetters = true)
    private Set<Project> allProjects = new HashSet<>();

    @OneToMany(mappedBy = "projectOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = {"users", "tickets", "assignedUser", "projectOwner"}, allowSetters = true)
    private Set<Project> ownedProjects = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "ticketOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = {"project", "assignedUser", "ticketOwner"}, allowSetters = true)
    private List<Ticket> ownedTickets = new ArrayList<>();

    @OneToMany(mappedBy = "assignedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = {  "tickets", "comments", "assignedUser" }, allowSetters = true)
    private Set<Ticket> assignedTickets = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "commentOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = { "commentOwner", "ticket"}, allowSetters = true)
    private List<Comment> comments = new ArrayList<>();

    // === constructors ===
    public User() {
    }

    public User(String username, String password, String email, String firstname, String lastname, String company) {
        this.username = username;
        setPassword(password);
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.company = company;
    }

    // === getters/setters ===
    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public void setPasswordNoEncrypt(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Set<UserRoles> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRoles> roles) {
        this.roles = roles;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Set<Project> getAllProjects() {
        return allProjects;
    }

    public void setAllProjects(Set<Project> allProjects) {
        this.allProjects = allProjects;
    }

    public Set<Project> getOwnedProjects() {
        return ownedProjects;
    }

    public void setOwnedProjects(Set<Project> ownedProjects) {
        this.ownedProjects = ownedProjects;
    }

    public List<Ticket> getOwnedTickets() {
        return ownedTickets;
    }

    public void setOwnedTickets(List<Ticket> ownedTickets) {
        this.ownedTickets = ownedTickets;
    }

    public Set<Ticket> getAssignedTickets() {
        return assignedTickets;
    }

    public void setAssignedTickets(Set<Ticket> assignedTickets) {
        this.assignedTickets = assignedTickets;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    // === override methods ===
    @Override
    public String toString() {
        return "User{" +
                "userid=" + userid +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", company='" + company + '\'' +
                '}';
    }

    @JsonIgnore
    public List<SimpleGrantedAuthority> getAuthority() {
        List<SimpleGrantedAuthority> rtnList = new ArrayList<>();

        for (UserRoles role : this.roles) {
            String myRole = "ROLE_" + role.getRole().getName().toUpperCase();
            rtnList.add(new SimpleGrantedAuthority(myRole));
        }

        return rtnList;
    }
}
