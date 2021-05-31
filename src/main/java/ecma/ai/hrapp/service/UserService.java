package ecma.ai.hrapp.service;

import ecma.ai.hrapp.component.Checker;
import ecma.ai.hrapp.component.MailSender;
import ecma.ai.hrapp.component.PasswordGenerator;
import ecma.ai.hrapp.entity.Role;
import ecma.ai.hrapp.entity.User;
import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.UserDto;
import ecma.ai.hrapp.repository.RoleRepository;
import ecma.ai.hrapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    Checker checker;
    @Autowired
    PasswordGenerator passwordGenerator;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MailSender mailSender;

    public ApiResponse add(UserDto userDto) throws MessagingException {
        Optional<Role> optionalRole = roleRepository.findById(userDto.getRoleId());
        if (!optionalRole.isPresent()) return new ApiResponse("Role id not found!", false);

        boolean check = checker.check(optionalRole.get().getName().name());//

        if (!check) {
            return new ApiResponse("Dostup netu!", false);
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            return new ApiResponse("Already exists!", false);
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPosition(userDto.getPosition());

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(optionalRole.get());
        user.setRoles(roleSet);

        String password = passwordGenerator.genRandomPassword(8);
        user.setPassword(passwordEncoder.encode(password));
//    user.setPassword(password);
        String code = UUID.randomUUID().toString();
        user.setVerifyCode(code);

        userRepository.save(user);

        //mail xabar yuborish kk
        boolean addStaff = mailSender.mailTextAddStaff(userDto.getEmail(), code, password);

        if (addStaff) {
            return new ApiResponse("User qo'shildi! va emailga xabar ketdi!", true);
        } else {
            return new ApiResponse("Xatolik yuz berdi", false);
        }

    }

    public ApiResponse edit(String password) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<User> optionalUser = userRepository.findById(user.getId());

        if (!optionalUser.isPresent())
            return new ApiResponse("Not Found", false);

        User user1 = optionalUser.get();
        if (!user1.isEnabled() || !user1.isAccountNonExpired() || !user1.isAccountNonLocked() || !user1.isCredentialsNonExpired())
            return new ApiResponse("Your Account Does Not Have Access to This Function", false);
        user1.setPassword(passwordEncoder.encode(password));
        userRepository.save(user1);
        return new ApiResponse("Password Changed Successfully", true);
    }
}
