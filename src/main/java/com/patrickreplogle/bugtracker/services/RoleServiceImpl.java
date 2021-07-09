package com.patrickreplogle.bugtracker.services;

import com.patrickreplogle.bugtracker.exceptions.ResourceFoundException;
import com.patrickreplogle.bugtracker.exceptions.ResourceNotFoundException;
import com.patrickreplogle.bugtracker.models.Role;
import com.patrickreplogle.bugtracker.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private UserAuditing userAuditing;

    @Override
    public List<Role> findAll() {
        List<Role> list = new ArrayList<>();

        roleRepository.findAll()
                    .iterator()
                    .forEachRemaining(list::add);

        return list;
    }

    @Override
    public Role findRoleById(long id) throws ResourceNotFoundException {
        return roleRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Role id " + id + " not found."));
    }

    @Transactional
    @Override
    public Role save(Role role)
    {
        if (role.getUsers().size() > 0) {
            throw new ResourceFoundException("User Roles are not updated through Role.");
        }
        return roleRepository.save(role);
    }

    @Override
    public Role findByName(String name) throws ResourceNotFoundException {
        Role r = roleRepository.findByNameIgnoreCase(name);

        if (r != null) {
            return r;
        }
        throw new ResourceNotFoundException("Role " + name + " not found.");
    }

    @Transactional
    @Override
    public Role update(long id, Role role) {
        if (role.getName() == null) {
            throw new ResourceNotFoundException("No role name found to update!");
        }

        if (role.getUsers().size() > 0) {
            throw new ResourceFoundException("User Roles are not updated through Role. See endpoint POST: users/user/{userid}/role/{roleid}");
        }

        Role newRole = findRoleById(id); // see if id exists

        roleRepository.updateRoleName(
                userAuditing.getCurrentAuditor().get(),
                id,
                role.getName());

        return findRoleById(id);
    }

    @Transactional
    @Override
    public void delete(long id) {
        roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role id " + id + " not found."));
        roleRepository.deleteById(id);
    }
}
