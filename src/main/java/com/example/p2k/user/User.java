package com.example.p2k.user;

import com.example.p2k.courseuser.CourseUser;
import com.example.p2k.post.Post;
import com.example.p2k.reply.Reply;
import com.example.p2k.vm.Vm;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="user_tb")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false, unique = true)
    private String email; // 중복 체크 필요

    //@Column(length = 20, nullable = false) //소셜 로그인은 이름 필요 없음 - 보류
    private String name;

    //@Column(length = 256, nullable = false) //소셜 로그인은 패스워드 필요 없음 - 보류
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;    // 학생, 교육자, 관리자

    @OneToMany(mappedBy = "user")
    private List<Vm> vms = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<CourseUser> courseUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Reply> replies = new ArrayList<>();

    @Builder
    public User(Long id, String email, String name, String password, Role role, List<Vm> vms, List<Post> posts, List<Reply> replies) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.vms = vms;
        this.posts = posts;
        this.replies = replies;
    }

    public User updateEmail(String email){
        this.email = email;
        return this;
    }
}