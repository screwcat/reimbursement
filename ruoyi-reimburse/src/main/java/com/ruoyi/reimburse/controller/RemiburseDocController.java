package com.ruoyi.reimburse.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.reimburse.domain.RemiburseDoc;
import com.ruoyi.reimburse.service.IRemiburseDocService;
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
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 报销单据主Controller
 *
 * @author ruoyi
 * @date 2026-04-03
 */
@RestController
@RequestMapping("/remiburseDoc")
public class RemiburseDocController extends BaseController
{
    @Autowired
    private IRemiburseDocService remiburseDocService;

    /**
     * 查询报销单据主列表
     */
    @PreAuthorize("@ss.hasPermi('system:doc:list')")
    @GetMapping("/list")
    public TableDataInfo list(RemiburseDoc remiburseDoc)
    {
        startPage();
        List<RemiburseDoc> list = remiburseDocService.selectRemiburseDocList(remiburseDoc);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:doc:list')")
    @GetMapping("/listSummary")
    public TableDataInfo listSummary(RemiburseDoc remiburseDoc)
    {
        startPage();
        List<RemiburseDoc> list = remiburseDocService.selectRemiburseDoclistSummary(remiburseDoc);
        return getDataTable(list);
    }

    /**
     * 导出报销单据主列表
     */
    @PreAuthorize("@ss.hasPermi('system:doc:export')")
    @Log(title = "报销单据主", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RemiburseDoc remiburseDoc)
    {
        List<RemiburseDoc> list = remiburseDocService.selectRemiburseDocList(remiburseDoc);
        ExcelUtil<RemiburseDoc> util = new ExcelUtil<RemiburseDoc>(RemiburseDoc.class);
        util.exportExcel(response, list, "报销单据主数据");
    }

    /**
     * 获取报销单据主详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:doc:query')")
    @GetMapping(value = "/{docId}")
    public AjaxResult getInfo(@PathVariable("docId") Long docId)
    {
        return success(remiburseDocService.selectRemiburseDocByDocId(docId));
    }

    /**
     * 新增报销单据主
     */
    @PreAuthorize("@ss.hasPermi('system:doc:add')")
    @Log(title = "报销单据主", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RemiburseDoc remiburseDoc)
    {
        return toAjax(remiburseDocService.insertRemiburseDoc(remiburseDoc));
    }

    /**
     * 修改报销单据主
     */
    @PreAuthorize("@ss.hasPermi('system:doc:edit')")
    @Log(title = "报销单据主", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RemiburseDoc remiburseDoc)
    {
        return toAjax(remiburseDocService.updateRemiburseDoc(remiburseDoc));
    }

    /**
     * 删除报销单据主
     */
    @PreAuthorize("@ss.hasPermi('system:doc:remove')")
    @Log(title = "报销单据主", businessType = BusinessType.DELETE)
	@DeleteMapping("/{docIds}")
    public AjaxResult remove(@PathVariable Long[] docIds)
    {
        return toAjax(remiburseDocService.deleteRemiburseDocByDocIds(docIds));
    }

    @Log(title = "提交审核", businessType = BusinessType.UPDATE)
    @PostMapping("/submit/{docId}")
    public AjaxResult submit(@PathVariable Long docId)
    {
        return toAjax(remiburseDocService.submitReimburse(docId));
    }

    @Log(title = "撤销申请", businessType = BusinessType.UPDATE)
    @PostMapping("/cancel/{docId}")
    public AjaxResult cancel(@PathVariable Long docId)
    {
        return toAjax(remiburseDocService.changeProcessState(docId,"DRAFT"));
    }

    @Log(title = "更改审核状态", businessType = BusinessType.INSERT)
    @PostMapping("/changeProcessState")
    public AjaxResult changeProcessState(@RequestBody Map<String, Object> sysReimburse)
    {
        return toAjax(remiburseDocService.changeProcessState(Long.valueOf(sysReimburse.get("docId").toString()),sysReimburse.get("processState").toString()));
    }
}
