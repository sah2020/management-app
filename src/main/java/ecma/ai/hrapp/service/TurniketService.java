package ecma.ai.hrapp.service;

import ecma.ai.hrapp.component.Checker;
import ecma.ai.hrapp.entity.Company;
import ecma.ai.hrapp.entity.Turniket;
import ecma.ai.hrapp.entity.User;
import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.TurniketDTO;
import ecma.ai.hrapp.repository.CompanyRepository;
import ecma.ai.hrapp.repository.TurniketHistoryRepository;
import ecma.ai.hrapp.repository.TurniketRepository;
import ecma.ai.hrapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
public class TurniketService {
    @Autowired
    TurniketRepository turniketRepository;
    @Autowired
    TurniketHistoryRepository tuniketHistoryRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    Checker checker;

    public void add(Integer companyId, UUID userId) {
        Turniket turniket = new Turniket();
        turniket.setCompany(companyRepository.getOne(companyId));
        turniket.setOwner(userRepository.getOne(userId));
        turniketRepository.save(turniket);
    }

    public ApiResponse addTurniket(TurniketDTO turniketDTO) {
        boolean check = checker.check();
        if (!check)
            return new ApiResponse("Access Not Found",false);

        Optional<User> optionalUser = userRepository.findById(turniketDTO.getUserId());
        if (!optionalUser.isPresent())
            return new ApiResponse("User Not Found", false);

        Optional<Company> optionalCompany = companyRepository.findById(turniketDTO.getCompanyId());
        if (!optionalCompany.isPresent())
            return new ApiResponse("Company Not Found", false);

        Turniket turniket = new Turniket();
        turniket.setOwner(optionalUser.get());
        turniket.setCompany(optionalCompany.get());
        Turniket savedTurniket = turniketRepository.save(turniket);
        return new ApiResponse("TURNSTILE Addded", true, savedTurniket);
    }

    public ApiResponse getAll() {
        boolean check = checker.check();
        if (!check)
            return new ApiResponse("Access Not Found", false);
        return new ApiResponse("Success", true, turniketRepository.findAll());
    }

    public ApiResponse delete(String number) {
        boolean check = checker.check();
        if (!check)
            return new ApiResponse("Access Not Found", false);
        Optional<Turniket> optionalTurniket = turniketRepository.findByNumber(number);
        if (!optionalTurniket.isPresent())
            return new ApiResponse("TURNSTILE Not Found", false);
        turniketRepository.deleteByNumber(number);
        return new ApiResponse("Successfully Deleted", true);
    }
}
