package ecma.ai.hrapp.service;

import ecma.ai.hrapp.component.Checker;
import ecma.ai.hrapp.component.MailSender;
import ecma.ai.hrapp.entity.Role;
import ecma.ai.hrapp.entity.Task;
import ecma.ai.hrapp.entity.User;
import ecma.ai.hrapp.entity.enums.TaskStatus;
import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.TaskDTO;
import ecma.ai.hrapp.repository.TaskRepository;
import ecma.ai.hrapp.repository.UserRepository;
import ecma.ai.hrapp.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.util.*;

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

    public ApiResponse getStaffsTask() {
        User user1 = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> byId = userRepository.findById(user1.getId());
        if (!byId.isPresent()) return new ApiResponse("User not found", false);
        User user = byId.get();
        List<Task> byTaskTakerId = taskRepository.findAllByTaskTaker(user);
        List<TaskDTO> tasks = new ArrayList<>();
        for (Task task : byTaskTakerId) {
            TaskDTO taskDTO = new TaskDTO(task.getName(), task.getDescription(), task.getDeadline(),
                    task.getStatus(), task.getTaskGiver().getId(), task.getCompletedDate());
            tasks.add(taskDTO);
        }
        return new ApiResponse("Your tasks", true, byTaskTakerId);
    }

    public ApiResponse staffComletedTask(UUID id, TaskDTO taskDTO) {
        User taskTaker = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> byId = userRepository.findById(taskTaker.getId());
        if (!byId.isPresent()) return new ApiResponse("User not found", false);
        User user = byId.get();
        Optional<Task> byId1 = taskRepository.findById(id);
        if (!byId1.isPresent()) return new ApiResponse("Task not found", false);
        Task task = byId1.get();
        if (!task.getTaskTaker().getId().equals(user.getId()))
            return new ApiResponse("Task is not belong to you,and you don't change", false);
        task.setStatus(taskDTO.getStatus());
        if (taskDTO.getStatus().name().equals(TaskStatus.COMPLETED.name())) {
            task.setCompletedDate(new Timestamp(System.currentTimeMillis()));
        }
        Task save = taskRepository.save(task);
        if (taskDTO.getStatus().name().equals(TaskStatus.COMPLETED.name())) {
            try {
                boolean b = mailSender.mailTextCompleteTask(save.getTaskGiver().getEmail(), save.getTaskTaker().getUsername(), save.getName());
                if (b) return new ApiResponse("Task saqlandi qilindi va emailga xabar junatildi",true);
            } catch (MessagingException e) {
                return new ApiResponse("Task saqlandi, lekin emailga xabar yuborishda xatolik kelib chiqdi",true);
            }
        }
        return new ApiResponse("Task edited successfully",true);
    }
}
