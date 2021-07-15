package com.patrickreplogle.bugtracker.services.tickets;

import com.patrickreplogle.bugtracker.exceptions.AccessDeniedException;
import com.patrickreplogle.bugtracker.exceptions.ResourceNotFoundException;
import com.patrickreplogle.bugtracker.models.Ticket;
import com.patrickreplogle.bugtracker.models.User;
import com.patrickreplogle.bugtracker.repository.TicketRepository;
import com.patrickreplogle.bugtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service(value = "ticketService")
public class TicketServiceImpl implements TicketService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Ticket findTicketById(long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket with id " + id + " not found."));
    }

    @Override
    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public void delete(long id) {
        Ticket ticketToDelete = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket id " + id + " not found."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        if (!ticketToDelete.getProject().getUsers().contains(user)) {
            throw new AccessDeniedException("User " + authentication.getName() + " does not have permission to delete ticket id " + id);
        }

        ticketRepository.deleteById(id);
    }

    @Override
    public Ticket update(Ticket ticket, long id) {
        Ticket currentTicket = findTicketById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        if (!currentTicket.getProject().getUsers().contains(user)) {
            throw new AccessDeniedException("User " + authentication.getName() + " does not have permission to update ticket id " + id);
        }

        if (ticket.getTitle() != null) {
            currentTicket.setTitle(ticket.getTitle());
        }

        if (ticket.getDescription() != null) {
            currentTicket.setDescription(ticket.getDescription());
        }

        if (ticket.getImageurl() != null) {
            currentTicket.setImageurl(ticket.getImageurl());
        }

        if (ticket.getPriority() != null) {
            currentTicket.setPriority(ticket.getPriority());
        }

        if (ticket.isCompleted() != currentTicket.isCompleted()) {
            currentTicket.setCompleted(ticket.isCompleted());
        }

        if (ticket.getAssignedUser() != null) {
            currentTicket.setAssignedUser(ticket.getAssignedUser());
        }

        return ticketRepository.save(currentTicket);
    }
}
