package com.epamlearning.repositories;

import com.epamlearning.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, BaseRepository{
    Optional<User> findByUsername(String username);

}
