package com.example.controller;

import com.example.common.ResponseResult;
import com.example.dto.LoginRequest;
import com.example.dto.LoginResponse;
import com.example.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "认证授权", description = "用户登录、退出、令牌管理接口")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    @Operation(
            summary = "用户登录",
            description = "使用用户名和密码登录系统，成功返回JWT Token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登录成功",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseResult.class),
                                    examples = @ExampleObject(
                                            value = """
                        {
                          "code": 200,
                          "msg": "登录成功",
                          "data": {
                            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                            "tokenType": "Bearer",
                            "expiresIn": 86400
                          }
                        }
                        """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "用户名或密码错误",
                            content = @Content(
                                    examples = @ExampleObject(
                                            value = """
                        {
                          "code": 401,
                          "msg": "用户名或密码错误",
                          "data": null
                        }
                        """
                                    )
                            )
                    )
            }
    )
    public ResponseResult<LoginResponse> login(
            @Parameter(description = "登录请求参数", required = true)
            @Valid @RequestBody LoginRequest loginRequest) {

        // 进行身份认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成JWT token
        String jwt = jwtUtils.generateToken(loginRequest.getUsername());

        LoginResponse response = new LoginResponse();
        response.setToken(jwt);

        return ResponseResult.success("登录成功", response);
    }

    @GetMapping("/test")
    @Operation(summary = "测试接口", description = "用于测试JWT认证是否正常工作")
    public ResponseResult<String> testAuth() {
        return ResponseResult.success("认证测试成功");
    }
}