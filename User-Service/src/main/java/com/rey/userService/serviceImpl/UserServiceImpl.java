package com.rey.userService.serviceImpl;

import com.rey.userService.dto.ErrorCodeEnum;
import com.rey.userService.dto.LoginRequestDto;
import com.rey.userService.dto.UserRequestDto;
import com.rey.userService.dto.UserResponseDto;
import com.rey.userService.entity.User;
import com.rey.userService.exception.UserExceptionHandler;
import com.rey.userService.repository.UserRepository;
import com.rey.userService.service.ServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements ServiceInterface {

    private final UserRepository userRepo;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authManager;
    private final JWTService jwtService;

    @Override
    public UserResponseDto register(UserRequestDto userRequestDto) {

        boolean usernameExist = userRepo.findByUsername(userRequestDto.getUsername().strip()).isPresent();
        log.info("Check if username is present: {}", usernameExist);

        if (usernameExist) {
            log.error("Username Already taken");
            throw new UserExceptionHandler(
                    ErrorCodeEnum.USERNAME_ALREADY_EXIST.getErrorCode(),
                    ErrorCodeEnum.USERNAME_ALREADY_EXIST.getErrorMessage(),
                    HttpStatus.CONFLICT);
        }

        boolean emailExist = userRepo.findByEmail(userRequestDto.getEmail().strip()).isPresent();
        log.info("Check if email is present: {}", emailExist);

        if (emailExist) {
            log.error("Email already exist");
            throw new UserExceptionHandler(
                 ErrorCodeEnum.EMAIL_ALREADY_TAKEN.getErrorCode(),
                 ErrorCodeEnum.EMAIL_ALREADY_TAKEN.getErrorMessage(),
                 HttpStatus.CONFLICT);
        }

        if (!userRequestDto.getPassword().equals(userRequestDto.getConfirmPassword())) {
            log.error("Password Mismatched");
            throw new UserExceptionHandler(
                    ErrorCodeEnum.PASSWORD_MISMATCH.getErrorCode(),
                    ErrorCodeEnum.PASSWORD_MISMATCH.getErrorMessage(),
                    HttpStatus.CONFLICT);
        }

        User user = modelMapper.map(userRequestDto, User.class);
        log.info("Mapped userRequestDto to User");

        User savedUser = userRepo.save(user);
        log.info("Saved user into the DB");

        UserResponseDto userResponse = modelMapper.map(savedUser, UserResponseDto.class);
        log.info("Mapped saved User to Response");

        return userResponse;
    }

    @Override
    public String loginUser(LoginRequestDto loginRequest) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));

        if (auth.isAuthenticated()) {
            log.info("Token released");
            return jwtService.generateToken(loginRequest.getUsername().strip());
        } else {
            log.error("Invalid Credentials");
            return "INVALID CREDENTIALS";
        }

    }
}
