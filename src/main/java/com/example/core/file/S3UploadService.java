package com.example.core.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;
    private static final int filePathLength = 73;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //todo exception 정의 (메소드 전체)
    public String upload(MultipartFile file) throws IOException {
        String createFileName = "image/" + createFileName(file.getOriginalFilename());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3.putObject(bucket, createFileName, file.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, createFileName).toString();
    }

    public List<String> upload(List<MultipartFile> files) throws IOException {
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            fileNames.add(upload(file));
        }

        return fileNames;
    }

    public void delete(String url){
        String filePathAndName = url.substring(filePathLength);
        amazonS3.deleteObject(bucket, filePathAndName);
    }

    private String createFileName(String fileName) throws IOException {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) throws IOException {
        if (fileName.isEmpty()) {
            throw new IOException("");
        }
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)) {
            throw new IOException("");
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
