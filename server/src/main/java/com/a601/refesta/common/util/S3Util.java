package com.a601.refesta.common.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Util {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.base-url}")
    private String baseUrl;

    public String uploadFile(MultipartFile file) {

        try {
            String type = file.getContentType();
            assert type != null;
            if (type.startsWith("video")) {
                type = "video/mp4";
            }
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            String fileUrl = baseUrl + fileName;
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(type);
            metadata.setContentLength(file.getSize());


            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucket, fileName, file.getInputStream(), metadata
            );
            putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3Client.putObject(putObjectRequest);

            return fileUrl;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFile(String FileName) {
        String S3FileName = FileName.replaceAll(baseUrl, "");
        amazonS3Client.deleteObject(bucket, S3FileName);
    }

    public String getFile(String S3FileName) {
        try {
            URL url = amazonS3Client.getUrl(bucket, S3FileName);
            return (url != null) ? url.getPath() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
