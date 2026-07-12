package id.my.agungdh.nat1_website_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Table(name = "categories")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
public class Category extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "category")
    private List<Post> posts;
}
