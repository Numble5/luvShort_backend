package com.example.backend.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.backend.exception.BackendException;
import com.example.backend.exception.ReturnCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

@NoArgsConstructor
@Service
public class S3Service {

   private AmazonS3 s3Client;



   @Value("${cloud.aws.s3.bucket}")
   private String awsBucket;

   @Value("${cloud.aws.region.static}")
   private String region;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

   @PostConstruct
   public void setS3Client() {
       AWSCredentials credentials = new BasicAWSCredentials(this.accessKey,this.secretKey);

       s3Client = AmazonS3ClientBuilder.standard()
               .withCredentials(new AWSStaticCredentialsProvider(credentials))
               .withRegion(this.region)
               .build();
   }
    public String upload(MultipartFile multipartFile, String bucket) {
        int extIdx = multipartFile.getOriginalFilename().lastIndexOf("."); // 가장 마지막 "."의 index
        String extension = multipartFile.getOriginalFilename().substring(extIdx+1); // 파일의 확장자

        String fileName = UUID.randomUUID() + "." + extension;
        String fileBucket = awsBucket + "/" + bucket; // 파일 비디오/썸네일 저장 위치 다름

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        try {
            s3Client.putObject(new PutObjectRequest(fileBucket,fileName,multipartFile.getInputStream(),objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
            return s3Client.getUrl(fileBucket,fileName).toString();
        }catch (IOException e) {
            // 파일 저장 에러
            throw new BackendException(ReturnCode.FILE_CAN_NOT_SAVE);
        }


    }

//
//    public void delete(String filePath) {
//        File file = new File(filePath);
//
//        if(!file.exists()) return; // 파일 존재하지 않는다.
//        if(!file.delete()) throw new BackendException(ReturnCode.FILE_CAN_NOT_DELETE);
//    }
}
