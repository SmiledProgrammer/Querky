package pl.szinton.querky.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.szinton.querky.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);
}
