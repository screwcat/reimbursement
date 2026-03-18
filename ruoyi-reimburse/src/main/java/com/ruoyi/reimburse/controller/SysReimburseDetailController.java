package com.ruoyi.reimburse.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.reimburse.domain.SysReimburseDetail;
import com.ruoyi.reimburse.service.ISysReimburseDetailService;
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
 * 报销费用明细Controller
 *
 * @author ruoyi
 * @date 2026-03-11
 */
@RestController
@RequestMapping("/system/detail")
public class SysReimburseDetailController extends BaseController
{
    @Autowired
    private ISysReimburseDetailService sysReimburseDetailService;

    /**
     * 查询报销费用明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysReimburseDetail sysReimburseDetail)
    {
        startPage();
        List<SysReimburseDetail> list = sysReimburseDetailService.selectSysReimburseDetailList(sysReimburseDetail);
        return getDataTable(list);
    }

    /**
     * 导出报销费用明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:detail:export')")
    @Log(title = "报销费用明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysReimburseDetail sysReimburseDetail)
    {
        List<SysReimburseDetail> list = sysReimburseDetailService.selectSysReimburseDetailList(sysReimburseDetail);
        ExcelUtil<SysReimburseDetail> util = new ExcelUtil<SysReimburseDetail>(SysReimburseDetail.class);
        util.exportExcel(response, list, "报销费用明细数据");
    }

    /**
     * 获取报销费用明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:detail:query')")
    @GetMapping(value = "/{detailId}")
    public AjaxResult getInfo(@PathVariable("detailId") Long detailId)
    {
        return success(sysReimburseDetailService.selectSysReimburseDetailByDetailId(detailId));
    }

    /**
     * 新增报销费用明细
     */
    @PreAuthorize("@ss.hasPermi('system:detail:add')")
    @Log(title = "报销费用明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysReimburseDetail sysReimburseDetail)
    {
        return toAjax(sysReimburseDetailService.insertSysReimburseDetail(sysReimburseDetail));
    }

    /**
     * 修改报销费用明细
     */
    @PreAuthorize("@ss.hasPermi('system:detail:edit')")
    @Log(title = "报销费用明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysReimburseDetail sysReimburseDetail)
    {
        return toAjax(sysReimburseDetailService.updateSysReimburseDetail(sysReimburseDetail));
    }

    /**
     * 删除报销费用明细
     */
    @PreAuthorize("@ss.hasPermi('system:detail:remove')")
    @Log(title = "报销费用明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{detailIds}")
    public AjaxResult remove(@PathVariable Long[] detailIds)
    {
        return toAjax(sysReimburseDetailService.deleteSysReimburseDetailByDetailIds(detailIds));
    }
}
