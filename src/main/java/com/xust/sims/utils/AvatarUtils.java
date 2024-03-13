package com.xust.sims.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class AvatarUtils {
    private static String bucketName = "";
    private static String region = "";
    private static String secretId = "";
    private static String secretKey = "";

    private static COSCredentials cred = new BasicCOSCredentials(secretId,secretKey);
    /**
     * 设置bucket的区域，COS低于的简称
     */
    private static ClientConfig clientConfig = new ClientConfig(new Region(region));
    private static COSClient cosClient = new COSClient(cred, clientConfig);

    private static String urlPrefix = "";


    public static String uploadFile(MultipartFile file, String prefix) {
        String originalFilename = file.getOriginalFilename();
        String subString = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        //判断文件类型，如果不是图片格式，则直接返回该数据
        if (!isImage(subString)) {
            return subString;
        }
        Random random = new Random();
        String name = prefix + "/" + random.nextInt(1000) + System.currentTimeMillis() + subString;
        try {
            InputStream ins = file.getInputStream();
            pushFile(ins, name);
            return urlPrefix + name;
        } catch (Exception e) {
            //如果发生异常，咱不处理
        }
        return subString;
    }


    private static String pushFile(InputStream inputStream, String fileName) {
        String ret = "";
        try {
            // 创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(inputStream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getContentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            // 上传文件
            PutObjectResult putResult = cosClient.putObject(bucketName,  fileName, inputStream, objectMetadata);
            ret = putResult.getETag();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    private static String getContentType(String substring) {
        if (".jpg".equalsIgnoreCase(substring)) {
            return "image/jpeg";
        } else if (".png".equalsIgnoreCase(substring)) {
            return "image/png";
        }
        return null;
    }


    private static boolean isImage(String suffix) {
        if (".jpg".equals(suffix) || ".png".equals(suffix) || ".jpeg".equals(suffix) || ".gif".equalsIgnoreCase(suffix)) {
            return true;
        }
        return false;
    }
}