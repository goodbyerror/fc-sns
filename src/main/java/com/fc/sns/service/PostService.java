package com.fc.sns.service;

import com.fc.sns.controller.request.PostCommentRequest;
import com.fc.sns.exception.ErrorCode;
import com.fc.sns.exception.SnsApplicationException;
import com.fc.sns.model.Post;
import com.fc.sns.model.entity.LikeEntity;
import com.fc.sns.model.entity.PostEntity;
import com.fc.sns.model.entity.UserEntity;
import com.fc.sns.repository.LikeEntityRepository;
import com.fc.sns.repository.PostEntityRepository;
import com.fc.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LikeEntityRepository likeEntityRepository;

    @Transactional
    public void create(String title, String body, String userName) {
        UserEntity userEntity = getUserOrException(userName);
        postEntityRepository.save(PostEntity.of(title, body, userEntity));
    }

    @Transactional
    public Post modify(String title, String body, String userName, Integer postId) {
        // user exist check
        UserEntity userEntity = getUserOrException(userName);

        //post exist check
        PostEntity postEntity = getPostOrException(postId);

        //post permission
        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has not permission with %s", userName, postId));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);

        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }

    @Transactional
    public void delete(String userName, Integer postId) {
        //작성한 사용자가 존재하지 않는 경우 오류
        UserEntity userEntity = getUserOrException(userName);

        //삭제하려는 포스트가 존재하지 않는 경우 오류
        PostEntity postEntity = getPostOrException(postId);

        // post permission
        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName, postId));
        }

        //삭제 처리
        postEntityRepository.delete(postEntity);
    }

    public Page<Post> list(Pageable pageable) {

        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> myList(Pageable pageable, String userName) {
        UserEntity userEntity = getUserOrException(userName);

        return postEntityRepository.findAllByUser(userEntity, pageable).map(Post::fromEntity);
    }

    @Transactional
    public void like(Integer postId, String userName) {
        PostEntity postEntity = getPostOrException(postId);
        UserEntity userEntity = getUserOrException(userName);

        likeEntityRepository.findByUserAndPost(userEntity, postEntity).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.ALREADY_LIKED, String.format("User already liked the post"));
        });

        likeEntityRepository.save(LikeEntity.of(userEntity, postEntity));
    }

    public int likesCount(Integer postId) {
        PostEntity postEntity = getPostOrException(postId);

        return likeEntityRepository.countByPost(postEntity);
    }

    public void comment(Integer postId, PostCommentRequest postCommentRequest, String userName) {
        PostEntity postEntity = getPostOrException(postId);
        UserEntity userEntity = getUserOrException(userName);

    }

    private PostEntity getPostOrException(Integer postId) {
        return postEntityRepository.findById(postId)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("post not found")));
    }

    private UserEntity getUserOrException(String userName) {
        return userEntityRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("user not found")));
    }
}
