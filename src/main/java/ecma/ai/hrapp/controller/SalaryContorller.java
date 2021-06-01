package ecma.ai.hrapp.controller;

import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.SalaryDto;
import ecma.ai.hrapp.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/salary")
public class SalaryContorller {
    @Autowired
    SalaryService salaryService;

    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody SalaryDto salaryDto){
        ApiResponse apiResponse = salaryService.add(salaryDto);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK:HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @GetMapping("{email}")
    public HttpEntity<?> getByUser(@PathVariable String email){
        ApiResponse apiResponse = salaryService.getByUser(email);
        return ResponseEntity.status(apiResponse.isSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping("/{month}")
    public HttpEntity<?> getByMonth(@PathVariable String month){
        ApiResponse apiResponse = salaryService.getByMonth(month);
        return ResponseEntity.status(apiResponse.isSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(apiResponse);
    }


}
