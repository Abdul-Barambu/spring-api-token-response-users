package com.abdul.SpringApiResponse.appuser;

import com.abdul.SpringApiResponse.token.TokenConfirmation;
import com.abdul.SpringApiResponse.token.TokenConfirmationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenConfirmationService tokenConfirmationService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(() -> new IllegalStateException("Email nor found"));
    }

    public ResponseEntity<Object> signUpUser(AppUser appUser){
        boolean emailExist = appUserRepository.findByEmail(appUser.getEmail()).isPresent();
        if(emailExist){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createEmailExist("error", "Email (" +appUser.getEmail()+ ") already exist, register with another email"));
        }

        String passwordEncoded = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(passwordEncoded);

        appUserRepository.save(appUser);

//        Token

        String token = UUID.randomUUID().toString();
        TokenConfirmation tokenConfirmation =  new TokenConfirmation(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10),
                appUser
        );

        tokenConfirmationService.saveToken(tokenConfirmation);


        return ResponseEntity.ok(createRegisterResponse("success", "Registration Successfully", token,
                "confirm your token using the url below to get access", "localhost:8080/api/v1/confirm?token="+token));
    }

    public int enabledUser(String email){
        return appUserRepository.enableUser(email);
    }

    public List<AppUser> getAllUsers(){
        return appUserRepository.findAll();
    }


//    Response
    private Map<String, Object> createEmailExist(String status, String message){
        Map<String, Object> response = new HashMap<>();
        response.put("Status", status);

        Map<String, Object> data = new HashMap<>();
        data.put("Message", message);

        response.put("Data", data);

        return response;
    }

    private Map<String,Object> createRegisterResponse(String status, String message, String token, String msg, String url){
        Map<String, Object> response = new HashMap<>();
        response.put("Status", status);

        Map<String, Object> data = new HashMap<>();
        data.put("Message", message);
        data.put("Token", token);
        data.put("msg", msg);
        data.put("URL", url);

        response.put("Data", data);

        return response;
    }
}
