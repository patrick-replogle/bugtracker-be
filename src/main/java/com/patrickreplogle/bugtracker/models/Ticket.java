package com.patrickreplogle.bugtracker.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "tickets")
public class Ticket extends Auditable {

    enum PriorityLevel {
        LOW,
        MEDIUM,
        HIGH
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ticketid;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = true)
    private String imageurl;

    @Column(nullable = false)
    private boolean completed = false;

    @Column(nullable = false)
    private PriorityLevel priority;

    @ManyToOne(optional = false)
    @JoinColumn(name = "projectid")
    @JsonIgnoreProperties(value = {"tickets", "projectOwner" }, allowSetters = true)
    private Project project;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    @JsonIgnoreProperties(value = {"allProjects", "ownedProjects", "roles", "ownedTickets", "projectOwner", "assignedTickets", "hibernateLazyInitializer", "handler" }, allowSetters = true)
    private User ticketOwner;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "assigneduserid")
    @JsonIgnoreProperties(value = {"allProjects", "ownedProjects", "roles", "ownedTickets", "assignedTickets", "comments", "hibernateLazyInitializer", "handler"}, allowSetters = true)
    private User assignedUser;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = {"assignedUser", "project", "roles", "allProjects", "ownedProjects", "comments", "ticket", "hibernateLazyInitializer", "handler"}, allowSetters = true)
    private List<Comment> comments = new ArrayList<>();

    public Ticket() {
    }

    public Ticket(@NotNull String title, @NotNull String description, String imageurl, Project project, User ticketOwner, User assignedUser, @NotNull PriorityLevel priority) {
        this.title = title;
        this.description = description;
        this.imageurl = imageurl;
        this.project = project;
        this.ticketOwner = ticketOwner;
        this.assignedUser = assignedUser;
        this.priority = priority;
    }

    public long getTicketid() {
        return ticketid;
    }

    public void setTicketid(long ticketid) {
        this.ticketid = ticketid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getTicketOwner() {
        return ticketOwner;
    }

    public void setTicketOwner(User ticketOwner) {
        this.ticketOwner = ticketOwner;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    public PriorityLevel getPriority() {
        return priority;
    }

    public void setPriority(PriorityLevel priority) {
        this.priority = priority;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
