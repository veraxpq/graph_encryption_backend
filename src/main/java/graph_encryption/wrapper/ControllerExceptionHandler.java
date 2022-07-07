package graph_encryption.wrapper;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import graph_encryption.exceptions.UnauthorizedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.IllegalFormatException;

@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger logger = LogManager.getLogger(ControllerExceptionHandler.class.getName());

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleIllegalArgumentException(
            IllegalArgumentException e, HttpServletRequest request, HttpServletResponse response) {
        logger.debug(ExceptionUtils.getStackTrace(e));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(1001, e.getMessage()), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UnrecognizedPropertyException.class)
    @ResponseBody()
    public ResponseEntity<ErrorMessage> handleUnrecognizedPropertyException(
            UnrecognizedPropertyException e, HttpServletRequest request, HttpServletResponse response) {
        logger.debug(ExceptionUtils.getStackTrace(e));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(1002, "Unknown property."), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DuplicateKeyException.class)
    @ResponseBody()
    public ResponseEntity<ErrorMessage> handleDuplicateKeyException(
            DuplicateKeyException e, HttpServletRequest request, HttpServletResponse response) {
        logger.debug(ExceptionUtils.getStackTrace(e));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(1002, "illegal id"), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MultipartException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleMultipartException(
            MultipartException e, HttpServletRequest request, HttpServletResponse response) {
        logger.debug(ExceptionUtils.getStackTrace(e));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(1003, "The file is not correct."), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleBindException (
            BindException e, HttpServletRequest request, HttpServletResponse response) {
        logger.debug(ExceptionUtils.getStackTrace(e));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(1004, e.getMessage()), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleValidatorException (
            MethodArgumentNotValidException e, HttpServletRequest request, HttpServletResponse response) {
        logger.debug(ExceptionUtils.getStackTrace(e));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        String message = e.getMessage();
        if (e.getBindingResult().getAllErrors().isEmpty() == false) {
            message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        }
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(1005, message), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = IllegalFormatException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleIIllegalFormatException (
            IllegalFormatException e, HttpServletRequest request, HttpServletResponse response) {
        logger.debug(ExceptionUtils.getStackTrace(e));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(1006, "The format is not correct."), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e, HttpServletRequest request, HttpServletResponse response) {
        logger.debug(ExceptionUtils.getStackTrace(e));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(1007, "The format is not correct."), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleConstraintViolationException(
            ConstraintViolationException e, HttpServletRequest request, HttpServletResponse response) {
        logger.debug(ExceptionUtils.getStackTrace(e));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(1008, e.getMessage()), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleUnauthorizedException(
            UnauthorizedException e, HttpServletRequest request, HttpServletResponse response) {
        logger.debug(ExceptionUtils.getStackTrace(e));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(1009, e.getMessage()), headers, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleRuntimeException(
            RuntimeException e, HttpServletRequest request, HttpServletResponse response) {
        logger.error(ExceptionUtils.getStackTrace(e));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(2001, "Internal server error"), headers,  HttpStatus.INTERNAL_SERVER_ERROR);
    }
}