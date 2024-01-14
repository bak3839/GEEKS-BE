package com.example.geeks.domain;

import com.example.geeks.Enum.DormitoryType;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String content;

    private int like_count;

    private DormitoryType type;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();

    public void setMember(Member member) {
        this.member = member;

        if(!member.getPosts().contains(this)) {
            member.addPost(this);
        }
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
    }

    @Builder
    public Post(String title, String content, int like_count, DormitoryType type) {
        this.title = title;
        this.content = content;
        this.like_count = like_count;
        this.type = type;
    }
}
