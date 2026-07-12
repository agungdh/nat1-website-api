package id.my.agungdh.nat1_website_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "users", indexes = {
        @Index(columnList = "uuid"),
        @Index(columnList = "username", unique = true)
})
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String role;
}
