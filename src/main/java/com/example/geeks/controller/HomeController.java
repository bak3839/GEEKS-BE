package com.example.geeks.controller;

import com.example.geeks.Security.Util;
import com.example.geeks.responseDto.HomeMainDTo;
import com.example.geeks.responseDto.SearchMemberDTO;
import com.example.geeks.responseDto.SearchPostCursorDTO;
import com.example.geeks.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {
    private final Util util;
    @Value("${jwt.secret}")
    private String tokenSecretKey;

    private final PointService pointService;

    private final PostService postService;

    private final DetailService detailService;

    private final MemberService memberService;

    private final RoomMateService roomMateService;

    @GetMapping("/healthy")
    public String healthyCheck() {
        return ".";
    }

    @GetMapping("/main")
    public HomeMainDTo home(@CookieValue String token) {
        Long userId = util.getUserId(token, tokenSecretKey);
        String nickname = util.getNickname(token, tokenSecretKey);

        return new HomeMainDTo(
                nickname,
                detailService.detailExist(userId),
                roomMateService.applyRoommate(userId),
                pointService.homePointList(userId),
                postService.homeLivePost(),
                postService.homeWeeklyPost());
    }

    @GetMapping("/search/post")
    public SearchPostCursorDTO searchPost(@RequestParam String keyword,
                                      @RequestParam Long cursor) {
        return postService.searchPost(cursor, keyword);
    }

    @GetMapping("/search/member")
    public List<SearchMemberDTO> searchMember(@RequestParam String keyword,
                                              @CookieValue String token) {
        Long userId = util.getUserId(token, tokenSecretKey);
        return memberService.searchMember(userId, keyword);
    }
}
