package az.company.qwisedemoapp.model.constants;

public final class EndpointConstants {
    private EndpointConstants() {}

    public static final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/qwise-app/auth/login",
            "/api/v1/qwise-app/auth/register",
            "/api/v1/qwise-app/auth/refresh",
            "/api/v1/qwise-app/auth/verify-otp",
            "/api/v1/qwise-app/auth/forgot-password/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/api/v1/public/**",
            "/login/oauth2/**"
    };

    public static final String[] ADMIN_ENDPOINTS = {
            "/api/v1/qwise-app/admin/**",
            "/actuator/**"
    };
}
