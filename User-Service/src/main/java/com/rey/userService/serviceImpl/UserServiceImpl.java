package com.rey.userService.serviceImpl;

import com.rey.userService.dto.ErrorCodeEnum;
import com.rey.userService.dto.LoginRequestDto;
import com.rey.userService.dto.UserRequestDto;
import com.rey.userService.dto.UserResponseDto;
import com.rey.userService.entity.Role;
import com.rey.userService.entity.User;
import com.rey.userService.exception.UserExceptionHandler;
import com.rey.userService.helper.FileUploadHelper;
import com.rey.userService.repository.UserRepository;
import com.rey.userService.service.ServiceInterface;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements ServiceInterface {

    private final UserRepository userRepo;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authManager;
    private final JWTService jwtService;
    private final FileUploadHelper fileUploadHelper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDto register(UserRequestDto userRequestDto) {
        User user = new User();
        boolean usernameExist = userRepo.findByUsername(userRequestDto.getUsername().strip()).isPresent();
        log.info("Check if username is present: {}", usernameExist);

        if (usernameExist) {
            log.error("Username Already taken");
            throw new UserExceptionHandler(
                    ErrorCodeEnum.USERNAME_ALREADY_EXIST.getErrorCode(),
                    ErrorCodeEnum.USERNAME_ALREADY_EXIST.getErrorMessage(),
                    HttpStatus.CONFLICT);
        }
        user.setUsername(userRequestDto.getUsername().strip());

        boolean emailExist = userRepo.findByEmail(userRequestDto.getEmail().strip()).isPresent();
        log.info("Check if email is present: {}", emailExist);

        if (emailExist) {
            log.error("Email already exist");
            throw new UserExceptionHandler(
                 ErrorCodeEnum.EMAIL_ALREADY_TAKEN.getErrorCode(),
                 ErrorCodeEnum.EMAIL_ALREADY_TAKEN.getErrorMessage(),
                 HttpStatus.CONFLICT);
        }

        user.setEmail(userRequestDto.getEmail().strip());



        if (!userRequestDto.getPassword().equals(userRequestDto.getConfirmPassword())) {
            log.error("Password Mismatched");
            throw new UserExceptionHandler(
                    ErrorCodeEnum.PASSWORD_MISMATCH.getErrorCode(),
                    ErrorCodeEnum.PASSWORD_MISMATCH.getErrorMessage(),
                    HttpStatus.CONFLICT);
        }

        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setRole(Role.USER);
        user.setDateOfBirth(userRequestDto.getDateOfBirth());
        user.setFirstName(userRequestDto.getFirstName().strip());
        user.setLastName(userRequestDto.getLastName().strip());
        user.setPhoneNumber(userRequestDto.getPhoneNumber().strip());



        User savedUser = userRepo.save(user);
        log.info("Saved user into the DB");

        UserResponseDto userResponse = modelMapper.map(savedUser, UserResponseDto.class);
        log.info("Mapped saved User to Response");

        return userResponse;
    }


    @Override
    public void uploadProfile(Long id, MultipartFile file) throws IOException {
        User user = userRepo.findById(id)
                .orElseThrow(()-> new UserExceptionHandler(
                        ErrorCodeEnum.USERNAME_ALREADY_EXIST.getErrorCode(),
                        ErrorCodeEnum.USERNAME_ALREADY_EXIST.getErrorMessage(),
                        HttpStatus.CONFLICT));

        String fileUrl = fileUploadHelper.uploadProfilePicture(file);
        log.info("Uploaded the picture to the directory");

        user.setProfilePictureUrl(fileUrl);
        log.info("Picture Url successfully saved in the database");

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

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        Page<User> userPage = userRepo.findAll(pageable);
        log.info("Gotten all users from the database");
        Page<UserResponseDto> userResponseDto =userPage
                .map(user -> modelMapper.map(user, UserResponseDto.class));
        log.info("Mapped user into UserResponseDto");
        return userResponseDto;
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(()-> new UserExceptionHandler(
                        ErrorCodeEnum.USER_NOT_FOUND.getErrorCode(),
                        ErrorCodeEnum.USER_NOT_FOUND.getErrorMessage(),
                        HttpStatus.CONFLICT));
        log.info("User found with id: {}",user.getId());
        UserResponseDto userResponse = modelMapper.map(user, UserResponseDto.class);
        log.info("Mapped user to userResponse: {}",userResponse);
        return userResponse;
    }

    @Override
    public UserResponseDto revokeAdminRole(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(()-> new UserExceptionHandler(
                        ErrorCodeEnum.USERNAME_ALREADY_EXIST.getErrorCode(),
                        ErrorCodeEnum.USERNAME_ALREADY_EXIST.getErrorMessage(),
                        HttpStatus.CONFLICT));
        log.info("User id: {} found",user.getId());

        if (Role.USER.equals(user.getRole())){
            log.error("User already has User privileges");
            throw new UserExceptionHandler(
                    ErrorCodeEnum.USER_ALREADY_HAVE_USER_PRIVILEGES.getErrorCode(),
                    ErrorCodeEnum.USER_ALREADY_HAVE_USER_PRIVILEGES.getErrorMessage(),
                    HttpStatus.CONFLICT);
        }

        user.setRole(Role.USER);
        log.info("Role revoked to User");

        User savedUser = userRepo.save(user);
        log.info("Role saved to user");

        UserResponseDto userResponse = modelMapper.map(savedUser, UserResponseDto.class);
        log.info("Saved User into UserResponse");
        return userResponse;
    }

    @Override
    public UserResponseDto assignAdminRole(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(()-> new UserExceptionHandler(
                        ErrorCodeEnum.USERNAME_ALREADY_EXIST.getErrorCode(),
                        ErrorCodeEnum.USERNAME_ALREADY_EXIST.getErrorMessage(),
                        HttpStatus.CONFLICT));
        log.info("User with id: {} found",user.getId());

        if (Role.ADMIN.equals(user.getRole())) {
            log.error("User already has admin privileges");
            throw new UserExceptionHandler(
                    ErrorCodeEnum.USER_ALREADY_HAVE_ADMIN_PRIVILEGES.getErrorCode(),
                    ErrorCodeEnum.USER_ALREADY_HAVE_ADMIN_PRIVILEGES.getErrorMessage(),
                    HttpStatus.CONFLICT);
        }

        user.setRole(Role.ADMIN);
        log.info("Role set to Admin");

        User savedUser = userRepo.save(user);
        log.info("Role assigned successfully");

        UserResponseDto userResponse = modelMapper.map(savedUser, UserResponseDto.class);
        log.info("Mapped saved user into response dto");
        return userResponse;
    }



}
