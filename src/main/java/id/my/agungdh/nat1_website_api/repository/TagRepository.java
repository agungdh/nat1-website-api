package id.my.agungdh.nat1_website_api.repository;

import id.my.agungdh.nat1_website_api.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByUuid(String uuid);
    Optional<Tag> findBySlug(String slug);
    List<Tag> findByUuidIn(List<String> uuids);
}
