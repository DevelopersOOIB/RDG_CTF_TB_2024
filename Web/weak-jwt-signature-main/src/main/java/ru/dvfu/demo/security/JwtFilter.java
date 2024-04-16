package ru.dvfu.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import ru.dvfu.demo.property.JwtProperties;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final JwtProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    @Override
    public void doFilter(
        ServletRequest servletRequest,
        ServletResponse servletResponse,
        FilterChain filterChain
    ) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Optional<String> accessToken = getTokenFromRequest(request);

        if (accessToken.isPresent()) {

            Pair<Boolean, String> tokeValidation = jwtTokenProvider.validateJwtToken(accessToken.get());

            if (Boolean.TRUE.equals(tokeValidation.getFirst())) {
                Authentication auth = jwtTokenProvider.getAuthentication(accessToken.get());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                SecurityContextHolder.clearContext();
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(tokeValidation.getSecond());
                response.flushBuffer();
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public Optional<String> getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(jwtProperties.getAuthenticationHeader());

        if (StringUtils.hasText(authHeader) && authHeader.startsWith(jwtProperties.getAuthenticationType())) {
            String[] values = authHeader.split(" ");
            if (values.length >= 2) {
                return Optional.ofNullable(values[1]);
            }
        }
        return Optional.empty();
    }
}
