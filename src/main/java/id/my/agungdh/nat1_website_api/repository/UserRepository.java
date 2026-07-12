package id.my.agungdh.nat1_website_api.repository;

import id.my.agungdh.nat1_website_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
