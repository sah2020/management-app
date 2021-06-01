package ecma.ai.hrapp.controller;

import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.TurniketHistoryDto;
import ecma.ai.hrapp.service.TurniketHistoryService;
import ecma.ai.hrapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/turniketHistory")
public class TurniketHistoryController {
    @Autowired
    TurniketHistoryService turniketHistoryService;

    @GetMapping
    public HttpEntity<?> get(HttpServletRequest httpServletRequest){
        ApiResponse apiResponse = turniketHistoryService.get(httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody TurniketHistoryDto turniketHistoryDto){
        ApiResponse apiResponse = turniketHistoryService.enteringCompanyAdd(turniketHistoryDto);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.ACCEPTED:HttpStatus.CONFLICT).body(apiResponse);
    }

}
