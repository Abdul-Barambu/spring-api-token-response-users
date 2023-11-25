package com.abdul.SpringApiResponse.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenConfirmationRepository extends JpaRepository<TokenConfirmation, Long> {

    Optional<TokenConfirmation> findByToken(String token);


    @Transactional
    @Modifying
    @Query("UPDATE TokenConfirmation t SET t.confirmedAt = ?2 WHERE t.token = ?1")
    int updateConfirmedAt(String token, LocalDateTime confirmedAt);
}
