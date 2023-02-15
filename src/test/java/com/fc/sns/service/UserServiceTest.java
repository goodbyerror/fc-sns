package com.fc.sns.service;

import com.fc.sns.exception.ErrorCode;
import com.fc.sns.exception.SnsApplicationException;
import com.fc.sns.fixture.UserEntityFixture;
import com.fc.sns.model.entity.UserEntity;
import com.fc.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void join() {
        //given
        String userName = "username";
        String password = "password";

        //when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(UserEntityFixture.get(userName, password, 1));

        //then
        Assertions.assertDoesNotThrow(() -> userService.join(userName, password));
    }

    @Test
    void 회원가입시_userName으로_회원가입한_유저가_이미_있는경우() {
        //given
        String userName = "username";
        String password = "password";
        UserEntity fixture = UserEntityFixture.get(userName, password, 1);

        //when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        when(bCryptPasswordEncoder.encode(password)).thenReturn("encoded_password");
        when(userEntityRepository.save(any())).thenReturn(UserEntityFixture.get(userName, password, 1));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> userService.join(userName, password));
        Assertions.assertEquals(e.getErrorCode(), ErrorCode.DUPLICATED_USER_NAME);
    }

    @Test
    void login_성공하는_경우() {
        //given
        String userName = "username";
        String password = "password";
        UserEntity fixture = UserEntityFixture.get(userName, password, 1);

        //when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        when(bCryptPasswordEncoder.matches(password, fixture.getPassword())).thenReturn(true);

        //then
        Assertions.assertDoesNotThrow(() -> userService.login(userName, password));


    }

    @Test
    void login_회원가입이_안되어있는_사용자가_로그인을_시도하는_경우() {
        //given
        String userName = "username";
        String password = "password";
        UserEntity fixture = UserEntityFixture.get(userName, password, 1);

        //when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());

        //then
        Assertions.assertThrows(SnsApplicationException.class, () -> userService.login(userName, password));
    }

    @Test
    void login_패스워드가_틀린_경우() {
        //given
        String userName = "username";
        String password = "password";
        UserEntity fixture = UserEntityFixture.get(userName, password, 1);

        //when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
    }


}
