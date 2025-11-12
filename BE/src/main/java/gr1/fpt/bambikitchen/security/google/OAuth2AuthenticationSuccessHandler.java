package gr1.fpt.bambikitchen.security.google;

import gr1.fpt.bambikitchen.security.CustomUserDetails;
import gr1.fpt.bambikitchen.security.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private JwtUtils jwtUtils;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${mobile.url}")
    private String mobileUrl;


    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        // 1. Lấy CustomOAuth2User từ authentication
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // 2. Lấy thông tin từ CustomOAuth2User
        int accountId = oAuth2User.getId();
        String role = oAuth2User.getRole();
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_"+role)
        );
        CustomUserDetails userDetail = new CustomUserDetails(accountId,authorities);

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetail,null,userDetail.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = jwtUtils.generateToken(auth);

        //xét xem đang gửi là web hay mobile
        String userAgent = request.getHeader("User-Agent");
       // String redirectUri = frontendUrl; // default
//        if(userAgent!=null){
//            String ua = userAgent.toLowerCase();
//            if (ua.contains("okhttp") || ua.contains("android")) {
//                redirectUri = mobileUrl;
//            }
//        }
        String redirectUri = request.getParameter("redirect_uri");
        if (redirectUri == null || redirectUri.isEmpty()) {
            redirectUri = frontendUrl; // fallback cho web
        }
        String redirectUrl = UriComponentsBuilder.fromUriString(redirectUri + "/oauth2/callback")
                .queryParam("token", jwt)
                .build()
                .toUriString();

        // 5. Thực hiện redirect
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }



}
