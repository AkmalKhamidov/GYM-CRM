package com.epamlearning.repositories;

import com.epamlearning.entities.LoginAttempt;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long>, BaseRepository{

    @Query(value = "SELECT s.* FROM login_attempts s WHERE s.username = ?1 and clock_timestamp() - s.timestamp <= interval '3 minute'", nativeQuery = true)
    List<LoginAttempt> findByUsername(String username);
    void deleteByUsername(String username);
}
