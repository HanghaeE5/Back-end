package com.example.backend.user.config;

import com.example.backend.exception.RestAuthenticationEntryPoint;
import com.example.backend.filter.TokenAuthenticationFilter;
import com.example.backend.notification.service.EmitterService;
import com.example.backend.user.domain.RoleType;
import com.example.backend.user.oauth.handler.OAuth2AuthenticationFailureHandler;
import com.example.backend.user.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.example.backend.user.oauth.handler.TokenAccessDeniedHandler;
import com.example.backend.user.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.example.backend.user.repository.UserRefreshTokenRepository;
import com.example.backend.user.repository.UserRepository;
import com.example.backend.user.service.CustomOAuth2UserService;
import com.example.backend.user.token.AuthTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final CorsProperties corsProperties;
    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final CustomOAuth2UserService oAuth2UserService;
    private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final EmitterService emitterService;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .headers()
                .frameOptions().sameOrigin().and() // SockJS는 기본적으로 HTML iframe 요소를 통한 전송을 허용하지 않도록 설정되는데 해당 내용을 해제한다.
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .accessDeniedHandler(tokenAccessDeniedHandler)
                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()

                .antMatchers("/subscribe/**").permitAll()
                .antMatchers("/publish/notification/**").permitAll()

                .antMatchers("/sub/**").permitAll()
                .antMatchers("/pub/**").permitAll()
                .antMatchers("/ws/**").permitAll()
                .antMatchers("/sub").permitAll()
                .antMatchers("/ws").permitAll()
                .antMatchers("/pub").permitAll()

                .antMatchers("/").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()

                .antMatchers("/test/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/oauth2/**").permitAll()
                .antMatchers("/error/**").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/refresh").permitAll()
                .antMatchers("/register/**").permitAll()

                .antMatchers("/health").permitAll()
                .antMatchers("/notifications/**").hasAnyAuthority(RoleType.USER.getCode())
                .antMatchers("/register/social").hasAnyAuthority(RoleType.USER.getCode())
                .antMatchers("/todo/**").hasAnyAuthority(RoleType.USER.getCode())
                .antMatchers("/board/**").hasAnyAuthority(RoleType.USER.getCode())
                .antMatchers("/user/**").hasAnyAuthority(RoleType.USER.getCode())
                .antMatchers("/chat/**").hasAnyAuthority(RoleType.USER.getCode())
                .antMatchers("/event/**").hasAnyAuthority(RoleType.USER.getCode())

                .antMatchers("/api/**/admin/**").hasAnyAuthority(RoleType.ADMIN.getCode())
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization")
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/**/oauth2/code/**")
                .and()
                .userInfoEndpoint()
                .userService(oAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler())
                .failureHandler(oAuth2AuthenticationFailureHandler());

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    private static final String[] PERMIT_URL_ARRAY = {
//            /* swagger v2 */
//            "/v2/api-docs",
//            "/swagger-resources",
//            "/swagger-resources/**",
//            "/configuration/ui",
//            "/configuration/security",
//            "/swagger-ui.html",
//            "/webjars/**",
//            /* swagger v3 */
//            "/v3/api-docs/**",
//            "/swagger-ui/**",
//            "/ws",
//            "/ws/**",
//            "/subscribe/**"
//    };
//
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(
                "/subscribe/**"
        );
    }

    /*
     * security 설정 시, 사용할 인코더 설정
     * */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * 토큰 필터 설정
     * */
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    /*
     * 쿠키 기반 인가 Repository
     * 인가 응답을 연계 하고 검증할 때 사용.
     * */
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    /*
     * Oauth 인증 성공 핸들러
     * */
    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(
                tokenProvider,
                appProperties,
                userRefreshTokenRepository,
                userRepository,
                emitterService,
                oAuth2AuthorizationRequestBasedOnCookieRepository()
        );
    }

    /*
     * Oauth 인증 실패 핸들러
     * */
    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository());
    }

    /*
     * Cors 설정
     * */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfig = new CorsConfiguration();
        String[] exposedHeader = corsProperties.getExposedHeaders().split(",");
        corsConfig.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
        corsConfig.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
        corsConfig.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
        for (String expose : exposedHeader)
            corsConfig.addExposedHeader(expose);
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(corsConfig.getMaxAge());

        corsConfigSource.registerCorsConfiguration("/**", corsConfig);
        return corsConfigSource;
    }
}
