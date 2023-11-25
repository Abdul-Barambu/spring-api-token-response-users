package com.abdul.SpringApiResponse.registration;

import com.abdul.SpringApiResponse.appuser.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1")
@AllArgsConstructor
@CrossOrigin("*")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping(path = "/registration/payees")
    public ResponseEntity<Object> registerPayee(@RequestBody Registration registration){
        return registrationService.registerPayee(registration);
    }

    @PostMapping(path = "/registration/schools")
    public ResponseEntity<Object> registerSchool(@RequestBody Registration registration){
        return registrationService.registerSchool(registration);
    }

    @GetMapping(path = "/confirm")
    public ResponseEntity<Object> confirm(String token){
        return registrationService.confirmToken(token);
    }

    @GetMapping(path = "/getAllUsers")
    public List<AppUser> getAllUsers(){
        return registrationService.getUsers();
    }
}
