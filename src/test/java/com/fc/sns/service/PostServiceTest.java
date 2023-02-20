package com.fc.sns.service;

import com.fc.sns.exception.ErrorCode;
import com.fc.sns.exception.SnsApplicationException;
import com.fc.sns.fixture.PostEntityFixture;
import com.fc.sns.fixture.UserEntityFixture;
import com.fc.sns.model.entity.PostEntity;
import com.fc.sns.model.entity.UserEntity;
import com.fc.sns.repository.PostEntityRepository;
import com.fc.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostEntityRepository postEntityRepository;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @Test
    void 포스트작성이_성공한경우() {
        //given
        String title = "title";
        String body = "text";
        String userName = "userName";

        //when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        //then
        Assertions.assertDoesNotThrow(() -> postService.create(title, body, userName));

    }

    @Test
    void 포스트작성시_유저가존재하지않는경우() {
        String title = "title";
        String body = "body";
        String userName = "userName";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.create(title, body, userName));
        Assertions.assertEquals(e.getErrorCode(), ErrorCode.USER_NOT_FOUND);
    }

    @Test
    void 포스트_수정이_성공한_경우() {
        String title = "title";
        String body = "body";
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(userName, postId, 1);
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(postEntityRepository.saveAndFlush(any())).thenReturn(postEntity);

        Assertions.assertDoesNotThrow(() -> postService.modify(title, body, userName, postId));

    }

    @Test
    void 포스트_수정을_할때_포스트가_존재하지_않는_경우() {
        PostEntity postEntity = PostEntityFixture.get("userName", 1, 1);
        UserEntity userEntity = postEntity.getUser();

        //when
        when(userEntityRepository.findByUserName("userName")).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(1)).thenReturn(Optional.empty());

        //then
        Assertions.assertThrows(SnsApplicationException.class,
                () -> postService.modify("title", "body", "userName", 1));
    }

    @Test
    void 포스트수정시권한이없는경우() {
        PostEntity postEntity = PostEntityFixture.get("userName", 1, 1);
        UserEntity userEntity = postEntity.getUser();
        PostEntity postEntity1 = PostEntityFixture.get("userName1", 1, 1);

        when(userEntityRepository.findByUserName("userName")).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(1)).thenReturn(Optional.of(postEntity1));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.modify("title", "body", "userName", 1));

        Assertions.assertEquals(e.getErrorCode(), ErrorCode.INVALID_PERMISSION);

    }

    @Test
    void 포스트_삭제_정상() {
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(userName, postId, 1);
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        Assertions.assertDoesNotThrow(() -> postService.delete(userName, 1));

    }

    @Test
    void 포스트_삭제시_로그인_하지_않은_경우() {
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(userName, postId, 1);
        UserEntity writer = UserEntityFixture.get("userName1", "password", 2);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.delete(userName, 1));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());

    }

    @Test
    void 포스트_삭제시_작성자가_다른_경우() {
        PostEntity postEntity = PostEntityFixture.get("userName", 1, 1);
        UserEntity userEntity = postEntity.getUser();
        PostEntity postEntity1 = PostEntityFixture.get("userName1", 1, 1);
        UserEntity writer = postEntity1.getUser();

        when(userEntityRepository.findByUserName("userName")).thenReturn(Optional.of(writer));
        when(postEntityRepository.findById(1)).thenReturn(Optional.of(postEntity));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.delete("userName", 1));

        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 포스트_삭제시_포스트가_존재하지_않는_경우() {
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(userName, postId, 1);
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.delete(userName, 1));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 피드_목록_조회() {
        //given
        Pageable pageable = mock(Pageable.class);
        when(postEntityRepository.findAll(pageable)).thenReturn(Page.empty());

        Assertions.assertDoesNotThrow(() -> postService.list(pageable));
    }

    @Test
    void 내피드_목록_조회() {

        Pageable pageable = mock(Pageable.class);
        UserEntity user = mock(UserEntity.class);
        when(userEntityRepository.findByUserName(any())).thenReturn(Optional.of(user));
        when(postEntityRepository.findAllByUser(user, pageable)).thenReturn(Page.empty());

        Assertions.assertDoesNotThrow(() -> postService.myList(pageable, "userName"));

    }
}
