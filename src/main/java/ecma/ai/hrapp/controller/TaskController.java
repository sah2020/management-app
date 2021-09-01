package ecma.ai.hrapp.controller;

import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.TaskDTO;
import ecma.ai.hrapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody TaskDTO taskDTO) throws MessagingException {
        ApiResponse apiResponse = taskService.add(taskDTO);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getYourTasks() {
        ApiResponse staffsTask = taskService.getStaffsTask();
        return ResponseEntity.status(staffsTask.isSuccess() ? HttpStatus.FOUND : HttpStatus.CONFLICT).body(staffsTask);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editTaskStaff(@PathVariable UUID id, @Valid @RequestBody TaskDTO taskDTO) {
        ApiResponse apiResponse = taskService.staffCompletedTask(id, taskDTO);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(apiResponse);
    }
}
