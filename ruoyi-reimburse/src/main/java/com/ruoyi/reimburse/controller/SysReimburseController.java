package com.ruoyi.reimburse.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.reimburse.domain.ReimburseRequest;
import com.ruoyi.reimburse.domain.SysReimburse;
import com.ruoyi.reimburse.service.ISysReimburseService;
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
 * 报销申请单主Controller
 *
 * @author ruoyi
 * @date 2026-03-07
 */
@RestController
@RequestMapping("/reimburse")
public class SysReimburseController extends BaseController
{
    @Autowired
    private ISysReimburseService sysReimburseService;

    /**
     * 查询报销申请单主列表
     */
    @PreAuthorize("@ss.hasPermi('system:reimburse:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysReimburse sysReimburse)
    {
        startPage();
        List<SysReimburse> list = sysReimburseService.selectSysReimburseList(sysReimburse);
        return getDataTable(list);
    }

    /**
     * 导出报销申请单主列表
     */
    @PreAuthorize("@ss.hasPermi('system:reimburse:export')")
    @Log(title = "报销申请单主", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysReimburse sysReimburse)
    {
        List<SysReimburse> list = sysReimburseService.selectSysReimburseList(sysReimburse);
        ExcelUtil<SysReimburse> util = new ExcelUtil<SysReimburse>(SysReimburse.class);
        util.exportExcel(response, list, "报销申请单主数据");
    }

    /**
     * 获取报销申请单主详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:reimburse:query')")
    @GetMapping(value = "/{reimburseId}")
    public AjaxResult getInfo(@PathVariable("reimburseId") Long reimburseId)
    {
        return success(sysReimburseService.getReimburseInfo(reimburseId));
    }

    /**
     * 新增报销申请单主
     */
    @PreAuthorize("@ss.hasPermi('system:reimburse:add')")
    @Log(title = "报销申请单主", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysReimburse sysReimburse)
    {
        return toAjax(sysReimburseService.insertSysReimburse(sysReimburse));
    }

    /**
     * 修改报销申请单主
     */
    @PreAuthorize("@ss.hasPermi('system:reimburse:edit')")
    @Log(title = "修改申请单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ReimburseRequest reimburseRequest)
    {
        return toAjax(sysReimburseService.updateSysReimburse(reimburseRequest));
    }

    /**
     * 删除报销申请单主
     */
    @PreAuthorize("@ss.hasPermi('system:reimburse:remove')")
    @Log(title = "报销申请单主", businessType = BusinessType.DELETE)
	@DeleteMapping("/{reimburseIds}")
    public AjaxResult remove(@PathVariable Long[] reimburseIds)
    {
        return toAjax(sysReimburseService.deleteSysReimburseByReimburseIds(reimburseIds));
    }

    @Log(title = "报销申请单主", businessType = BusinessType.INSERT)
    @PostMapping("/addReimburse")
    public AjaxResult addReimburse(@RequestBody ReimburseRequest reimburseRequest)
    {
        return toAjax(sysReimburseService.createReimburse(reimburseRequest));
    }

    @Log(title = "提交审核", businessType = BusinessType.UPDATE)
    @PostMapping("/submit/{reimburseId}")
    public AjaxResult submit(@PathVariable Long reimburseId)
    {
        return toAjax(sysReimburseService.submitReimburse(reimburseId));
    }

    @Log(title = "撤销申请", businessType = BusinessType.UPDATE)
    @PostMapping("/cancel/{reimburseId}")
    public AjaxResult cancel(@PathVariable Long reimburseId)
    {
        return toAjax(sysReimburseService.changeProcessState(reimburseId,"DRAFT"));
    }

    @Log(title = "更改审核状态", businessType = BusinessType.INSERT)
    @PostMapping("/changeProcessState")
    public AjaxResult changeProcessState(@RequestBody Map<String, Object> sysReimburse)
    {
        return toAjax(sysReimburseService.changeProcessState(((Number)sysReimburse.get("reimburseId")).longValue(),sysReimburse.get("processState").toString()));
    }

}
