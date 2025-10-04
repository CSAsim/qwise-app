package az.company.qwisedemoapp.handler;

import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.model.dto.response.AuthResponseDto;
import az.company.qwisedemoapp.model.enums.UserRole;
import az.company.qwisedemoapp.model.enums.UserStatus;
import az.company.qwisedemoapp.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String registrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

        User useInfo = User.builder()
                .email(email)
                .fullName(name)
                .password(UUID.randomUUID().toString())
                .provider(registrationId.toUpperCase())
                .status(UserStatus.ACTIVE)
                .roles(Set.of(UserRole.ROLE_STUDENT))
                .build();
        AuthResponseDto authResponse = authService.loginOrRegisterOAuth2User(useInfo);

        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getWriter(), authResponse);
    }
}
