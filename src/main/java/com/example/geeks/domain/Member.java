package com.example.geeks.domain;

import com.example.geeks.Enum.DormitoryType;
import com.example.geeks.Enum.Gender;
import com.example.geeks.requestDto.ProfileEditDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Getter
@ToString(exclude = "password")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    private String email;

    private String password;

    private String major;

    private int studentID;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private int exp;

    private String image_url;

    private String introduction;

    @Enumerated(EnumType.STRING)
    private DormitoryType type;

    @OneToOne(mappedBy = "member", cascade = ALL)
    @JoinColumn(name = "detail_id")
    private Detail detail;

    @OneToMany(mappedBy = "member", cascade = ALL)
    private List<Point> point = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {PERSIST, REMOVE})
    private List<Comment> comments = new ArrayList<>();

    public void changeIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void addPost(Post post) {
        this.posts.add(post);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void changeProfile(ProfileEditDTO dto) {
        this.nickname = dto.getNickname();
        this.major = dto.getMajor();
        this.studentID = dto.getStudentID();
        this.introduction = dto.getIntroduction();
    }

    @Builder
    public Member(String nickname, String email, String password, String major, int studentID, Gender gender, int exp, String image_url, String introduction, DormitoryType type) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.major = major;
        this.studentID = studentID;
        this.gender = gender;
        this.exp = exp;
        this.image_url = image_url;
        this.introduction = introduction;
        this.type = type;
    }
}
