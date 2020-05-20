package com.weiss.weiss.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ListIterator;

@Service
public class AwsService {
    @Autowired
    AmazonS3 s3client;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Value("${aws.bucketName}")
    private String bucketName;
    @Value("${aws.imgFolder}")
    private String imgFolder;

    public String  saveAtS3(File tempFile) {
        String savedFile = checkInStorage(tempFile);

        if(savedFile!=null){
            URL existFileUrl = s3client.getUrl(bucketName, savedFile);
            return existFileUrl.getProtocol()+"://"+existFileUrl.getHost()+existFileUrl.getPath();
        }
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
                imgFolder+"/"+tempFile.getName(),
                tempFile);
        putObjectRequest.setStorageClass(StorageClass.ReducedRedundancy);
        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        PutObjectResult putObjectResult = s3client.putObject(putObjectRequest);
        boolean requesterCharged = putObjectResult.isRequesterCharged();
        URL url = s3client.getUrl(bucketName,
                tempFile.getName());
       return url.getProtocol()+"://"+url.getHost()+"/"+imgFolder+url.getPath();
    }

    private String checkInStorage(File tempFile) {
        String md5 = null;
        try {
            md5  = getMd5(tempFile);
        } catch (Exception e) {
            LOGGER.error("cant calculate md5");
            return null;
        }

        ListObjectsRequest listObjects = new ListObjectsRequest().withBucketName(bucketName);
        ObjectListing objectListing = s3client.listObjects(listObjects);
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        ListIterator<S3ObjectSummary> iterator = objectSummaries.listIterator();
        while(iterator.hasNext()){
            S3ObjectSummary next = iterator.next();
            if(next.getETag().equalsIgnoreCase(md5)){
                return next.getKey();
            }
        }
        return null;
    }

    private String getMd5(File tempFile) throws IOException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(Files.readAllBytes(tempFile.toPath()));
        byte[] hash = messageDigest.digest();
        return String.format("%032X", new BigInteger(1, hash));
    }


}
