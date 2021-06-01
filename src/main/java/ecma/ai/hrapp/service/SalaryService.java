package ecma.ai.hrapp.service;

import ecma.ai.hrapp.component.Checker;
import ecma.ai.hrapp.entity.PaidSalary;
import ecma.ai.hrapp.entity.Role;
import ecma.ai.hrapp.entity.User;
import ecma.ai.hrapp.entity.enums.Month;
import ecma.ai.hrapp.entity.enums.RoleName;
import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.SalaryDto;
import ecma.ai.hrapp.repository.SalaryRepository;
import ecma.ai.hrapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class SalaryService {
    @Autowired
    SalaryRepository salaryRepository;
    @Autowired
    UserService userService;
    @Autowired
    Checker checker;
    @Autowired
    UserRepository userRepository;

    public ApiResponse add(SalaryDto salaryDto){
        Optional<User> byEmail = userRepository.findByEmail(salaryDto.getEmail());
        if (!byEmail.isPresent()) return new ApiResponse("User not found",false);
        User user = byEmail.get();

        Set<Role> roles = user.getRoles();
        String role = RoleName.ROLE_STAFF.name();
        for (Role rol : roles) {
            role = rol.getName().name();
        }

        boolean check = checker.check(role);
        if(!check)
            return new ApiResponse("No access", false);

        PaidSalary paidSalary = new PaidSalary();
        paidSalary.setAmount(salaryDto.getAmount());
        paidSalary.setOwner(user);
        paidSalary.setPeriod(salaryDto.getPeriod());
        PaidSalary save = salaryRepository.save(paidSalary);
        return new ApiResponse("Xodimga oylik kiritildi!", true,save);
    }
    public ApiResponse getByUser(String email){
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (!byEmail.isPresent()) return new ApiResponse("User not found",false);
        User user = byEmail.get();
        Set<Role> roles = user.getRoles();
        String role = RoleName.ROLE_STAFF.name();
        for (Role rolex : roles) {
            role = rolex.getName().name();
        }
        boolean check = checker.check(role);
        if(!check)
            return new ApiResponse("Sizda huquq yo'q!", false);
        return new ApiResponse("List by Owner", true, salaryRepository.findAllByOwner(user));
    }

    public ApiResponse getByMonth(String month){
        boolean check = checker.check();
        if (!check)
            return new ApiResponse("Sizda huquq yo'q", false);
        Month period = null;
        for (Month value : Month.values()) {
            if (value.name().equals(month)){
                period = value;
                break;
            }
        }
        if (period == null)
            return new ApiResponse("Month xato!", false);
        return new ApiResponse("List by period", true,salaryRepository.findAllByPeriod(period));
    }

}
