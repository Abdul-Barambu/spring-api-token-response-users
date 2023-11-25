package com.abdul.SpringApiResponse.registration;

import com.abdul.SpringApiResponse.appuser.AppUser;
import com.abdul.SpringApiResponse.appuser.AppUserRole;
import com.abdul.SpringApiResponse.appuser.AppUserService;
import com.abdul.SpringApiResponse.token.TokenConfirmation;
import com.abdul.SpringApiResponse.token.TokenConfirmationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final AppUserService appUserService;
    private final TokenConfirmationService tokenConfirmationService;


    //    Register Payees
    public ResponseEntity<Object> registerPayee(Registration registration) {
        boolean isEmailValid = emailValidator.test(registration.getEmail());
        if (!isEmailValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createInvalidEmailResponse("error", "Email not valid"));
        }

        return appUserService.signUpUser(
                new AppUser(
                        registration.getName(),
                        registration.getEmail(),
                        registration.getPassword(),
                        AppUserRole.PAYEE
                )
        );
    }


    //    Register Schools
    public ResponseEntity<Object> registerSchool(Registration registration) {
        boolean isEmailValid = emailValidator.test(registration.getEmail());
        if (!isEmailValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createInvalidEmailResponse("error", "Email not valid"));
        }

        return appUserService.signUpUser(
                new AppUser(
                        registration.getName(),
                        registration.getEmail(),
                        registration.getPassword(),
                        AppUserRole.SCHOOL
                )
        );
    }

//    Confirm Token
    public ResponseEntity<Object> confirmToken(String token){
        try{

            TokenConfirmation tokenConfirmation = tokenConfirmationService.getToken(token).orElseThrow(() -> new IllegalStateException("Tokrn not found"));
            if(tokenConfirmation.getConfirmedAt() !=null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createTokenConfirmResponse("error", "Token already confirmed", tokenConfirmation.getConfirmedAt()));
            }

            LocalDateTime expires = tokenConfirmation.getExpiresAt();
            if(expires.isBefore(LocalDateTime.now())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createTokenExpiredResponse("error", "Token has expired already", tokenConfirmation.getExpiresAt()));
            }

            tokenConfirmationService.setConfirmedAt(token);
            appUserService.enabledUser(tokenConfirmation.getAppUser().getEmail());

            return ResponseEntity.ok(createSuccessResponse("success", "Your token has been confirmed successfully"));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createServerError("error", e.getMessage()));
        }
    }

    public List<AppUser> getUsers(){
        return appUserService.getAllUsers();
    }


//    Response
    private Map<String,Object> createInvalidEmailResponse(String status, String message){
        Map<String,Object> response = new HashMap<>();
        response.put("Status", status);

        Map<String,Object> data = new HashMap<>();
        data.put("Message", message);
        response.put("Data", data);

        return response;
    }

    private Map<String,Object> createTokenConfirmResponse(String status, String message, LocalDateTime confirmedAt){
        Map<String,Object> response = new HashMap<>();
        response.put("Status", status);

        Map<String,Object> data = new HashMap<>();
        data.put("Message", message);
        data.put("Token confirmed at", confirmedAt);
        response.put("Data", data);

        return response;
    }

    private Map<String,Object> createTokenExpiredResponse(String status, String message, LocalDateTime expiredAt){
        Map<String,Object> response = new HashMap<>();
        response.put("Status", status);

        Map<String,Object> data = new HashMap<>();
        data.put("Message", message);
        data.put("Token expired at", expiredAt);
        response.put("Data", data);

        return response;
    }

    private Map<String,Object> createSuccessResponse(String status, String message){
        Map<String,Object> response = new HashMap<>();
        response.put("Status", status);

        Map<String,Object> data = new HashMap<>();
        data.put("Message", message);
        response.put("Data", data);

        return response;
    }

    private Map<String,Object> createServerError(String status, String message){
        Map<String,Object> response = new HashMap<>();
        response.put("Status", status);

        Map<String,Object> data = new HashMap<>();
        data.put("Message", message);
        response.put("Data", data);

        return response;
    }
}
