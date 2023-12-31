package com.abdul.SpringApiResponse.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TokenConfirmationService {

    private final TokenConfirmationRepository tokenConfirmationRepository;

    public void saveToken(TokenConfirmation tokenConfirmation){
        tokenConfirmationRepository.save(tokenConfirmation);
    }

    public Optional<TokenConfirmation> getToken(String token){
        return tokenConfirmationRepository.findByToken(token);
    }

    public int setConfirmedAt(String token){
        return tokenConfirmationRepository.updateConfirmedAt(
                token,
                LocalDateTime.now()
        );
    }
}
