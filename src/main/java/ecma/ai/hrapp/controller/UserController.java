package ecma.ai.hrapp.controller;

import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.UserDto;
import ecma.ai.hrapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    //Yangi user qo'shish
    //MANAGER,DIREKTOR //PreAuthorize
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody UserDto userDto) throws MessagingException {

        ApiResponse response = userService.add(userDto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PutMapping("/edit")
    public HttpEntity<?> edit(@RequestBody String password){
        ApiResponse apiResponse = userService.edit(password);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    //FOR DIRECTOR AND MANAGER
    @GetMapping("/{email}")
    public HttpEntity<?> getOneUser(@PathVariable String email){
        ApiResponse apiResponse = userService.getOne(email);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK:HttpStatus.BAD_REQUEST).body(apiResponse);

    }
}
