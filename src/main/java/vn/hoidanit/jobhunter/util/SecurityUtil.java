package vn.hoidanit.jobhunter.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.dto.ResLoginDTO;


@Service
public class SecurityUtil {
//    JWT được cấu tạo từ 3 thành phần: header.payload.signature
//    1. header => encode dưới dạng base64
//    2. payload (thông tin data chứa trong token) => encode dưới dạng base64
//    3. Signature (chữ ký) => được tạo nên từ thuật toán mã hóa + (header + payload) + key
//            (mật khẩu) dưới dạng base64
    private final JwtEncoder jwtEncoder; // dùng để mã hóa JWT

    public SecurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Value("${chihieu.jwt.base64-secret}")
    private String jwtKey;

    @Value("${chihieu.jwt.access-token-validity-in-seconds}")
    private long accessTokenExpiration;

    @Value("${chihieu.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public String createAccessToken(Authentication authentication) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS);

        // @formatter:off
        // pauload của JWT
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now) // thời gian phát hành
            .expiresAt(validity) // thời gian hết hạn
            .subject(authentication.getName()) // tên người dùng
            .claim("chihieu", authentication) // thông tin khác
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public String createRefreshToken(String email, ResLoginDTO dto) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.refreshTokenExpiration, ChronoUnit.SECONDS);

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now) // thời gian phát hành
                .expiresAt(validity) // thời gian hết hạn
                .subject(email) // tên người dùng
                .claim("user", dto.getUser()) // thông tin khác
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<String> getCurrentUserLogin() { // lấy người dùng khi đã đăng nhập
        SecurityContext securityContext = SecurityContextHolder.getContext(); // chứa chi tiết về người dùng đã được xác thực.
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication())); //  chứa chi tiết về người dùng đã được xác thực, bao gồm tên đăng nhập và các quyền của người dùng.
    }

    private static String extractPrincipal(Authentication authentication) { // trích xuất thông tin người dùng đã được xác thực
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) { // UserDetails là một interface mà Spring Security cung cấp (cú pháp java 14) -> gắn giá trị của principal vào biến springSecurityUser
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    /**
     * Get the JWT of the current user.
     *
     * @return the JWT of the current user.
     */
    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    // public static boolean isAuthenticated() {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     return authentication != null && getAuthorities(authentication).noneMatch(AuthoritiesConstants.ANONYMOUS::equals);
    // }

    /**
     * Checks if the current user has any of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has any of the authorities, false otherwise.
     */
    // public static boolean hasCurrentUserAnyOfAuthorities(String... authorities) {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     return (
    //         authentication != null && getAuthorities(authentication).anyMatch(authority -> Arrays.asList(authorities).contains(authority))
    //     );
    // }

    /**
     * Checks if the current user has none of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has none of the authorities, false otherwise.
     */
    // public static boolean hasCurrentUserNoneOfAuthorities(String... authorities) {
    //     return !hasCurrentUserAnyOfAuthorities(authorities);
    // }

    /**
     * Checks if the current user has a specific authority.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    // public static boolean hasCurrentUserThisAuthority(String authority) {
    //     return hasCurrentUserAnyOfAuthorities(authority);
    // }

    // private static Stream<String> getAuthorities(Authentication authentication) {
    //     return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
    // }
}
