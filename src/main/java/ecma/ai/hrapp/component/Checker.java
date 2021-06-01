package ecma.ai.hrapp.component;

import ecma.ai.hrapp.entity.Role;
import ecma.ai.hrapp.entity.User;
import ecma.ai.hrapp.entity.enums.RoleName;
import ecma.ai.hrapp.repository.TaskRepository;
import ecma.ai.hrapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class Checker {

    @Autowired
    UserRepository userRepository;


    public boolean check(String role) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<User> optionalUser = userRepository.findById(user.getId());

        if (optionalUser.isPresent()) {
            Set<Role> roles = optionalUser.get().getRoles();
            String position = optionalUser.get().getPosition();

            if (role.equals(RoleName.ROLE_DIRECTOR.name())) return false;
            for (Role adminRole : roles) {
                if (role.equals(RoleName.ROLE_MANAGER.name()) &&
                        adminRole.getName().name().equals(RoleName.ROLE_DIRECTOR.name())) {
                    return true;
                }
                if ((role.equals(RoleName.ROLE_STAFF.name()) &&
                        adminRole.getName().name().equals(RoleName.ROLE_MANAGER.name())&&
                        position.equalsIgnoreCase("HRMANAGER"))||
                adminRole.getName().name().equals(RoleName.ROLE_DIRECTOR.name())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Integer checkForTask(String role){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<User> optionalUser = userRepository.findById(user.getId());

        if (optionalUser.isPresent()) {
            Set<Role> roles = optionalUser.get().getRoles();

            for (Role adminRole : roles) {
                if (role.equals(RoleName.ROLE_MANAGER.name()) &&
                        adminRole.getName().name().equals(RoleName.ROLE_DIRECTOR.name())) {
                    return 1;
                }
                if (role.equals(RoleName.ROLE_STAFF.name()) &&
                        (adminRole.getName().name().equals(RoleName.ROLE_MANAGER.name())||
                        adminRole.getName().name().equals(RoleName.ROLE_DIRECTOR.name()))) {
                    return 1;
                }
            }
        }
        return 0;

    }
    public boolean check( ){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOptional = userRepository.findById(user.getId());
        if (userOptional.isPresent()){
            for (Role role : user.getRoles()) {
                if (role.getName().name().equals(RoleName.ROLE_DIRECTOR.name())
                        || ((role.getName().name().equals(RoleName.ROLE_MANAGER.name())
                        && user.getPosition().toLowerCase().equals("hrmanagement")))){
                    return true;
                }
            }
        }
        return false;
    }





}
