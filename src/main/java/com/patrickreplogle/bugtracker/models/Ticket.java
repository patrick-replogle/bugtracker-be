package com.patrickreplogle.bugtracker.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tickets")
public class Ticket extends Auditable {

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
    private String priority;

    @ManyToOne(optional = false)
    @JoinColumn(name = "projectid")
    @JsonIgnoreProperties(value = {"tickets"}, allowSetters = true)
    private Project project;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    @JsonIgnoreProperties(value = {"allProjects", "ownedProjects", "roles", "ownedTickets", "projectOwner", "assignedTickets", "hibernateLazyInitializer", "handler" }, allowSetters = true)
    private User ticketOwner;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "assigneduserid")
    @JsonIgnoreProperties(value = {"allProjects", "ownedProjects", "roles", "ownedTickets", "assignedTickets", "hibernateLazyInitializer", "handler"}, allowSetters = true)
    private User assignedUser;

    public Ticket() {
    }

    public Ticket(@NotNull String title, @NotNull String description, String imageurl, Project project, User ticketOwner, User assignedUser, @NotNull String priority) {
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
