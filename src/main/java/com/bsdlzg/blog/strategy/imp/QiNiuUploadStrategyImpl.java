package com.bsdlzg.blog.strategy.imp;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.entity.SystemConfig;
import com.bsdlzg.blog.enums.QiNiuAreaEnum;
import com.bsdlzg.blog.exception.BusinessException;
import com.bsdlzg.blog.service.SystemConfigService;
import com.bsdlzg.blog.strategy.FileUploadStrategy;
import com.bsdlzg.blog.utils.UUIDUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;


@Service("qiNiuUploadStrategyImpl")
@RequiredArgsConstructor
public class QiNiuUploadStrategyImpl implements FileUploadStrategy {

    private final Logger logger = LoggerFactory.getLogger(QiNiuUploadStrategyImpl.class);

    private final SystemConfigService systemConfigService;
    private String qi_niu_accessKey;
    private String qi_niu_secretKey;
    private String qi_niu_bucket;
    private Region region;
    private String qi_niu_url;


    @PostConstruct
    private void init(){
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();
        qi_niu_accessKey = systemConfig.getQiNiuAccessKey();
        qi_niu_secretKey = systemConfig.getQiNiuSecretKey();
        qi_niu_bucket = systemConfig.getQiNiuBucket();
        qi_niu_url = systemConfig.getQiNiuPictureBaseUrl();
        region = QiNiuAreaEnum.getRegion(systemConfig.getQiNiuArea());

        logger.info("------初始化七牛云上传配置文件成功-----");
    }

    /**
     * 获取文件列表 暂不进行分页（云存储使用的返回参数marker标记来区分是否有下一页，不太好进行跟每一页对应）
     * @param pageNo 页码
     * @param limit 页数
     * @return
     */
    @Override
    public ResponseResult fileList(int pageNo, int limit) {
        Configuration configuration = new Configuration(region);
        Auth auth = Auth.create(qi_niu_accessKey, qi_niu_secretKey);
        BucketManager bucketManager = new BucketManager(auth,configuration);
        try {
            //第一个参数：桶名称，第二个参数：指定文件名前缀，第三个参数：下一页标记（从filesV2 结果中取），第四个参数：每次显示最大数
            FileListing filesV2 = bucketManager.listFilesV2(qi_niu_bucket, null, null, 1000, null);
            return ResponseResult.success(filesV2.items);
        } catch (QiniuException e) {
            logger.error(e.getMessage());
            throw new BusinessException("获取文件资源列表失败！");
        }
    }

    @Override
    public String fileUpload(MultipartFile file,String suffix) {
        String key = null;
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(region);
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        Auth auth = Auth.create(qi_niu_accessKey, qi_niu_secretKey);
        String upToken = auth.uploadToken(qi_niu_bucket);
        FileInputStream inputStream = null;
        try {
            inputStream = (FileInputStream) file.getInputStream();
            Response response = uploadManager.put(inputStream, UUIDUtils.getUuid() + "." + suffix, upToken,null,null);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(),DefaultPutRet.class);
            key =  qi_niu_url + putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            logger.info("QiniuException:{}",r.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return key;
    }

    /**
     * 批量删除文件
     * @return
     */
    @Override
    public Boolean deleteFile(String ...keys) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        //...其他参数参考类注释
        Auth auth = Auth.create(qi_niu_accessKey, qi_niu_secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(qi_niu_bucket, keys);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < keys.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = keys[i];
                System.out.print(key + "\t");
                if (status.code == 200) {
                    System.out.println("delete success");
                } else {
                    System.out.println(status.data.error);
                }
            }
            return true;
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
            return false;
        }
    }
}
