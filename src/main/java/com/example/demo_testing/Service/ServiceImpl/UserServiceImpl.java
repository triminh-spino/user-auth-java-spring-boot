package com.example.demo_testing.Service.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo_testing.Entity.Token;
import com.example.demo_testing.Entity.User;
import com.example.demo_testing.Enum.Role;
import com.example.demo_testing.Enum.TokenType;
import com.example.demo_testing.Exception.ConflictException;
import com.example.demo_testing.Exception.NotFoundException;
import com.example.demo_testing.Mapper.UserMapper;
import com.example.demo_testing.Model.ApiResponse;
import com.example.demo_testing.Model.LoginRequest;
import com.example.demo_testing.Model.NameRequest;
import com.example.demo_testing.Model.RegisterRequest;
import com.example.demo_testing.Model.TokenResponse;
import com.example.demo_testing.Model.UserDTO;
import com.example.demo_testing.Repository.TokenRepository;
import com.example.demo_testing.Repository.UserRepository;
import com.example.demo_testing.Service.JwtService;
import com.example.demo_testing.Service.UserService;
import com.example.demo_testing.Util.Response;

@Service
public class UserServiceImpl implements UserService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(
        JwtService jwtService,
        AuthenticationManager authenticationManager,
        UserDetailsService userDetailsService,
        TokenRepository tokenRepository,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        UserMapper userMapper
    ) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    private void saveUserToken(User user, String jwt, TokenType type) {
        tokenRepository.findByTokenAndTokenType(jwt, type).ifPresentOrElse(token -> {
            token.setRevoked(false);
            token.setExpired(jwtService.extractExpiration(jwt, type));
            tokenRepository.save(token);
        }, () -> {
            Token token = Token.builder()
                .user(user)
                .token(jwt) 
                .expired(jwtService.extractExpiration(jwt, type))
                .tokenType(type)
                .revoked(false)
                .build();
           
            tokenRepository.save(token);
        });
 
    }
 
    private void revokeAllUserToken(User user, TokenType type) {
        List<Token> validUserToken = tokenRepository.findAllValidTokenByUser(user.getId(), type);
 
        if (validUserToken.isEmpty()) {
            return;
        }
 
        validUserToken.forEach(token -> {
            token.setRevoked(true);
        });
 
        tokenRepository.saveAll(validUserToken);
    }

    @Override
    public ApiResponse<TokenResponse> login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> 
            new NotFoundException("User not found.")
        );
        String jwtToken = jwtService.generateToken(user, TokenType.BEARER);

        revokeAllUserToken(user, TokenType.BEARER);
        saveUserToken(user, jwtToken, TokenType.BEARER);

        TokenResponse response = new TokenResponse(jwtToken);

        return Response.createResponse(HttpStatus.OK, response);
    }

    @Override
    public ApiResponse<String> register(RegisterRequest registerRequest) {

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException("This email already exist.");
        }

        User user = userMapper.requestToEntity(registerRequest);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return Response.createResponse(HttpStatus.CREATED, "User register successfully.");
    }

    @Override
    public ApiResponse<UserDTO> getProfile(User user) {
        UserDTO userDTO = userMapper.toDto(user);
        return Response.createResponse(HttpStatus.CREATED, userDTO);
    }

    @Override
    public ApiResponse<UserDTO> updateUsername(User user, NameRequest nameRequest) {

        user.setName(nameRequest.getName());
        userRepository.save(user);

        UserDTO userDTO = userMapper.toDto(user);

        return Response.createResponse(HttpStatus.OK, userDTO);
    }

    @Override
    public ApiResponse<List<UserDTO>> getAllProfile() {
        
        List<User> users = userRepository.findAll();

        List<UserDTO> userDTOs = users.stream()
                                    .map(user -> userMapper.toDto(user))
                                    .collect(Collectors.toList());

        return Response.createResponse(HttpStatus.OK, userDTOs);

    }
}