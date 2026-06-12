package com.talenthub.job.domain.model;

import com.talenthub.web.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Getter
@Entity
@Table(name = "skills")
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Skill extends BaseEntity {

    public enum Type {
        TECHNICAL, SOFT, LANGUAGE, CERTIFICATION
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Type type;

    public static Skill create(String name, Type type) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Skill name must not be blank");
        }
        if (type == null) {
            throw new IllegalArgumentException("Skill type must not be null");
        }
        Skill skill = new Skill();
        skill.id = UUID.randomUUID();
        skill.name = name.trim();
        skill.type = type;
        return skill;
    }

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Skill name must not be blank");
        }
        this.name = newName.trim();
    }
}
