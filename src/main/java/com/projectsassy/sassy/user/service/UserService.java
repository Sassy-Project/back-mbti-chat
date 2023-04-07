package com.projectsassy.sassy.user.service;

import com.projectsassy.sassy.exception.code.ErrorCode;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.dto.DuplicateEmailDto;
import com.projectsassy.sassy.user.dto.DuplicateLoginIdDto;
import com.projectsassy.sassy.user.dto.UserJoinDto;
import com.projectsassy.sassy.exception.user.DuplicatedException;
import com.projectsassy.sassy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public void join(UserJoinDto joinDto) {
        User user = joinDto.toEntity();

        // 패스워드 인코딩
        user.encodingPassword(encoder.encode(joinDto.getPassword()));
        userRepository.save(user);

    }

    //아이디 중복검사
    public void duplicateLoginId(DuplicateLoginIdDto duplicateLoginIdDto) {
        userRepository.findByLoginId(duplicateLoginIdDto.getLoginId())
                .ifPresent(d -> {
                        throw new DuplicatedException(ErrorCode.DUPLICATE_LOGIN_ID);
                });
    }

    //이메일 중복검사
    public void duplicateEmail(DuplicateEmailDto duplicateEmailDto) {
        userRepository.findByEmail(duplicateEmailDto.getEmail())
                .ifPresent(d -> {
                    throw new DuplicatedException(ErrorCode.DUPLICATE_EMAIL);
                });
    }
}
