package com.talenthub.candidate.application.usecase;

import com.talenthub.candidate.domain.exception.FileStorageException;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;
    public String uploadFile(MultipartFile file) {
        try {
            // Kiểm tra bucket đã tồn tại chưa, nếu chưa thì tạo mới
            boolean found = minioClient.bucketExists(io.minio.BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(io.minio.MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // 1. Sinh tên file ngẫu nhiên tránh trùng lặp
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // 2. Upload luồng stream lên MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            return fileName; // Trả về "Mã giữ đồ" - Claim Check Pattern (ví dụ: 123-abc.pdf)

        } catch (Exception e) {
            throw new FileStorageException("Lỗi trong quá trình upload file lên MinIO", e);
        }
    }
}