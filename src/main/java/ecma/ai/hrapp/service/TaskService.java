package ecma.ai.hrapp.service;

import ecma.ai.hrapp.component.Checker;
import ecma.ai.hrapp.component.MailSender;
import ecma.ai.hrapp.entity.Role;
import ecma.ai.hrapp.entity.Task;
import ecma.ai.hrapp.entity.User;
import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.TaskDTO;
import ecma.ai.hrapp.repository.TaskRepository;
import ecma.ai.hrapp.repository.UserRepository;
import ecma.ai.hrapp.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    Checker checker;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MailSender mailSender;

    @Autowired
    JwtProvider jwtProvider;

    public ApiResponse add(TaskDTO taskDTO) throws MessagingException {
        User taskGiver = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<User> optionalTaskGiver = userRepository.findById(taskGiver.getId());
        if (!optionalTaskGiver.isPresent())
            return new ApiResponse("User Not Found", false);

        Optional<User> optionalUser = userRepository.findById(taskDTO.getTaskTakerId());
        if (!optionalUser.isPresent())
            return new ApiResponse("Task Taker ID Not Found", false);

        User user = optionalUser.get();

        Set<Role> roles = user.getRoles();

        Integer checkerInt = 0;

        for (Role role : roles) {
            checkerInt += checker.checkForTask(role.getName().name());
        }

        if (checkerInt < 1)
            return new ApiResponse("Dostup Netu", false);

        Task task = new Task();
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setDeadline(taskDTO.getDeadline());
        task.setTaskTaker(userRepository.getOne(taskDTO.getTaskTakerId()));
        task.setTaskGiver(optionalTaskGiver.get());
        task.setCompletedDate(null);
        Task savedTask = taskRepository.save(task);

        boolean sendEmail = mailSender.mailTextAddTask(savedTask.getTaskTaker().getEmail(), savedTask.getName());

        if (!sendEmail)
            return new ApiResponse("Task Added, but there was error in sending email", false, savedTask);

        return new ApiResponse("Task Added and Email Successfully Sent!", true, savedTask);
    }
}
