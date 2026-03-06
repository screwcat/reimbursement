package com.reimburse.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * 报销票据影像附件表
 *
 * @author ruoyi
 */
@Data
@TableName("sys_reimburse_attachment")
public class ReimburseAttachment
{
    private static final long serialVersionUID = 1L;

    /** 附件ID */
    @TableId(type = IdType.AUTO)
    private Long attachmentId;

    /** 报销单ID */
    private Long reimburseId;

    /** 文件名 */
    private String fileName;

    /** 文件路径 */
    private String filePath;

    /** 文件大小(字节) */
    private Long fileSize;

    /** 文件类型 */
    private String fileType;

    /** 上传人 */
    private String createBy;

    /** 上传时间 */
    private Date createTime;
}
