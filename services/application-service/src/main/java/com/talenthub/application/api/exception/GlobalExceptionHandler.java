package com.talenthub.application.api.exception;

import com.talenthub.application.domain.exception.*;
import feign.FeignException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Chuẩn hoá lỗi trả về theo RFC 7807 (ProblemDetail) cho application-service.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * BRULE-09: nộp trùng candidate + job.
     */
    @ExceptionHandler(DuplicateApplicationException.class)
    public ProblemDetail handleDuplicate(DuplicateApplicationException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());          // 409
    }

    @ExceptionHandler(ApplicationNotFoundException.class)
    public ProblemDetail handleNotFound(ApplicationNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());         // 404
    }


    @ExceptionHandler(JobNotOpenForApplicationException.class)
    public ProblemDetail handleJobNotOpen(JobNotOpenForApplicationException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());            // 409
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleBadRequest(IllegalArgumentException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());       // 400
    }

    /**
     * BRULE-12: chuyển stage không hợp lệ (nhảy cóc / lùi): unprocessable.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleInvalidTransition(IllegalStateException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage()); // 422
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fe ->
                fieldErrors.put(fe.getField(), fe.getDefaultMessage()));

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        pd.setProperty("errors", fieldErrors);
        return pd;
    }

    @ExceptionHandler(RemoteJobNotFoundException.class)
    public ProblemDetail handleRemoteJobNotFound(RemoteJobNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());           // 404
    }

    @ExceptionHandler(RemoteCandidateNotFoundException.class)
    public ProblemDetail handleRemoteCandidateNotFound(RemoteCandidateNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());           // 404
    }

    /**
     * job-service lỗi/sập/timeout (5xx) - lỗi up stream, KHÔNG phải lỗi client.
     */
    @ExceptionHandler(FeignException.class)
    public ProblemDetail handleFeign(FeignException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_GATEWAY,                                                            // 502
                "Lỗi khi gọi job-service");
        pd.setProperty("upstreamStatus", ex.status());   // -1 nếu timeout/không kết nối được
        return pd;
    }

    @ExceptionHandler(DownstreamServiceException.class)
    public ProblemDetail handleDownstream(DownstreamServiceException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage()); // 503
    }

    /**
     * Vi phạm ràng buộc toàn vẹn ở DB (vd: unique uk_candidate_job khi nộp trùng đồng thời).
     * Map về 409 cho đồng nhất với DuplicateApplicationException.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "Dữ liệu vi phạm ràng buộc toàn vẹn (có thể do nộp trùng candidate + job)"); // 409
    }


}
