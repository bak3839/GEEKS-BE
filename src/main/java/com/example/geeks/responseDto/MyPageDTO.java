package com.example.geeks.responseDto;

import com.example.geeks.Enum.DormitoryType;
import com.example.geeks.Enum.Gender;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MyPageDTO {
    private String nickname;

    private String major;

    private String introduction;

    private String photoName;

    private int studentID;

    private boolean exist;

    private boolean open;

    private DormitoryType type;

    private Gender gender;

    @Builder
    public MyPageDTO(String nickname, String major, String introduction, String photoName, int studentID, boolean exist, boolean open, DormitoryType type, Gender gender) {
        this.nickname = nickname;
        this.major = major;
        this.introduction = introduction;
        this.photoName = photoName;
        this.studentID = studentID;
        this.exist = exist;
        this.open = open;
        this.type = type;
        this.gender = gender;
    }
}
