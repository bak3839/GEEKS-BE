package com.example.geeks.Init;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.geeks.Enum.*;
import com.example.geeks.domain.*;
import com.example.geeks.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.TimeZone;

@Component
@RequiredArgsConstructor
public class MemberInit {
    private final AcceptRoomMateRepository acceptRoomMateRepository;

    private final SuggestionRepository suggestionRepository;

    private final MemberRepository memberRepository;

    private final DetailRepository detailRepository;

    private final PostRepository postRepository;

    private final BCryptPasswordEncoder encoder;

    @PostConstruct
    public void memberInit() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));

//        Member admin = Member.builder()
//                .nickname("admin")
//                .email("admin@sangmyung.kr")
//                .password(encoder.encode("geeksadmin0221"))
//                .major("행정실")
//                .studentID(1)
//                .gender(Gender.MALE)
//                .image_url("")
//                .introduction("")
//                .type(DormitoryType.NEW)
//                .open(false)
//                .build();
//
//        memberRepository.save(admin);
//
//        Member member1 = Member.builder()
//                .nickname("geeks")
//                .email("geeks@sangmyung.kr")
//                .password(encoder.encode("1234"))
//                .major("소프트웨어학과")
//                .studentID(19)
//                .gender(Gender.MALE)
//                .image_url("")
//                .introduction("")
//                .type(DormitoryType.NEW)
//                .open(false)
//                .build();
//
//        memberRepository.save(member1);
//
//        Post post1 = Post.builder()
//                .commentCount(0)
//                .anonymity(false)
//                .title("GEEKS가 오픈했어요!")
//                .content("GEEKS와 함께 더 편리한 기숙사 생활 만들어요.")
//                .like_count(0)
//                .type(DormitoryType.NEW)
//                .build();
//
//        post1.setMember(member1);
//        postRepository.save(post1);
//
//        for(int i = 1; i <= 15; i++) {
//            Suggestion suggestion = Suggestion.builder()
//                    .title("필터 테스트 " + i)
//                    .content("필터 테스트 " + i)
//                    .agree_count(0)
//                    .suggestionState(SuggestionState.ONGOING)
//                    .build();
//            suggestion.setMember(member1);
//            suggestionRepository.save(suggestion);
//        }

        /*

        Member member2 = Member.builder()
                .nickname("member2")
                .email("2@sangmyung.kr")
                .password(encoder.encode("1234"))
                .major("소프트웨어")
                .studentID(17)
                .gender(Gender.FEMALE)
                .image_url("")
                .introduction("예민하지 않으신 분들 찾아요")
                .type(DormitoryType.NEW)
                .open(true)
                .build();

        Member member3 = Member.builder()
                .nickname("눈누난나")
                .email("3@sangmyung.kr")
                .password(encoder.encode("1234"))
                .major("소프트웨어")
                .studentID(22)
                .gender(Gender.FEMALE)
                .image_url("")
                .introduction("같은 과이신 분들 선호합니다!")
                .type(DormitoryType.OLD)
                .open(true)
                .build();

        Member member4 = Member.builder()
                .nickname("종강하고싶다")
                .email("4@sangmyung.kr")
                .password(encoder.encode("1234"))
                .major("전자공학과")
                .studentID(22)
                .gender(Gender.MALE)
                .image_url("")
                .introduction("같은 과이신 분들 선호합니다!")
                .type(DormitoryType.NEW)
                .open(true)
                .build();

        Member member5 = Member.builder()
                .nickname("멋쟁이 토마토")
                .email("5@sangmyung.kr")
                .password(encoder.encode("1234"))
                .major("인더스트리얼디자인")
                .studentID(21)
                .gender(Gender.FEMALE)
                .image_url("")
                .introduction("외출 잦은 사람 선호합니다!")
                .type(DormitoryType.NEW)
                .open(true)
                .build();

        Member member6 = Member.builder()
                .nickname("수뭉이")
                .email("6@sangmyung.kr")
                .password(encoder.encode("1234"))
                .major("디지털만화영상")
                .studentID(21)
                .gender(Gender.FEMALE)
                .image_url("")
                .introduction("외출 잦은 사람 선호합니다!")
                .type(DormitoryType.NEW)
                .open(true)
                .build();

        Member member7 = Member.builder()
                .nickname("피자먹고싶다")
                .email("7@sangmyung.kr")
                .password(encoder.encode("1234"))
                .major("한국언어문화")
                .studentID(20)
                .gender(Gender.FEMALE)
                .image_url("")
                .introduction("외출 잦은 사람 선호합니다!")
                .type(DormitoryType.NEW)
                .open(true)
                .build();

        Member member8 = Member.builder()
                .nickname("슴우D")
                .email("8@sangmyung.kr")
                .password(encoder.encode("1234"))
                .major("커뮤니케이션디자인")
                .studentID(19)
                .gender(Gender.MALE)
                .image_url("")
                .introduction("외출 잦은 사람 선호합니다!")
                .type(DormitoryType.NEW)
                .open(true)
                .build();

        Member member9 = Member.builder()
                .nickname("너무추워")
                .email("9@sangmyung.kr")
                .password(encoder.encode("1234"))
                .major("문화예술경영")
                .studentID(18)
                .gender(Gender.MALE)
                .image_url("")
                .introduction("외출 잦은 사람 선호합니다!")
                .type(DormitoryType.NEW)
                .open(false)
                .build();


        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);
        memberRepository.save(member6);
        memberRepository.save(member7);
        memberRepository.save(member8);
        memberRepository.save(member9);

        Detail detail1 = Detail.builder()
                .habit(true)
                .smoking(false)
                .ear(Ear.BRIGHT)
                .sleep(Time.EARLY)
                .wakeup(Time.RANDOM)
                .out(Out.OUT)
                .cleaning(Cleaning.CLEAN)
                .tendency(Tendency.ALONE)
                .member(member1)
                .build();

        Detail detail2 = Detail.builder()
                .habit(false)
                .smoking(true)
                .ear(Ear.BRIGHT)
                .sleep(Time.EARLY)
                .wakeup(Time.EARLY)
                .out(Out.OUT)
                .cleaning(Cleaning.CLEAN)
                .tendency(Tendency.TOGETHER)
                .member(member2)
                .build();

        Detail detail3 = Detail.builder()
                .habit(false)
                .smoking(true)
                .ear(Ear.BRIGHT)
                .sleep(Time.EARLY)
                .wakeup(Time.EARLY)
                .out(Out.OUT)
                .cleaning(Cleaning.CLEAN)
                .tendency(Tendency.TOGETHER)
                .member(member3)
                .build();

        Detail detail4 = Detail.builder()
                .habit(true)
                .smoking(false)
                .ear(Ear.DARK)
                .sleep(Time.EARLY)
                .wakeup(Time.RANDOM)
                .out(Out.OUT)
                .cleaning(Cleaning.CLEAN)
                .tendency(Tendency.ALONE)
                .member(member4)
                .build();

        Detail detail5 = Detail.builder()
                .habit(true)
                .smoking(false)
                .ear(Ear.BRIGHT)
                .sleep(Time.EARLY)
                .wakeup(Time.RANDOM)
                .out(Out.HOME)
                .cleaning(Cleaning.CLEAN)
                .tendency(Tendency.TOGETHER)
                .member(member5)
                .build();

        Detail detail6 = Detail.builder()
                .habit(false)
                .smoking(false)
                .ear(Ear.BRIGHT)
                .sleep(Time.EARLY)
                .wakeup(Time.RANDOM)
                .out(Out.OUT)
                .cleaning(Cleaning.DIRTY)
                .tendency(Tendency.TOGETHER)
                .member(member6)
                .build();

        Detail detail7 = Detail.builder()
                .habit(false)
                .smoking(true)
                .ear(Ear.DARK)
                .sleep(Time.LATE)
                .wakeup(Time.LATE)
                .out(Out.HOME)
                .cleaning(Cleaning.CLEAN)
                .tendency(Tendency.ALONE)
                .member(member7)
                .build();

        Detail detail8 = Detail.builder()
                .habit(true)
                .smoking(false)
                .ear(Ear.DARK)
                .sleep(Time.LATE)
                .wakeup(Time.LATE)
                .out(Out.OUT)
                .cleaning(Cleaning.CLEAN)
                .tendency(Tendency.ALONE)
                .member(member8)
                .build();

        Detail detail9 = Detail.builder()
                .habit(true)
                .smoking(false)
                .ear(Ear.BRIGHT)
                .sleep(Time.EARLY)
                .wakeup(Time.RANDOM)
                .out(Out.PROMISE)
                .cleaning(Cleaning.OPPONENT)
                .tendency(Tendency.OPPONENT)
                .member(member9)
                .build();

        detailRepository.save(detail1);
        detailRepository.save(detail2);
        detailRepository.save(detail3);
        detailRepository.save(detail4);
        detailRepository.save(detail5);
        detailRepository.save(detail6);
        detailRepository.save(detail7);
        detailRepository.save(detail8);
        detailRepository.save(detail9);

        acceptRoomMateRepository.save(new AcceptRoomMate(member1, member4));

        Post post1 = Post.builder()
                .commentCount(2)
                .anonymity(true)
                .title("피자 같이 먹을 사람 구해요1")
                .content("ㅈㄱㄴ")
                .like_count(10)
                .type(DormitoryType.NEW)
                .build();

        Post post2 = Post.builder()
                .commentCount(2)
                .anonymity(true)
                .title("피자 같이 먹을 사람 구해요2")
                .content("ㅈㄱㄴ")
                .like_count(5)
                .type(DormitoryType.NEW)
                .build();

        Post post3 = Post.builder()
                .commentCount(2)
                .anonymity(true)
                .title("피자 같이 먹을 사람 구해요3")
                .content("ㅈㄱㄴ")
                .like_count(5)
                .type(DormitoryType.NEW)
                .build();

        Post post4 = Post.builder()
                .commentCount(2)
                .anonymity(true)
                .title("피자 같이 먹을 사람 구해요4")
                .content("ㅈㄱㄴ")
                .like_count(5)
                .type(DormitoryType.NEW)
                .build();

        Post post5 = Post.builder()
                .commentCount(2)
                .anonymity(true)
                .title("피자 같이 먹을 사람 구해요5")
                .content("ㅈㄱㄴ")
                .like_count(5)
                .type(DormitoryType.NEW)
                .build();

        Post post6 = Post.builder()
                .commentCount(8)
                .anonymity(true)
                .title("피자 같이 먹을 사람 구해요6")
                .content("ㅈㄱㄴ")
                .like_count(8)
                .type(DormitoryType.NEW)
                .build();

        Post post7 = Post.builder()
                .commentCount(2)
                .anonymity(true)
                .title("피자 같이 먹을 사람 구해요7")
                .content("ㅈㄱㄴ")
                .like_count(5)
                .type(DormitoryType.NEW)
                .build();

        Post post8 = Post.builder()
                .commentCount(2)
                .anonymity(true)
                .title("피자 같이 먹을 사람 구해요8")
                .content("ㅈㄱㄴ")
                .like_count(5)
                .type(DormitoryType.NEW)
                .build();

        Post post9 = Post.builder()
                .commentCount(2)
                .anonymity(true)
                .title("피자 같이 먹을 사람 구해요9")
                .content("ㅈㄱㄴ")
                .like_count(8)
                .type(DormitoryType.NEW)
                .build();

        Post post10 = Post.builder()
                .commentCount(2)
                .anonymity(true)
                .title("피자 같이 먹을 사람 구해요10")
                .content("ㅈㄱㄴ")
                .like_count(5)
                .type(DormitoryType.NEW)
                .build();

        Post post11 = Post.builder()
                .commentCount(2)
                .anonymity(true)
                .title("피자 같이 먹을 사람 구해요11")
                .content("ㅈㄱㄴ")
                .like_count(5)
                .type(DormitoryType.NEW)
                .build();

        Post post12 = Post.builder()
                .commentCount(2)
                .anonymity(true)
                .title("피자 같이 먹을 사람 구해요12")
                .content("ㅈㄱㄴ")
                .like_count(5)
                .type(DormitoryType.NEW)
                .build();

        Post post13 = Post.builder()
                .commentCount(2)
                .anonymity(true)
                .title("피자 같이 먹을 사람 구해요13")
                .content("ㅈㄱㄴ")
                .like_count(5)
                .type(DormitoryType.NEW)
                .build();

        Post post14 = Post.builder()
                .commentCount(2)
                .anonymity(true)
                .title("피자 같이 먹을 사람 구해요14")
                .content("ㅈㄱㄴ")
                .like_count(5)
                .type(DormitoryType.NEW)
                .build();

        Post post15 = Post.builder()
                .commentCount(2)
                .anonymity(true)
                .title("피자 같이 먹을 사람 구해요15")
                .content("ㅈㄱㄴ")
                .like_count(5)
                .type(DormitoryType.NEW)
                .build();

        Post post16 = Post.builder()
                .commentCount(2)
                .anonymity(true)
                .title("피자 같이 먹을 사람 구해요16")
                .content("ㅈㄱㄴ")
                .like_count(5)
                .type(DormitoryType.NEW)
                .build();

        Post post17 = Post.builder()
                .commentCount(2)
                .anonymity(true)
                .title("피자 같이 먹을 사람 구해요17")
                .content("ㅈㄱㄴ")
                .like_count(5)
                .type(DormitoryType.NEW)
                .build();

        Post post18 = Post.builder()
                .commentCount(2)
                .anonymity(true)
                .title("피자 같이 먹을 사람 구해요오오오오오오오오오오오오오오오오오오오18")
                .content("ㅈㄱㄴ")
                .like_count(5)
                .type(DormitoryType.NEW)
                .build();

        post1.setMember(member1);
        post2.setMember(member1);
        post3.setMember(member1);
        post4.setMember(member1);
        post5.setMember(member1);
        post6.setMember(member1);
        post7.setMember(member1);
        post8.setMember(member1);
        post9.setMember(member1);
        post10.setMember(member1);
        post11.setMember(member1);
        post12.setMember(member1);
        post13.setMember(member1);
        post14.setMember(member1);
        post15.setMember(member1);
        post16.setMember(member1);
        post17.setMember(member1);
        post18.setMember(member1);

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);
        postRepository.save(post5);
        postRepository.save(post6);
        postRepository.save(post7);
        postRepository.save(post8);
        postRepository.save(post9);
        postRepository.save(post10);
        postRepository.save(post11);
        postRepository.save(post12);
        postRepository.save(post13);
        postRepository.save(post14);
        postRepository.save(post15);
        postRepository.save(post16);
        postRepository.save(post17);
        postRepository.save(post18);*/
    }
}
