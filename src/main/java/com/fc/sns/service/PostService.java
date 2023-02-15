package com.fc.sns.service;

import com.fc.sns.exception.ErrorCode;
import com.fc.sns.exception.SnsApplicationException;
import com.fc.sns.model.Post;
import com.fc.sns.model.entity.PostEntity;
import com.fc.sns.model.entity.UserEntity;
import com.fc.sns.repository.PostEntityRepository;
import com.fc.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;

    @Transactional
    public void create(String title, String body, String userName) {
        UserEntity user = userEntityRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is not found", userName)));
        postEntityRepository.save(PostEntity.of(title, body, user));
    }

    @Transactional
    public Post modify(String title, String body, String userName, Integer postId) {
        UserEntity user = userEntityRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is not found", userName)));

        //post exist
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s post not found", postId)));

        //post permission
        if (postEntity.getUser() != user) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has not permission with %s", userName, postId));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);

        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }

    @Transactional
    public void delete(String userName, Integer postId) {
        //작성한 사용자가 존재하지 않는 경우 오류
        userEntityRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is not found", userName)));

        //삭제하려는 포스트가 존재하지 않는 경우 오류
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s of post not found", postId)));

        if (userName.equals(postEntity.getUser().getUserName())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has not permission with %s", userName, postId));
        }

        //삭제 처리
        postEntityRepository.deleteById(postId);
    }
}
