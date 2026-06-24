package com.talenthub.application.infrastructure.client.candidate;

import com.talenthub.application.domain.exception.DownstreamServiceException;
import com.talenthub.application.domain.exception.RemoteCandidateNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class CandidateClientFallbackFactory implements FallbackFactory<CandidateServiceClient> {

    @Override
    public CandidateServiceClient create(Throwable cause) {
        return new CandidateServiceClient() {
            @Override
            public CandidateView getCandidateById(UUID candidateId) {
                // 404 phải giữ nguyên (lỗi nghiệp vụ, không phải downstream sập)
                if (cause instanceof RemoteCandidateNotFoundException e) {
                    throw e;
                }

                // getCandidateById không có giá trị fallback hợp lý thì báo lỗi tuyến sau (503)
                log.error("[FALLBACK] không lấy được candidate {} do candidate-service lỗi: {}",
                        candidateId, cause.toString());
                throw new DownstreamServiceException("candidate-service");
            }
        };
    }
}
