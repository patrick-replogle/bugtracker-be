package com.patrickreplogle.bugtracker.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project extends Auditable {
    // === fields ====
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long projectid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = true)
    private String repositoryurl;

    @Column(nullable = true)
    private String websiteurl;

    @Column(nullable = true)
    private String imageurl;

    @ManyToMany(fetch = FetchType.LAZY,
        cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
        },
        mappedBy = "allProjects")
    @JsonIgnoreProperties(value = {"allProjects", "ownedProjects", "roles", "ownedTickets", "assignedTickets", "hibernateLazyInitializer", "handler" }, allowSetters = true)
    private Set<User> users = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    @JsonIgnoreProperties(value = {"allProjects", "roles", "ownedTickets", "assignedTickets", "ownedProjects", "hibernateLazyInitializer", "handler"}, allowSetters = true)
    private User projectOwner;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = {"assignedUser", "project", "roles", "allProjects", "ownedProjects", "hibernateLazyInitializer", "handler"}, allowSetters = true)
    private List<Ticket> tickets = new ArrayList<>();

    // === constructors ====

    public Project() {
    }

    public Project(String name, String description, String repositoryurl, String websiteurl, String imageurl, User projectOwner) {
        this.name = name;
        this.description = description;
        this.repositoryurl = repositoryurl;
        this.websiteurl = websiteurl;
        this.imageurl = imageurl;
        this.projectOwner = projectOwner;
    }

    // === getters / setters ===

    public long getProjectid() {
        return projectid;
    }

    public void setProjectid(long projectid) {
        this.projectid = projectid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRepositoryurl() {
        return repositoryurl;
    }

    public void setRepositoryurl(String repositoryurl) {
        this.repositoryurl = repositoryurl;
    }

    public String getWebsiteurl() {
        return websiteurl;
    }

    public void setWebsiteurl(String websiteurl) {
        this.websiteurl = websiteurl;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public User getProjectOwner() {
        return projectOwner;
    }

    public void setProjectOwner(User projectOwner) {
        this.projectOwner = projectOwner;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    // === override methods ====

    @Override
    public String toString() {
        return "Project {" +
                "projectid=" + projectid +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", repositoryurl='" + repositoryurl + '\'' +
                ", websiteurl='" + websiteurl + '\'' +
                '}';
    }
}
