package com.patrickreplogle.bugtracker.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment extends Auditable {

    // === fields ===
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long commentid;

    @Column(nullable = false)
    private String comment;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    @JsonIgnoreProperties(value = {"allProjects", "ownedProjects", "roles", "ownedTickets", "projectOwner", "assignedTickets", "hibernateLazyInitializer", "handler" }, allowSetters = true)
    private User commentOwner;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ticketid")
    @JsonIgnoreProperties(value = {"ticketOwner", "project", "assignedUser", "comments" }, allowSetters = true)
    private Ticket ticket;

    // === constructors ===

    public Comment() {
    }

    public Comment(String comment, User commentOwner, Ticket ticket) {
        this.comment = comment;
        this.commentOwner = commentOwner;
        this.ticket = ticket;
    }

    // === getters / setters ===

    public long getCommentid() {
        return commentid;
    }

    public void setCommentid(long commentid) {
        this.commentid = commentid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getCommentOwner() {
        return commentOwner;
    }

    public void setCommentOwner(User commentOwner) {
        this.commentOwner = commentOwner;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
