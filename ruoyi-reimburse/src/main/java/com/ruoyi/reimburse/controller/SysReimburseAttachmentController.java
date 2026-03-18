package com.ruoyi.reimburse.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.reimburse.domain.SysReimburseAttachment;
import com.ruoyi.reimburse.service.ISysReimburseAttachmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 报销票据影像附件Controller
 *
 * @author ruoyi
 * @date 2026-03-11
 */
@RestController
@RequestMapping("/system/attachment")
public class SysReimburseAttachmentController extends BaseController
{
    @Autowired
    private ISysReimburseAttachmentService sysReimburseAttachmentService;

    /**
     * 查询报销票据影像附件列表
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysReimburseAttachment sysReimburseAttachment)
    {
        startPage();
        List<SysReimburseAttachment> list = sysReimburseAttachmentService.selectSysReimburseAttachmentList(sysReimburseAttachment);
        return getDataTable(list);
    }

    /**
     * 导出报销票据影像附件列表
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:export')")
    @Log(title = "报销票据影像附件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysReimburseAttachment sysReimburseAttachment)
    {
        List<SysReimburseAttachment> list = sysReimburseAttachmentService.selectSysReimburseAttachmentList(sysReimburseAttachment);
        ExcelUtil<SysReimburseAttachment> util = new ExcelUtil<SysReimburseAttachment>(SysReimburseAttachment.class);
        util.exportExcel(response, list, "报销票据影像附件数据");
    }

    /**
     * 获取报销票据影像附件详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:query')")
    @GetMapping(value = "/{attachmentId}")
    public AjaxResult getInfo(@PathVariable("attachmentId") Long attachmentId)
    {
        return success(sysReimburseAttachmentService.selectSysReimburseAttachmentByAttachmentId(attachmentId));
    }

    /**
     * 新增报销票据影像附件
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:add')")
    @Log(title = "报销票据影像附件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysReimburseAttachment sysReimburseAttachment)
    {
        return toAjax(sysReimburseAttachmentService.insertSysReimburseAttachment(sysReimburseAttachment));
    }

    /**
     * 修改报销票据影像附件
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:edit')")
    @Log(title = "报销票据影像附件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysReimburseAttachment sysReimburseAttachment)
    {
        return toAjax(sysReimburseAttachmentService.updateSysReimburseAttachment(sysReimburseAttachment));
    }

    /**
     * 删除报销票据影像附件
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:remove')")
    @Log(title = "报销票据影像附件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{attachmentIds}")
    public AjaxResult remove(@PathVariable Long[] attachmentIds)
    {
        return toAjax(sysReimburseAttachmentService.deleteSysReimburseAttachmentByAttachmentIds(attachmentIds));
    }
}
