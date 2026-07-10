package id.my.agungdh.nat1_website_api.repository;

import id.my.agungdh.nat1_website_api.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByUuid(String uuid);
    Optional<Post> findBySlug(String slug);

    @Query(value = "SELECT * FROM post WHERE deleted_at IS NULL AND to_tsvector('english', content) @@ plainto_tsquery('english', :query)", nativeQuery = true)
    List<Post> searchByContent(@Param("query") String query);
}
