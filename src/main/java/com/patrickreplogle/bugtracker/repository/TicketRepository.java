package com.patrickreplogle.bugtracker.repository;

import com.patrickreplogle.bugtracker.models.Ticket;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TicketRepository  extends CrudRepository<Ticket, Long> {
    @Query(value = "SELECT * FROM tickets WHERE assigneduserid = :userid AND projectid = :projectid", nativeQuery = true)
    List<Ticket> findAssignedTickets(long userid, long projectid);

    @Query(value = "SELECT * FROM tickets WHERE projectid = :projectid AND imageurl IS NOT NULL", nativeQuery = true)
    List<Ticket> findProjectTicketsWithImage(long projectid);

    @Transactional
    @Modifying
    @Query(value = "UPDATE tickets SET assigneduserid = null WHERE ticketid = :ticketid",
            nativeQuery = true)
    void unassignUserFromTicket(long ticketid);
}
