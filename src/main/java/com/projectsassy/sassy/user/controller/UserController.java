package com.projectsassy.sassy.user.controller;

import com.projectsassy.sassy.common.code.SuccessCode;
import com.projectsassy.sassy.token.dto.TokenDto;
import com.projectsassy.sassy.token.dto.TokenRequest;
import com.projectsassy.sassy.user.domain.User;

import com.projectsassy.sassy.common.response.ApiResponse;
import com.projectsassy.sassy.user.dto.EmailRequest;
import com.projectsassy.sassy.user.dto.*;

import com.projectsassy.sassy.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUp(@Validated @RequestBody UserJoinDto joinDto) {
        userService.join(joinDto);
        return new ResponseEntity<>(new ApiResponse(SuccessCode.SIGNUP_SUCCESS), HttpStatus.OK);
    }


    @ApiOperation(value = "회원가입 시 아이디 중복 검사")
    @PostMapping("/signup/id")
    public ResponseEntity<ApiResponse> duplicateLoginId(@Validated @RequestBody DuplicateLoginIdDto duplicateLoginIdDto) {
        userService.duplicateLoginId(duplicateLoginIdDto);
        return new ResponseEntity<>(new ApiResponse(SuccessCode.CAN_USE_ID), HttpStatus.OK);
    }


    @ApiOperation(value = "회원가입 시 이메일 중복 검사")
    @PostMapping("/signup/email")
    public ResponseEntity<ApiResponse> duplicateEmail(@Validated @RequestBody DuplicateEmailDto duplicateEmailDto) {
        userService.duplicateEmail(duplicateEmailDto);
        return new ResponseEntity<>(new ApiResponse(SuccessCode.CAN_USE_EMAIL), HttpStatus.OK);
    }

    @ApiOperation(value = "아이디 찾기")
    @PostMapping("/find/id")
    public ResponseEntity<FindIdResponse> findId(@Validated @RequestBody FindIdRequest findIdRequest) {
        FindIdResponse findIdResponse = userService.findMyId(findIdRequest);

        return new ResponseEntity<>(findIdResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "비밀번호 찾기")
    @PostMapping("/find/password")
    public ResponseEntity<ApiResponse> findPassword(@Validated @RequestBody FindPasswordRequest findPasswordRequest) {
        userService.findMyPassword(findPasswordRequest);

        return new ResponseEntity<>(new ApiResponse(SuccessCode.CERTIFY_CODE), HttpStatus.OK);
    }

    @ApiOperation(value = "인증 코드 이메일 전송")
    @PostMapping("/email")
    public ResponseEntity<ApiResponse> authEmail(@Validated @RequestBody EmailRequest request) {
        userService.authEmail(request);
        return new ResponseEntity<>(new ApiResponse(SuccessCode.SEND_EMAIL), HttpStatus.OK);
    }

    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@Validated @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        TokenDto tokenDto = userService.login(loginRequest);

        ResponseCookie cookie1 = ResponseCookie.from("userCookie1", "userAuth1")
            .path("/")
            .httpOnly(true)
            .domain(".projectsassy.net")
            .maxAge(3000)
            .build();

        ResponseCookie cookie2 = ResponseCookie.from("userCookie2", "userAuth2")
            .path("/")
            .httpOnly(true)
            .domain("localhost")
            .maxAge(3000)
            .build();

        response.addHeader("Set-Cookie", cookie1.toString());
        response.addHeader("Set-Cookie", cookie2.toString());
        return new ResponseEntity<>(tokenDto, HttpStatus.OK);
    }

    @ApiOperation(value = "토큰 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequest tokenRequest) {
        TokenDto responseToken = userService.reissue(tokenRequest);
        return new ResponseEntity<>(responseToken, HttpStatus.OK);
    }

    @ApiOperation(value = "마이페이지 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable(value = ("userId")) Long userId) {
        UserProfileResponse userProfileResponse = userService.getProfile(userId);
        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "마이페이지 수정")
    @PatchMapping("/{userId}")
    public ResponseEntity<UpdateProfileResponse> updateProfile(@PathVariable(value = ("userId")) Long userId,
                                                               @Validated @RequestBody UpdateProfileRequest updateProfileRequest) {
        UpdateProfileResponse updateProfileResponse = userService.updateProfile(userId, updateProfileRequest);
        return new ResponseEntity<>(updateProfileResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "비밀번호 수정")
    @PatchMapping("/{userId}/password")
    public ResponseEntity updatePassword(@PathVariable(value = ("userId")) Long userId,
                                         @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        userService.updatePassword(userId, updatePasswordRequest);
        return new ResponseEntity<>(new ApiResponse(SuccessCode.UPDATE_PASSWORD), HttpStatus.OK);
    }

    @ApiOperation(value = "회원 삭제")
    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable(value = ("userId")) Long userId) {
        userService.delete(userId);
        return new ResponseEntity<>(new ApiResponse(SuccessCode.DELETE_USER), HttpStatus.OK);
    }


}
