package com.patrickreplogle.bugtracker.services.tickets;

import com.patrickreplogle.bugtracker.exceptions.AccessDeniedException;
import com.patrickreplogle.bugtracker.exceptions.ResourceNotFoundException;
import com.patrickreplogle.bugtracker.models.CloudinaryUploader;
import com.patrickreplogle.bugtracker.models.Project;
import com.patrickreplogle.bugtracker.models.Ticket;
import com.patrickreplogle.bugtracker.models.User;
import com.patrickreplogle.bugtracker.repository.ProjectRepository;
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

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    CloudinaryUploader cloudinaryUploader;

    @Override
    public Ticket findTicketById(long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket with id " + id + " not found."));
    }

    @Override
    public Ticket save(Ticket ticket) throws ResourceNotFoundException, AccessDeniedException {
        Project project = projectRepository.findById(ticket.getProject().getProjectid())
                .orElseThrow(() -> new ResourceNotFoundException("Project with id " + ticket.getProject().getProjectid() + " not found."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        if (!project.getUsers().contains(user)) {
            throw new AccessDeniedException("User " + authentication.getName() + " does not have permission to create ticket");
        }

        // upload image to cloudinary
        if (ticket.getImageurl() != null && !ticket.getImageurl().startsWith("http")) {
            ticket.setImageurl(cloudinaryUploader.addNewImage(ticket.getImageurl()));
        }

        return ticketRepository.save(ticket);
    }

    @Override
    public void delete(long id) throws ResourceNotFoundException, AccessDeniedException {
        Ticket ticketToDelete = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket id " + id + " not found."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        if (!ticketToDelete.getProject().getUsers().contains(user)) {
            throw new AccessDeniedException("User " + authentication.getName() + " does not have permission to delete ticket id " + id);
        }

        // delete image from cloudinary
        if (ticketToDelete.getImageurl() != null) {
            cloudinaryUploader.removeImage(ticketToDelete.getImageurl());
        }

        ticketRepository.deleteById(id);
    }

    @Override
    public Ticket update(Ticket ticket, long id) throws AccessDeniedException {
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

        if (ticket.getPriority() != null) {
            currentTicket.setPriority(ticket.getPriority());
        }

        if (ticket.isCompleted() != currentTicket.isCompleted()) {
            currentTicket.setCompleted(ticket.isCompleted());
        }

        if (ticket.getAssignedUser() != null) {
            if (currentTicket.getAssignedUser() == null || ticket.getAssignedUser().getUserid() != currentTicket.getAssignedUser().getUserid()) {
                currentTicket.setAssignedUser(ticket.getAssignedUser());
            } else {
                currentTicket.setAssignedUser(null);
            }
        }

        if (ticket.getImageurl() != null && !ticket.getImageurl().startsWith("http")) {
            String url = currentTicket.getImageurl();
            if (url == null) {
                currentTicket.setImageurl(cloudinaryUploader.addNewImage(ticket.getImageurl()));
            } else {
                currentTicket.setImageurl(cloudinaryUploader.updateImage(ticket.getImageurl(), url));
            }
        }

        return ticketRepository.save(currentTicket);
    }
}
