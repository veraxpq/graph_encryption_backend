package graph_encryption.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import graph_encryption.model.UserLoginInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    private static final Logger logger = LogManager.getLogger(JwtUtils.class);

    private static final String SECRET = "aiit-hx-digital-oc-20201021";
    private static final long EXPIRATION = 7200L;

    public static String createToken(UserLoginInfo user) {
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRATION * 1000);
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = JWT.create()
                .withHeader(map)
                .withClaim("email", user.getEmail())
                .withClaim("password", user.getPassword())
                .withExpiresAt(expireDate)
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(SECRET));
        return token;
    }

    public static Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
        } catch (JWTVerificationException e) {
            logger.error(e.getMessage());
            return null;
        }
        return jwt.getClaims();
    }
}
