package ar.edu.um.fi.ingsr.persistence.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import ar.edu.um.fi.ingsr.persistence.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmail(String email);
    
}
