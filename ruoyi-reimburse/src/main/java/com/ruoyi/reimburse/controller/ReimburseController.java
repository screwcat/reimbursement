package com.ruoyi.reimburse.controller;

import com.ruoyi.reimburse.domain.Reimburse;
import com.ruoyi.reimburse.domain.ReimburseAttachment;
import com.ruoyi.reimburse.domain.ReimburseDetail;
import com.ruoyi.reimburse.service.IReimburseService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 报销申请Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/reimburse")
public class ReimburseController extends BaseController
{
    @Autowired
    private IReimburseService reimburseService;

    /**
     * 查询报销申请列表
     */
//    @PreAuthorize("@ss.hasPermi('reimburse:list')")
    @GetMapping("/list")
    public TableDataInfo list(Reimburse reimburse)
    {
        startPage();
        List<Reimburse> list = reimburseService.selectReimburseList(reimburse);
//        List<Reimburse> list = new ArrayList<>();
        return getDataTable(list);
    }

    /**
     * 导出报销申请列表
     */
    @PreAuthorize("@ss.hasPermi('reimburse:export')")
    @Log(title = "报销申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Reimburse reimburse)
    {
        List<Reimburse> list = reimburseService.selectReimburseList(reimburse);
        ExcelUtil<Reimburse> util = new ExcelUtil<Reimburse>(Reimburse.class);
        util.exportExcel(response, list, "报销申请数据");
    }

    /**
     * 获取报销申请详细信息
     */
    @PreAuthorize("@ss.hasPermi('reimburse:query')")
    @GetMapping(value = "/{reimburseId}")
    public AjaxResult getInfo(@PathVariable("reimburseId") Long reimburseId)
    {
        Map<String, Object> detail = reimburseService.selectReimburseDetail(reimburseId);
        return AjaxResult.success(detail);
    }

    /**
     * 新增报销申请
     */
    @PreAuthorize("@ss.hasPermi('reimburse:add')")
    @Log(title = "报销申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Map<String, Object> params)
    {
        Reimburse reimburse = (Reimburse) params.get("reimburse");
        List<ReimburseDetail> detailList = (List<ReimburseDetail>) params.get("detailList");
        List<ReimburseAttachment> attachmentList = (List<ReimburseAttachment>) params.get("attachmentList");
        return toAjax(reimburseService.insertReimburse(reimburse, detailList, attachmentList));
    }

    /**
     * 修改报销申请
     */
    @PreAuthorize("@ss.hasPermi('reimburse:edit')")
    @Log(title = "报销申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Map<String, Object> params)
    {
        Reimburse reimburse = (Reimburse) params.get("reimburse");
        List<ReimburseDetail> detailList = (List<ReimburseDetail>) params.get("detailList");
        List<ReimburseAttachment> attachmentList = (List<ReimburseAttachment>) params.get("attachmentList");
        return toAjax(reimburseService.updateReimburse(reimburse, detailList, attachmentList));
    }

    /**
     * 删除报销申请
     */
    @PreAuthorize("@ss.hasPermi('reimburse:remove')")
    @Log(title = "报销申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{reimburseIds}")
    public AjaxResult remove(@PathVariable Long[] reimburseIds)
    {
        return toAjax(reimburseService.deleteReimburseByIds(reimburseIds));
    }

    /**
     * 提交审批
     */
    @PreAuthorize("@ss.hasPermi('reimburse:submit')")
    @Log(title = "报销申请", businessType = BusinessType.UPDATE)
    @PostMapping("/submit/{reimburseId}")
    public AjaxResult submitApproval(@PathVariable("reimburseId") Long reimburseId)
    {
        return toAjax(reimburseService.submitApproval(reimburseId));
    }

    /**
     * 撤销申请
     */
    @PreAuthorize("@ss.hasPermi('reimburse:cancel')")
    @Log(title = "报销申请", businessType = BusinessType.UPDATE)
    @PostMapping("/cancel/{reimburseId}")
    public AjaxResult cancelApplication(@PathVariable("reimburseId") Long reimburseId)
    {
        return toAjax(reimburseService.cancelApplication(reimburseId));
    }

    /**
     * 上传票据影像
     * （注：若依已有文件上传工具类，此处简化，实际需结合若依FileController）
     */
    @PreAuthorize("@ss.hasPermi('reimburse:upload')")
    @Log(title = "报销申请", businessType = BusinessType.UPLOAD)
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file) throws IOException {
        // 调用若依文件上传工具类，返回文件路径等信息
        // 示例：

        String filePath = FileUploadUtils.upload(RuoYiConfig.getUploadPath(), file);
        ReimburseAttachment attachment = new ReimburseAttachment();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFilePath(filePath);
        attachment.setFileSize(file.getSize());
        attachment.setFileType(file.getContentType());
        return AjaxResult.success(attachment);

//        return AjaxResult.success("文件上传成功");
    }
}
