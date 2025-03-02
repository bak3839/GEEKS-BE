package com.example.geeks.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.geeks.domain.*;
import com.example.geeks.repository.*;
import com.example.geeks.requestDto.ModifyCommentDTO;
import com.example.geeks.requestDto.PostCommentRequestDTO;
import com.example.geeks.requestDto.PostCreateRequestDTO;
import com.example.geeks.responseDto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final PhotoRepository photoRepository;

    private final MemberRepository memberRepository;

    private final CommentRepository commentRepository;

    private final HeartRepository heartRepository;

    private final PostScrapRepository postScrapRepository;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

//    public String  uploadToS3(MultipartFile file, String nickname, int count) throws IOException {
//
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//        String current_date = now.format(dateTimeFormatter);
//
//        String fileName = nickname + current_date + count++;
//
//        ObjectMetadata metadata = new ObjectMetadata();
//
//        metadata.setContentLength(file.getSize());
//        metadata.setContentType(file.getContentType());
//
//        amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);
//        System.out.println("사진 URL" + amazonS3.getUrl(bucket, fileName));
//
//        return fileName;
//    }

    public List<String>  uploadToS32(List<MultipartFile> files, String nickname) {
        List<String> fileNames = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); // 최적의 스레드 풀 크기

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String current_date = now.format(dateTimeFormatter);

        try {
            AtomicInteger count = new AtomicInteger(1); // 고유한 파일 번호를 생성하기 위한 AtomicInteger

            CompletableFuture<?>[] futures = files.stream()
                    .map(file -> CompletableFuture.runAsync(() -> {
                        try {
                            String fileName = nickname + current_date + count.getAndIncrement();

                            ObjectMetadata metadata = new ObjectMetadata();

                            metadata.setContentLength(file.getSize());
                            metadata.setContentType(file.getContentType());

                            amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);
                            System.out.println("사진 URL" + amazonS3.getUrl(bucket, fileName));
                            fileNames.add(fileName);
                        } catch (IOException e) {
                            // 에러 처리
                        }
                    }, executor))
                    .toArray(CompletableFuture[]::new);

            CompletableFuture.allOf(futures).join(); // 모든 작업이 완료될 때까지 대기
        } finally {
            executor.shutdown(); // ExecutorService 종료
        }

        return fileNames;
    }

    @Transactional
    public void createPost(Long memberId, PostCreateRequestDTO requestDTO, List<MultipartFile> files) throws IOException {
        Member member = memberRepository.findByIdFetchDetail(memberId)
                .orElseThrow(() -> new NotFoundException("Could not found id : " + memberId));

        Post post = Post.builder()
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .anonymity(requestDTO.isAnonymity())
                .commentCount(0)
                .like_count(0)
                .type(member.getType())
                .build();
        post.setMember(member);

        postRepository.save(post);


        if (files != null) {
            List<String> fileNames = uploadToS32(files, member.getNickname());
            post.setPhotoName(fileNames.get(0));

            for (String fileName : fileNames) {
                Photo photo = Photo.createPhoto(fileName, post);
                photoRepository.save(photo);
            }
        }
    }

    public PostCursorPageDTO cursorBasePaging(Long cursor) {
        PageRequest pageRequest =
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

        Page<Post> pages;

        if(cursor == 0L) {
            pages = postRepository.findPostCursorBasePagingFirst(pageRequest);
        } else {
            pages = postRepository.findPostCursorBasePaging(cursor, pageRequest);
        }

        List<PostAllDTO> postAllDTOS = pages.map(post ->
                new PostAllDTO(
                        post.getId(), post.getTitle(), post.getContent(),
                        post.isAnonymity() ? null : post.getMember().getNickname(), post.getPhotoName(),
                        post.getCommentCount(), post.getLike_count(), post.isAnonymity(), post.getCreatedDate())).stream().toList();

        if(postAllDTOS.isEmpty()) {
            return new PostCursorPageDTO(0L, pages.hasNext(), postAllDTOS);
        }

        return new PostCursorPageDTO(postAllDTOS.get(postAllDTOS.size() - 1).getPostId(), pages.hasNext(), postAllDTOS);
    }

    public List<HomeRealTimePostDTO> homeLivePost() {
        LocalDateTime time = LocalDateTime.now().minusHours(12);
        PageRequest pageRequest = PageRequest.of(0, 3);
        List<Post> posts = postRepository.findPostToHome(time, pageRequest);

        return posts.stream().map(post ->
                HomeRealTimePostDTO.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .likeCount(post.getLike_count())
                        .commentCount(post.getCommentCount())
                        .createdDate(post.getCreatedDate()).build()).toList();
    }

    public List<HomeRealTimePostDTO> homeWeeklyPost() {
        LocalDateTime time = LocalDateTime.now().minusHours(168);
        PageRequest pageRequest = PageRequest.of(0, 3);
        List<Post> posts = postRepository.findPostToHome(time, pageRequest);

        return posts.stream().map(post ->
                HomeRealTimePostDTO.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .likeCount(post.getLike_count())
                        .commentCount(post.getCommentCount())
                        .createdDate(post.getCreatedDate()).build()).toList();
    }

    public PostDetailDTO findDetailPost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Could not found id : " + postId));

        List<PostCommentResponseDTO> comments = commentRepository.findByPostId(postId, userId);
        List<String> photoNames = photoRepository.findPhotoNamesByPostId(postId);

        Optional<Heart> heart = heartRepository.findByMemberIdAndPostId(userId, postId);
        Optional<PostScrap> postScrap = postScrapRepository.findByMemberIdAndPostId(userId, postId);

        return PostDetailDTO.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .comments(comments)
                .likeCount(post.getLike_count())
                .commentCount(post.getCommentCount())
                .photoNames(photoNames)
                .createdDate(post.getCreatedDate())
                .writer(post.isAnonymity() ? null : post.getMember().getNickname())
                .writerState(post.getMember().getId().equals(userId))
                .scrapState(postScrap.isPresent())
                .heartState(heart.isPresent())
                .build();
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Could not found id : " + postId));

        List<Photo> photos = post.getPhotos();

        for (Photo photo : photos) {
            amazonS3.deleteObject(bucket, photo.getPhotoName());
        }

        postRepository.delete(post);
    }

    @Transactional
    public void createComment(Long memberId, PostCommentRequestDTO requestDTO) {
        Member member = memberRepository.findByIdFetchDetail(memberId)
                .orElseThrow(() -> new NotFoundException("Could not found member id : " + memberId));

        Post post = postRepository.findById(requestDTO.getPostId())
                .orElseThrow(() -> new NotFoundException("Could not found post id : " + requestDTO.getPostId()));

        Comment comment = new Comment(requestDTO.getContent(), false, requestDTO.isAnonymity());
        comment.setMember(member);
        comment.setPost(post);

        Comment parentComment;

        if (requestDTO.getParentId() != null) {
            parentComment = commentRepository.findById(requestDTO.getParentId())
                    .orElseThrow(() -> new NotFoundException("Could not found parent id : " + requestDTO.getParentId()));
            comment.setParent(parentComment);
        }

        commentRepository.save(comment);

        if(comment.getId() != null) {
            post.increaseCommentCount(post.getCommentCount() + 1);
        }
    }

    @Transactional
    public void modifyComment(ModifyCommentDTO dto) {
        Comment comment = commentRepository.findById(dto.getCommentId())
                .orElseThrow(() -> new NotFoundException("Could not found id : " + dto.getCommentId()));

        comment.setContent(dto.getContent());
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Could not found id : " + commentId));

        comment.setDeleted(true);
    }

    @Transactional
    public void insertHeart(Long userId, Long postId) throws Exception {
        Member member = memberRepository.findByIdFetchDetail(userId)
                .orElseThrow(() -> new NotFoundException("Could not found id : " + userId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Could not found id : " + postId));

        if(heartRepository.findByMemberAndPost(member, post).isPresent()) {
            throw new Exception();
        }

        Heart heart = new Heart();
        heart.setPost(post);
        heart.setMember(member);

        heartRepository.save(heart);
        postRepository.increaseHeart(postId);
    }

    @Transactional
    public void deleteHeart(Long userId, Long postId) {
        Member member = memberRepository.findByIdFetchDetail(userId)
                .orElseThrow(() -> new NotFoundException("Could not found id : " + userId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Could not found id : " + postId));

        Heart heart = heartRepository.findByMemberAndPost(member, post)
                .orElseThrow(() -> new NotFoundException("Could not found heart"));

        member.getHearts().remove(heart);
        postRepository.decreaseHeart(postId);
        heartRepository.delete(heart);
    }
    
    @Transactional
    public void insertScrap(Long userId, Long postId) throws Exception {
        Member member = memberRepository.findByIdFetchDetail(userId)
                .orElseThrow(() -> new NotFoundException("Could not found id : " + userId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Could not found id : " + postId));

        if(postScrapRepository.findByMemberAndPost(member, post).isPresent()) {
            throw new Exception();
        }

        PostScrap postScrap = new PostScrap();
        postScrap.setPost(post);
        postScrap.setMember(member);

        postScrapRepository.save(postScrap);
    }

    @Transactional
    public void deleteScrap(Long userId, Long postId) {
        Member member = memberRepository.findByIdFetchDetail(userId)
                .orElseThrow(() -> new NotFoundException("Could not found id : " + userId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Could not found id : " + postId));

        PostScrap postScrap = postScrapRepository.findByMemberAndPost(member, post)
                .orElseThrow(() -> new NotFoundException("Could not found scrap"));

        member.getScraps().remove(postScrap);
        postScrapRepository.delete(postScrap);
    }

    public List<PostHistoryDTO> scrapList(Long userId) {
        List<PostScrap> posts = postScrapRepository.findPostUserScraps(userId);

        return posts.stream().map(post ->
                PostHistoryDTO.builder()
                        .title(post.getPost().getTitle())
                        .content(post.getPost().getContent())
                        .commentCount(post.getPost().getCommentCount())
                        .postId(post.getPost().getId())
                        .likeCount(post.getPost().getLike_count())
                        .createdDate(post.getPost().getCreatedDate()).build()).toList();
    }

    public CommunityHistoryDTO postList(Long userId) {
        List<Post> postHistory = postRepository.findPostHistory(userId);
        List<Comment> commentHistory = commentRepository.findCommentHistory(userId);

        List<PostHistoryDTO> postHistoryDTOS = postHistory.stream().map(post ->
                PostHistoryDTO.builder()
                        .title(post.getTitle())
                        .content(post.getContent())
                        .commentCount(post.getCommentCount())
                        .postId(post.getId())
                        .likeCount(post.getLike_count())
                        .createdDate(post.getCreatedDate()).build()).toList();

        List<CommentHistoryDTO> commentHistoryDTOS = commentHistory.stream().map(post ->
                CommentHistoryDTO.builder()
                        .title(post.getPost().getTitle())
                        .commentCount(post.getPost().getCommentCount())
                        .content(post.getContent())
                        .postId(post.getPost().getId())
                        .createdDate(post.getPost().getCreatedDate())
                        .likeCount(post.getPost().getLike_count()).build()).toList();

        return new CommunityHistoryDTO(postHistoryDTOS, commentHistoryDTOS);
    }

    @Transactional
    public void deletePostAndCommentAndHeartsAndPostScraps(Long id) {
        commentRepository.deleteByMemberId(id);
        heartRepository.deleteByMemberId(id);
        postScrapRepository.deleteByMemberId(id);
        postRepository.deleteByMemberId(id);
    }

    public SearchPostCursorDTO searchPost(Long cursor, String keyword) {
        PageRequest pageRequest =
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

        Page<Post> pages;

        if(cursor == 0L) {
            pages = postRepository.SearchCursorBasePagingFirst("%" + keyword + "%", pageRequest);
        }else {
            pages = postRepository.SearchCursorBasePaging(cursor, "%" + keyword + "%", pageRequest);
        }

        List<SearchPostDTO> posts = pages.map(post ->
                new SearchPostDTO(post.getId(), post.getTitle(),
                        post.getContent(), post.getCreatedDate())).stream().toList();

        if(posts.isEmpty()) {
            return new SearchPostCursorDTO(0L, pages.hasNext(), posts);
        }

        return new SearchPostCursorDTO(posts.get(posts.size() - 1).getPostId(), pages.hasNext(), posts);
    }
}
