package az.company.qwisedemoapp.model.constants;

public final class EndpointConstants {
    private EndpointConstants() {}

    public static final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/auth/refresh-token",
            "/api/v1/auth/verify-otp",
            "/api/v1/auth/forgot-password/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/api/v1/public/**",
            "/login/oauth2/**"
    };

    public static final String[] ADMIN_ENDPOINTS = {
            "/api/v1/users/admin/**",
            "/api/v1/packets/admin/**"
    };
}
