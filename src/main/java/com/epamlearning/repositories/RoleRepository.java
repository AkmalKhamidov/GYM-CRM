package com.epamlearning.repositories;

import com.epamlearning.entities.Role;
import com.epamlearning.entities.enums.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, BaseRepository {

    Optional<Role> findByName(RoleName name);

}
