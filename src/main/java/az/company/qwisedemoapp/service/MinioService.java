package az.company.qwisedemoapp.service;

import az.company.qwisedemoapp.config.MinioProperties;
import az.company.qwisedemoapp.exception.InvalidInputException;
import az.company.qwisedemoapp.model.enums.FileCategory;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioService {


    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public String uploadFile(MultipartFile file, FileCategory category) {
        try {
            if(file.isEmpty()) {
                return null;
            }
            String bucketName = switch (category) {
                case QUESTION_IMAGE -> minioProperties.getBuckets().get("question-images");
                case USER_PROFILE -> minioProperties.getBuckets().get("user-profiles");
                case THUMBNAIL_IMAGE -> minioProperties.getBuckets().get("thumbnail-images");
            };

            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String objectName = UUID.randomUUID() + extension;

            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
            }

            String url = minioProperties.getUrl();
            boolean secure = minioProperties.isSecure();
            String protocol = secure ? "https" : "http";

            return String.format("%s://%s/%s/%s", protocol, url.replace("http://", "").replace("https://", ""), bucketName, objectName);

        } catch (MinioException e) {
            throw new RuntimeException("Minio error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }

    @Transactional
    public String updateFile(MultipartFile newFile, String oldFileUrl, FileCategory category) {
        try {

            if (newFile == null || newFile.isEmpty()) {
                throw new InvalidInputException("New file cannot be null or empty");
            }

            if (oldFileUrl != null && !oldFileUrl.isEmpty()) {
                deleteFile(oldFileUrl);
            }

            return uploadFile(newFile, category);

        } catch (Exception e) {
            throw new RuntimeException("File update failed: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteFile(String url) {
        try {
            String[] parts = getParts(url);

            String bucketName = parts[1];
            String objectName = parts[2];

            minioClient.removeObject(
                    io.minio.RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );

            System.out.printf("✅ File deleted successfully from bucket '%s': %s%n", bucketName, objectName);

        } catch (MinioException e) {
            throw new RuntimeException("Minio error during delete: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("File deletion failed: " + e.getMessage(), e);
        }
    }

    private static String[] getParts(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("File URL cannot be null or empty");
        }

        String cleanedUrl = url.replace("http://", "").replace("https://", "");
        String[] parts = cleanedUrl.split("/", 3);
        if (parts.length < 3) {
            throw new InvalidInputException("Invalid MinIO URL format: " + url);
        }
        return parts;
    }
}
