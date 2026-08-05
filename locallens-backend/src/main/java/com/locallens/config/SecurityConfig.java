package com.locallens.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.locallens.security.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(SecurityConfig.class);

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /*
     * =====================================================
     * SECURITY FILTER CHAIN
     * =====================================================
     */

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                /*
                 * REST API uses JWT, so CSRF protection is not required.
                 */
                .csrf(csrf -> csrf.disable())

                /*
                 * Enable frontend access from Vite.
                 */
                .cors(cors ->
                        cors.configurationSource(
                                corsConfigurationSource()
                        )
                )

                /*
                 * Never create an HTTP session.
                 */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                /*
                 * Return JSON responses for 401 and 403.
                 */
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint(
                                        authenticationEntryPoint()
                                )
                                .accessDeniedHandler(
                                        accessDeniedHandler()
                                )
                )

                /*
                 * Endpoint authorization rules.
                 */
                .authorizeHttpRequests(auth -> auth

                        /*
                         * Allow browser CORS preflight requests.
                         */
                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        /*
                         * Authentication endpoints.
                         */
                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        /*
                         * Spring error endpoint.
                         */
                        .requestMatchers(
                                "/error"
                        ).permitAll()

                        /*
                         * Swagger and OpenAPI endpoints.
                         */
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        /*
                         * Public uploaded images and files.
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/uploads/**",
                                "/images/**",
                                "/files/**"
                        ).permitAll()

                        /*
                         * Public approved-place endpoints.
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/places",
                                "/api/places/",
                                "/api/places/approved",
                                "/api/places/approved/**",
                                "/api/places/public",
                                "/api/places/public/**",
                                "/api/places/categories",
                                "/api/places/categories/**"
                        ).permitAll()

                        /*
                         * Public numeric place-details endpoint.
                         *
                         * Examples:
                         * GET /api/places/1
                         * GET /api/places/25
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/places/{placeId:\\d+}"
                        ).permitAll()

                        /*
                         * =================================================
                         * ADMIN ENDPOINTS
                         * =================================================
                         *
                         * Accept both authority formats:
                         *
                         * ADMIN
                         * ROLE_ADMIN
                         *
                         * This prevents 403 when JwtAuthenticationFilter
                         * creates ADMIN without the ROLE_ prefix.
                         */
                        .requestMatchers(
                                "/api/admin/**"
                        ).hasAnyAuthority(
                                "ADMIN",
                                "ROLE_ADMIN"
                        )

                        /*
                         * =================================================
                         * TRAVELLER ENDPOINTS
                         * =================================================
                         */
                        .requestMatchers(
                                "/api/traveller/**",
                                "/api/travellers/**"
                        ).hasAnyAuthority(
                                "TRAVELLER",
                                "ROLE_TRAVELLER",
                                "ADMIN",
                                "ROLE_ADMIN"
                        )

                        /*
                         * =================================================
                         * LOCAL GUIDE ENDPOINTS
                         * =================================================
                         */
                        .requestMatchers(
                                "/api/guide/**",
                                "/api/guides/**",
                                "/api/local-guide/**",
                                "/api/local-guides/**"
                        ).hasAnyAuthority(
                                "LOCAL_GUIDE",
                                "ROLE_LOCAL_GUIDE",
                                "ADMIN",
                                "ROLE_ADMIN"
                        )

                        /*
                         * Creating or modifying a place requires either
                         * local-guide or admin authority.
                         */
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/places/**"
                        ).hasAnyAuthority(
                                "LOCAL_GUIDE",
                                "ROLE_LOCAL_GUIDE",
                                "ADMIN",
                                "ROLE_ADMIN"
                        )

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/places/**"
                        ).hasAnyAuthority(
                                "LOCAL_GUIDE",
                                "ROLE_LOCAL_GUIDE",
                                "ADMIN",
                                "ROLE_ADMIN"
                        )

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/places/**"
                        ).hasAnyAuthority(
                                "LOCAL_GUIDE",
                                "ROLE_LOCAL_GUIDE",
                                "ADMIN",
                                "ROLE_ADMIN"
                        )

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/places/**"
                        ).hasAnyAuthority(
                                "LOCAL_GUIDE",
                                "ROLE_LOCAL_GUIDE",
                                "ADMIN",
                                "ROLE_ADMIN"
                        )

                        /*
                         * Remaining API requests require a valid JWT.
                         */
                        .requestMatchers(
                                "/api/**"
                        ).authenticated()

                        /*
                         * Allow frontend/static application routes.
                         *
                         * This does not expose API endpoints because all
                         * /api/** routes are handled above.
                         */
                        .anyRequest().permitAll()
                )

                /*
                 * JWT filter must execute before Spring's default
                 * username/password filter.
                 */
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    /*
     * =====================================================
     * CORS CONFIGURATION
     * =====================================================
     */

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:5173",
                        "http://127.0.0.1:5173",
                        "http://localhost:5174",
                        "http://127.0.0.1:5174"
                )
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of(
                        "Authorization",
                        "Content-Type",
                        "Accept",
                        "Origin",
                        "X-Requested-With",
                        "Access-Control-Request-Method",
                        "Access-Control-Request-Headers"
                )
        );

        configuration.setExposedHeaders(
                List.of(
                        "Authorization",
                        "Content-Disposition"
                )
        );

        configuration.setAllowCredentials(true);

        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }

    /*
     * =====================================================
     * AUTHENTICATION MANAGER
     * =====================================================
     */

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();
    }

    /*
     * =====================================================
     * 401 UNAUTHORIZED HANDLER
     * =====================================================
     */

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {

        return (request, response, exception) -> {

            Authentication authentication =
                    org.springframework.security.core.context
                            .SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            LOGGER.warn(
                    "Unauthorized request: method={}, path={}, authentication={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    authentication
            );

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.setContentType(
                    "application/json"
            );

            response.setCharacterEncoding(
                    "UTF-8"
            );

            response.getWriter().write(
                    """
                    {
                      "status": 401,
                      "error": "Unauthorized",
                      "message": "Authentication is required. Please log in again with a valid JWT token."
                    }
                    """
            );
        };
    }

    /*
     * =====================================================
     * 403 FORBIDDEN HANDLER
     * =====================================================
     */

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {

        return (request, response, exception) -> {

            Authentication authentication =
                    org.springframework.security.core.context
                            .SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            String principal =
                    authentication == null
                            ? "not-authenticated"
                            : authentication.getName();

            String authorities =
                    authentication == null
                            ? "none"
                            : authentication
                                    .getAuthorities()
                                    .toString();

            LOGGER.warn(
                    "Access denied: method={}, path={}, principal={}, authorities={}, message={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    principal,
                    authorities,
                    exception.getMessage()
            );

            response.setStatus(
                    HttpServletResponse.SC_FORBIDDEN
            );

            response.setContentType(
                    "application/json"
            );

            response.setCharacterEncoding(
                    "UTF-8"
            );

            String safeAuthorities =
                    authorities
                            .replace("\\", "\\\\")
                            .replace("\"", "\\\"");

            response.getWriter().write(
                    """
                    {
                      "status": 403,
                      "error": "Forbidden",
                      "message": "You do not have permission to access this resource.",
                      "authorities": "%s"
                    }
                    """.formatted(safeAuthorities)
            );
        };
    }
}