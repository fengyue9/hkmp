package com.ruoyi.device.config;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.PutObjectOptions;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
/**
 * MinIO配置类
 *
 * @author hongrongjian
 * @date 2024/02/20
 */
@Configuration
public class MinioConfig implements InitializingBean {
    @Value(value = "${minio.bucket}")
    private String bucket;

    @Value(value = "${minio.host}")
    private String host;

    @Value(value = "${minio.url}")
    private String url;

    @Value(value = "${minio.access-key}")
    private String accessKey;

    @Value(value = "${minio.secret-key}")
    private String secretKey;

    private MinioClient minioClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasText(url, "Minio url 为空");
        Assert.hasText(accessKey, "Minio accessKey为空");
        Assert.hasText(secretKey, "Minio secretKey为空");
        this.minioClient = new MinioClient(this.host, this.accessKey, this.secretKey);
    }

    /**
     * 上传
     */
    public String putObject(MultipartFile multipartFile) throws Exception {
        // bucket 不存在，创建
        if (!minioClient.bucketExists(this.bucket)) {
            minioClient.makeBucket(this.bucket);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            // 上传文件的名称
            String fileName = multipartFile.getOriginalFilename();
            // PutObjectOptions，上传配置(文件大小，内存中文件分片大小)
            PutObjectOptions putObjectOptions = new PutObjectOptions(multipartFile.getSize(),
                    PutObjectOptions.MIN_MULTIPART_SIZE);
            // 文件的ContentType
            putObjectOptions.setContentType(multipartFile.getContentType());
            minioClient.putObject(this.bucket, fileName, inputStream, putObjectOptions);
            // 返回访问路径
            return this.url + UriUtils.encode(fileName, StandardCharsets.UTF_8);
        }
    }

    /**
     * 文件下载
     */
    public void download(String fileName, HttpServletResponse response) {
        // 从链接中得到文件名
        InputStream inputStream;
        try {
            MinioClient minioClient = new MinioClient(host, accessKey, secretKey);
            ObjectStat stat = minioClient.statObject(bucket, fileName);
            inputStream = minioClient.getObject(bucket, fileName);
            response.setContentType(stat.contentType());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/octet-stream");
            IOUtils.copy(inputStream, response.getOutputStream());
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("下载出现异常：" + e);
        }
    }

    /**
     * 列出所有存储桶名称
     *
     * @return
     * @throws Exception
     */
    public List<String> listBucketNames() throws Exception {
        List<Bucket> bucketList = listBuckets();
        List<String> bucketListName = new ArrayList<>();
        for (Bucket bucket : bucketList) {
            bucketListName.add(bucket.name());
        }
        return bucketListName;
    }

    /**
     * 查看所有桶
     *
     * @return
     * @throws Exception
     */
    public List<Bucket> listBuckets() throws Exception {
        return minioClient.listBuckets();
    }

    /**
     * 检查存储桶是否存在
     *
     * @param bucketName
     * @return
     * @throws Exception
     */
    public boolean bucketExists(String bucketName) throws Exception {
        boolean flag = minioClient.bucketExists(bucketName);
        if (flag) {
            return true;
        }
        return false;
    }

    /**
     * 创建存储桶
     *
     * @param bucketName
     * @return
     * @throws Exception
     */
    public boolean makeBucket(String bucketName) throws Exception {
        boolean flag = bucketExists(bucketName);
        if (!flag) {
            minioClient.makeBucket(bucketName);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除桶
     *
     * @param bucketName
     * @return
     * @throws Exception
     */
    public boolean removeBucket(String bucketName) throws Exception {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                // 有对象文件，则删除失败
                if (item.size() > 0) {
                    return false;
                }
            }
            // 删除存储桶，注意，只有存储桶为空时才能删除成功。
            minioClient.removeBucket(bucketName);
            flag = bucketExists(bucketName);
            if (!flag) {
                return true;
            }

        }
        return false;
    }

    /**
     * 列出存储桶中的所有对象
     *
     * @param bucketName 存储桶名称
     * @return
     * @throws Exception
     */
    public Iterable<Result<Item>> listObjects(String bucketName) throws Exception {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            return minioClient.listObjects(bucketName);
        }
        return null;
    }

    /**
     * 列出存储桶中的所有对象名称
     *
     * @param bucketName 存储桶名称
     * @return
     * @throws Exception
     */
    public List<String> listObjectNames(String bucketName) throws Exception {
        List<String> listObjectNames = new ArrayList<>();
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                listObjectNames.add(item.objectName());
            }
        }
        return listObjectNames;
    }

    /**
     * 删除一个对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @throws Exception
     */
    public boolean removeObject(String bucketName, String objectName) throws Exception {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            minioClient.removeObject(bucketName, objectName);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 文件访问路径
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return
     * @throws Exception
     */
    public String getObjectUrl(String bucketName, String objectName) throws Exception {
        boolean flag = bucketExists(bucketName);
        String url = "";
        if (flag) {
            url = minioClient.getObjectUrl(bucketName, objectName);
        }
        return url;
    }
}
