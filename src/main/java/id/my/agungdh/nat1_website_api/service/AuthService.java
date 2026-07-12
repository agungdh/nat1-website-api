package id.my.agungdh.nat1_website_api.service;

import id.my.agungdh.nat1_website_api.config.TokenService;
import id.my.agungdh.nat1_website_api.dto.AuthRequest;
import id.my.agungdh.nat1_website_api.dto.AuthResponse;
import id.my.agungdh.nat1_website_api.entity.User;
import id.my.agungdh.nat1_website_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String token = tokenService.generateToken(user.getUsername());
        return new AuthResponse(token);
    }
}
