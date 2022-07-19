package graph_encryption.wrapper;

import java.lang.annotation.*;

/**
 * This interface helps to verify token.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VerifyToken {
}
