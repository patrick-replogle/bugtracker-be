package com.patrickreplogle.bugtracker.repository;

import com.patrickreplogle.bugtracker.models.Ticket;
import org.springframework.data.repository.CrudRepository;

public interface TicketRepository  extends CrudRepository<Ticket, Long> {
}
