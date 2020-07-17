package zarg.bank.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class EntityLockingExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {PessimisticLockingFailureException.class, ObjectOptimisticLockingFailureException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {

        log.error("Failed while locking requested object for update for request {}", request);
        return handleExceptionInternal(ex, "Failed while locking requested object for update",
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}