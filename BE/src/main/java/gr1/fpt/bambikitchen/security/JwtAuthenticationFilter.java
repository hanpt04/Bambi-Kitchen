package gr1.fpt.bambikitchen.security;

import gr1.fpt.bambikitchen.exception.CustomException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // Bỏ qua các public endpoints (XÓA dòng path.startsWith("/api/"))
        if (path.equals("/api/user/login")
                || path.equals("/api/mail/calculate-calories")
                || path.equals("/api/user/login-with-google")
                || path.startsWith("/oauth2/")
                || path.startsWith("/swagger-ui/")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/api/user/reset-password")
                || path.startsWith("/api/user/forgot-password")
                || path.startsWith("/api/account/register")
                || path.startsWith("/api/mail/verify-otp")
                || path.startsWith("/api/mail/send-otp")
                || path.equals("/api/order/getFeedbacks")
                || path.startsWith("/api/payment/vnpay-return")
                || path.startsWith("/api/payment/momo-return")
                || ("GET".equals(method) && path.matches("^/api/dish/\\d+$")) // GET /api/dish/{id} cho phép truy cập công khai
                //  || path.startsWith("/api/")//để tạm để test api, sau này sửa lại, để lại là fillter ko quét
                || path.equals("/dump-data")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = parseJwtToken(request);

        // Nếu có JWT thì validate và set authentication
        if (jwt != null && jwtUtils.validateToken(jwt)) {
            int userId = jwtUtils.getUserIdFromToken(jwt);
            List<String> roles = jwtUtils.getRolesFromToken(jwt);
            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            CustomUserDetails userDetails = new CustomUserDetails(userId, authorities);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            auth.setDetails(userDetails);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        else if (jwt == null || !jwtUtils.validateToken(jwt)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid or missing JWT token\"}");
            return;
        }

        // Cho phép request tiếp tục dù không có JWT hoặc JWT không hợp lệ
        filterChain.doFilter(request, response);
    }
    private String parseJwtToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);  // Bỏ "Bearer "
        }
        return null;
    }
}
