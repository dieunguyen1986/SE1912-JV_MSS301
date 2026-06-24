package com.talenthub.application.infrastructure.client.job;

import com.talenthub.application.domain.exception.DownstreamServiceException;
import com.talenthub.application.domain.exception.JobNotOpenForApplicationException;
import com.talenthub.application.domain.exception.RemoteJobNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class JobClientFallbackFactory implements FallbackFactory<JobServiceClient> {

    @Override
    public JobServiceClient create(Throwable cause) {
        return new JobServiceClient() {
            @Override
            public JobView getJobById(UUID jobId) {
                // 404 or 409 phải giữ nguyên
                if (cause instanceof RemoteJobNotFoundException e) {
                    throw e;
                }
                if (cause instanceof JobNotOpenForApplicationException e) {
                    throw e;
                }

                // getJobById không có giá trị fallback hợp lý thì báo lỗi tuyến sau (503)
                log.error("[FALLBACK] không lấy được job {} do job-service lỗi: {}",
                        jobId, cause.toString());
                throw new DownstreamServiceException("job-service");
            }
        };
    }
}
