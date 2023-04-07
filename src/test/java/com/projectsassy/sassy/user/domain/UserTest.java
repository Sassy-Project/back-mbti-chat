package com.projectsassy.sassy.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Test
    @DisplayName("비밀번호 인코딩")
    public void encodePassword() throws Exception {
        //given
        User user = User.of("qwer1234", "1q2w3e", "haha", "qwer@naver.com", "man", "enfp", "image");

        //when
        String encodedPassword = encoder().encode(user.getPassword());
        user.encodingPassword(encodedPassword);

        //then
        assertThat("1q2w3ee").isNotEqualTo(user.getPassword());
    }

}