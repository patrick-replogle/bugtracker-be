package com.patrickreplogle.bugtracker.controllers;

import com.patrickreplogle.bugtracker.exceptions.ResourceFoundException;
import com.patrickreplogle.bugtracker.models.Ticket;
import com.patrickreplogle.bugtracker.services.tickets.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    TicketService ticketService;

    // get ticket by id
    @GetMapping(value = "/ticket/{ticketId}",
            produces = "application/json")
    public ResponseEntity<?> getTicketById(@PathVariable Long ticketId) {
        Ticket ticket = ticketService.findTicketById(ticketId);

        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    // create a new ticket
    @PostMapping(value = "/ticket", consumes = "application/json")
    public ResponseEntity<?> addNewTicket(
            @Valid
            @RequestBody
                    Ticket ticket)
            throws ResourceFoundException {

        return new ResponseEntity<>(ticketService.save(ticket), HttpStatus.CREATED);
    }

    // partially update a ticket record
    @PatchMapping(value = "/ticket/{id}",
            consumes = "application/json")
    public ResponseEntity<?> updateTicket(
            @RequestBody
                    Ticket ticket,
            @PathVariable
                    long id) {
       ;
        return new ResponseEntity<>(ticketService.update(ticket, id), HttpStatus.OK);
    }

    // delete a ticket record
    @DeleteMapping(value = "/ticket/{id}")
    public ResponseEntity<?> deleteTicketById(
            @PathVariable
                    long id) {
        ticketService.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
