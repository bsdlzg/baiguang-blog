package com.bsdlzg.blog.strategy.context;

import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.strategy.FileUploadStrategy;
import com.bsdlzg.blog.utils.SpringContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


/**
 * @apiNote 文件上传策略上下文
 */
@Service
@RequiredArgsConstructor
public class FileUploadStrategyContext {

    private final SpringContextUtils springContextUtils;

    /**
     * 执行文件上传策略
     *
     * @param file 文件对象
     * @return {@link String} 文件名
     */
    public String executeFileUploadStrategy(String fileUploadMode, MultipartFile file, String suffix) {
        return ((FileUploadStrategy) springContextUtils.getBean(fileUploadMode)).fileUpload(file, suffix);
    }

    /**
     * 执行文件列表策略
     *
     * @param pageNo   页数
     * @param pageSize 每页显示数量
     * @return {@link String} 文件名
     */
    public ResponseResult executeFileListStrategy(String fileUploadMode, int pageNo, int pageSize) {
        return ((FileUploadStrategy) springContextUtils.getBean(fileUploadMode)).fileList(pageNo, pageSize);
    }

    /**
     * 删除文件策略
     *
     * @param fileUploadMode
     * @param key
     * @return
     */
    public Boolean executeDeleteFileStrategy(String fileUploadMode, String... key) {
        return ((FileUploadStrategy) springContextUtils.getBean(fileUploadMode)).deleteFile(key);
    }
}
