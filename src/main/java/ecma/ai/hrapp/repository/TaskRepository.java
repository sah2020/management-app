package ecma.ai.hrapp.repository;

import ecma.ai.hrapp.entity.Task;
import ecma.ai.hrapp.entity.Turniket;
import ecma.ai.hrapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findAllByTaskTaker(User taskTaker);
}
