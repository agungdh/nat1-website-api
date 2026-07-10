package id.my.agungdh.nat1_website_api.repository;

import id.my.agungdh.nat1_website_api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByUuid(String uuid);
    Optional<Category> findBySlug(String slug);
}
