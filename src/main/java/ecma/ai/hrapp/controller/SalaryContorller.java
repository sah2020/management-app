package ecma.ai.hrapp.controller;

import ecma.ai.hrapp.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/salary")
public class SalaryContorller {
    @Autowired
    SalaryService salaryService;
}
