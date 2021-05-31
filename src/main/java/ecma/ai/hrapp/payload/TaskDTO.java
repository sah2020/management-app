package ecma.ai.hrapp.payload;

import ecma.ai.hrapp.entity.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Timestamp deadline;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @NotNull
    private UUID taskTakerId;

    private UUID taskGiverId;

    private Timestamp completedDate;

    public TaskDTO(String name, String description, Timestamp deadline, UUID taskTakerId) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.taskTakerId = taskTakerId;
    }
}
