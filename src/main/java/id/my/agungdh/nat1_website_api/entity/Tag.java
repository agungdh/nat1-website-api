package id.my.agungdh.nat1_website_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.SQLRestriction;

import java.util.Set;

@Entity
@Table(name = "tags")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
public class Tag extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts;
}
