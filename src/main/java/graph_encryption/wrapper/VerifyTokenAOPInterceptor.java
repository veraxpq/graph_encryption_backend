package graph_encryption.wrapper;

import com.auth0.jwt.interfaces.Claim;
import graph_encryption.exceptions.UnauthorizedException;
import graph_encryption.util.JwtUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * This class is the interceptor to add token verification before api is called.
 */
@Component
@Aspect()
public class VerifyTokenAOPInterceptor {

    private static final Logger logger = LogManager.getLogger(VerifyTokenAOPInterceptor.class.getName());

    /**
     * This method register for the pointcut.
     */
    @Pointcut("@within(graph_encryption.wrapper.VerifyToken)||@annotation(graph_encryption.wrapper.VerifyToken)")
    public void pointcut() {}

    /**
     * This method verify if the request contains verified token before it is called.
     * @param point
     */
    @Before("pointcut()")
    public void before(JoinPoint point) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.
                getRequestAttributes()).getRequest();

        final String token = request.getHeader("Authorization");
        if (token == null) {
            throw new UnauthorizedException();
        }

        Map<String, Claim> userData = JwtUtils.verifyToken(token);
        if (userData == null) {
            throw new UnauthorizedException("Please login!");
        }

        String email = userData.get("email").asString();
        String password = userData.get("password").asString();

        request.setAttribute("password", password);
        request.setAttribute("email", email);
    }
}
