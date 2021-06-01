package ecma.ai.hrapp.repository;

import ecma.ai.hrapp.entity.PaidSalary;
import ecma.ai.hrapp.entity.User;
import ecma.ai.hrapp.entity.enums.Month;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SalaryRepository extends JpaRepository<PaidSalary, UUID> {
        List<PaidSalary> findAllByOwner(User owner);


    List<PaidSalary> findAllByPeriod(Month period);
}
