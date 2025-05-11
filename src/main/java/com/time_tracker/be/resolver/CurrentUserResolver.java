package com.time_tracker.be.resolver;

import com.time_tracker.be.annotation.CurrentUser;
import com.time_tracker.be.dto.CurrentUserDto;
import com.time_tracker.be.exception.NotAuthorizedException;
import com.time_tracker.be.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
public class CurrentUserResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;

    public CurrentUserResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class) &&
                parameter.getParameterType().equals(CurrentUserDto.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String token = request.getHeader("Authorization");
        log.info("Authorization: {}", token);
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            log.info(token);
            Claims claims = jwtTokenProvider.getClaims(token); // decode token pakai jwt util kamu
            log.info("Claims: {}", claims);
            CurrentUserDto user = new CurrentUserDto();
            user.setId_user(claims.get("id_user", Long.class));
            user.setEmail(claims.get("email", String.class));
            user.setName(claims.get("name", String.class));
            return user;
        }

        throw new NotAuthorizedException("Token invalid or missing");
    }
}
