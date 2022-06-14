package pl.szinton.querky.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.szinton.querky.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
