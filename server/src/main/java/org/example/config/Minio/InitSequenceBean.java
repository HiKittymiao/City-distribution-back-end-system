package org.example.config.Minio;

import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import lombok.extern.slf4j.Slf4j;
import org.example.utlis.MinioUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @program: springBootDemo
 * @description: minio初始化策略
 * @author: TangLeiPing
 * @create: 2022-06-18 13:43
 **/

@Component
@Slf4j
public class InitSequenceBean implements InitializingBean {

    @Resource
    private MinioClient minioClient;
    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private MinioConfig prop;

    @Override
    public void afterPropertiesSet() {
        String bucketName = prop.getBucketName();
        try {
            if (!minioUtil.bucketExists(bucketName)) {
                minioUtil.makeBucket(bucketName);
                String policyJson = "{\n" +
                        "\t\"Version\": \"2012-10-17\",\n" +
                        "\t\"Statement\": [{\n" +
                        "\t\t\"Effect\": \"Allow\",\n" +
                        "\t\t\"Principal\": {\n" +
                        "\t\t\t\"AWS\": [\"*\"]\n" +
                        "\t\t},\n" +
                        "\t\t\"Action\": [\"s3:GetBucketLocation\", \"s3:ListBucket\", \"s3:ListBucketMultipartUploads\"],\n" +
                        "\t\t\"Resource\": [\"arn:aws:s3:::" + bucketName + "\"]\n" +
                        "\t}, {\n" +
                        "\t\t\"Effect\": \"Allow\",\n" +
                        "\t\t\"Principal\": {\n" +
                        "\t\t\t\"AWS\": [\"*\"]\n" +
                        "\t\t},\n" +
                        "\t\t\"Action\": [\"s3:AbortMultipartUpload\", \"s3:DeleteObject\", \"s3:GetObject\", \"s3:ListMultipartUploadParts\", \"s3:PutObject\"],\n" +
                        "\t\t\"Resource\": [\"arn:aws:s3:::" + bucketName + "/*\"]\n" +
                        "\t}]\n" +
                        "}\n";
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(policyJson).build());
                log.info("buckets：【{}】,创建[readwrite]策略成功！", bucketName);
            } else {
                log.info("minio bucket->>>【{}】already exists", bucketName);
            }
        } catch (Exception e) {
            log.debug("minio bucket->>>【{}】 created filed", bucketName);
            e.printStackTrace();
        }

    }
}

