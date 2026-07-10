package id.my.agungdh.nat1_website_api.repository;

import id.my.agungdh.nat1_website_api.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByUuid(String uuid);
    Optional<Post> findBySlug(String slug);
}
