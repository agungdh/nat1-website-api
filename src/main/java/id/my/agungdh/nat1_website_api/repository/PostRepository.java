package id.my.agungdh.nat1_website_api.repository;

import id.my.agungdh.nat1_website_api.entity.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"category", "tags"})
    Optional<Post> findByUuid(String uuid);

    @EntityGraph(attributePaths = {"category", "tags"})
    Optional<Post> findBySlug(String slug);

    @EntityGraph(attributePaths = {"category", "tags"})
    List<Post> findAll();

    @Query(value = "SELECT * FROM post WHERE deleted_at IS NULL AND to_tsvector('english', content) @@ plainto_tsquery('english', :query)", nativeQuery = true)
    @EntityGraph(attributePaths = {"category", "tags"})
    List<Post> searchByContent(@Param("query") String query);

    @Query("SELECT p.title FROM Post p")
    List<String> findAllTitles();
}
