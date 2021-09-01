package ecma.ai.hrapp.service;

import ecma.ai.hrapp.component.Checker;
import ecma.ai.hrapp.entity.Role;
import ecma.ai.hrapp.entity.Turniket;
import ecma.ai.hrapp.entity.TurniketHistory;
import ecma.ai.hrapp.entity.enums.RoleName;
import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.TurniketHistoryDto;
import ecma.ai.hrapp.repository.TurniketHistoryRepository;
import ecma.ai.hrapp.repository.TurniketRepository;
import ecma.ai.hrapp.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;

@Service
public class TurniketHistoryService {
    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    TurniketHistoryRepository turniketHistoryRepository;
    @Autowired
    TurniketRepository turniketRepository;
    @Autowired
    Checker checker;
    public ApiResponse get(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");

        token = token.substring(7);

        String username = jwtProvider.getUsernameFromToken(token);//username
        if (username.equals("direktor")) {
            return new ApiResponse("Success",true,turniketHistoryRepository.findAll());
        }
        else{
            return new ApiResponse("ACCESS DENIED",false);
        }

    }
    public ApiResponse enteringCompanyAdd(TurniketHistoryDto turniketHistoryDto) {
        Optional<Turniket> byId = turniketRepository.findById(turniketHistoryDto.getTurniketNumber());
        if (!byId.isPresent()) return new ApiResponse("TURNSTILE not found,no access to enter", false);

        Set<Role> roles = byId.get().getOwner().getRoles();
        String role = RoleName.ROLE_STAFF.name();
        for (Role role1 : roles) {
            role = role1.getName().name();
            break;
        }

        boolean check = checker.check(role);
        if (!check)
            return new ApiResponse("BAD ACTION!!!", false);

        TurniketHistory turniketHistory = new TurniketHistory();
        turniketHistory.setTurniket(byId.get());
        turniketHistory.setType(turniketHistoryDto.getType());
        turniketHistoryRepository.save(turniketHistory);
        return new ApiResponse("WELCOME!", true);
    }
}
