package com.example.p2k.course;

import com.example.p2k.user.BaseTimeEntity;
import com.example.p2k.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="course_tb")
public class Course extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 256, nullable = false)
    private String name;

    @Column(length = 256, nullable = false)
    private String description;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Course(Long id, String name, String description, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.user = user;
    }
}
