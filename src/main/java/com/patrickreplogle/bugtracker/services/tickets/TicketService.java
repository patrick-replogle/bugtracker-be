package com.patrickreplogle.bugtracker.services.tickets;

import com.patrickreplogle.bugtracker.models.Ticket;

import java.util.List;

public interface TicketService {
    Ticket findTicketById(long id);

    Ticket save(Ticket ticket);

    void delete(long id);

    Ticket update(Ticket ticket, long id);
}
