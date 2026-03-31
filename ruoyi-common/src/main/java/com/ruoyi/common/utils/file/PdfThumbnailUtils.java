package com.ruoyi.common.utils.file;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * PDF缩略图生成工具类
 * @author ruoyi
 */
public class PdfThumbnailUtils {

    /**
     * PDF缩略图宽度
     */
    private static final int THUMBNAIL_WIDTH = 200;

    /**
     * PDF缩略图高度
     */
    private static final int THUMBNAIL_HEIGHT = 260;

    /**
     * 生成PDF缩略图
     * @param file PDF文件
     * @param uploadDir 上传目录
     * @param fileName 原文件名称
     * @return 缩略图访问路径
     * @throws IOException 异常
     */
    public static String generatePdfThumbnail(MultipartFile file, String uploadDir, String fileName) throws IOException {
        // 获取PDF文件输入流
        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {

            // 渲染PDF第一页为图片
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImageWithDPI(0, 96); // 0表示第一页，96DPI

            // 缩放图片到指定尺寸
            BufferedImage thumbnail = new BufferedImage(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, BufferedImage.TYPE_INT_RGB);
            thumbnail.getGraphics().drawImage(image.getScaledInstance(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, java.awt.Image.SCALE_SMOOTH), 0, 0, null);

            // 构建缩略图文件名（原文件名+_thumbnail.jpg）
            String thumbnailFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_thumbnail.jpg";
            File thumbnailFile = new File(uploadDir + File.separator + thumbnailFileName);

            // 保存缩略图
            ImageIO.write(thumbnail, "jpg", thumbnailFile);

            // 返回缩略图的访问路径
//            return FileUploadUtils.getPathFileName(uploadDir, thumbnailFileName);
            return thumbnailFileName;
        } catch (Exception e) {
            throw new IOException("生成PDF缩略图失败", e);
        }
    }
}
