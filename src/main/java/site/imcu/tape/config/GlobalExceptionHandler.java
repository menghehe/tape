package site.imcu.tape.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import site.imcu.tape.pojo.ResponseData;

/**
 * @author MengHe
 * @date 2020/1/2 9:50
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
//    @ExceptionHandler(value = Exception.class)
//    @ResponseBody
//    public ResponseData ExceptionHandler(Exception e){
//        log.info(e.getMessage());
//        return ResponseData.builder().message(e.getMessage()).code(-1).build();
//    }


    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ResponseData bindExceptionHandler(BindException bindException){
        log.info(bindException.getMessage());
        return ResponseData.builder().message(bindException.getMessage()).code(-1).build();
    }

    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseBody
    public ResponseData authenticationException(AuthenticationException authenticationException){
        log.info(authenticationException.getMessage());
        return ResponseData.builder().code(-1).message(authenticationException.getMessage()).build();
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseData handleValidationExceptions(MethodArgumentNotValidException e){
        log.info(e.getMessage());
        return ResponseData.builder().code(-1).message(e.getMessage()).build();
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseBody
    public ResponseData authenticationException(AccessDeniedException e){
        log.info(e.getMessage());
        return ResponseData.builder().code(-1).message(e.getMessage()).build();
    }



}
