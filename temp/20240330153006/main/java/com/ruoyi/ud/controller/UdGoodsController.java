package com.ruoyi.ud.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.ud.domain.UdGoods;
import com.ruoyi.ud.service.IUdGoodsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商品管理Controller
 * 
 * @author ruoyi
 * @date 2024-03-29
 */
@RestController
@RequestMapping("/setting/goods")
public class UdGoodsController extends BaseController
{
    @Autowired
    private IUdGoodsService udGoodsService;

    /**
     * 查询商品管理列表
     */
    @PreAuthorize("@ss.hasPermi('setting:goods:list')")
    @GetMapping("/list")
    public TableDataInfo list(UdGoods udGoods)
    {
        startPage();
        List<UdGoods> list = udGoodsService.selectUdGoodsList(udGoods);
        return getDataTable(list);
    }

    /**
     * 导出商品管理列表
     */
    @PreAuthorize("@ss.hasPermi('setting:goods:export')")
    @Log(title = "商品管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UdGoods udGoods)
    {
        List<UdGoods> list = udGoodsService.selectUdGoodsList(udGoods);
        ExcelUtil<UdGoods> util = new ExcelUtil<UdGoods>(UdGoods.class);
        util.exportExcel(response, list, "商品管理数据");
    }

    /**
     * 获取商品管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('setting:goods:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(udGoodsService.selectUdGoodsById(id));
    }

    /**
     * 新增商品管理
     */
    @PreAuthorize("@ss.hasPermi('setting:goods:add')")
    @Log(title = "商品管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UdGoods udGoods)
    {
        return toAjax(udGoodsService.insertUdGoods(udGoods));
    }

    /**
     * 修改商品管理
     */
    @PreAuthorize("@ss.hasPermi('setting:goods:edit')")
    @Log(title = "商品管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UdGoods udGoods)
    {
        return toAjax(udGoodsService.updateUdGoods(udGoods));
    }

    /**
     * 删除商品管理
     */
    @PreAuthorize("@ss.hasPermi('setting:goods:remove')")
    @Log(title = "商品管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(udGoodsService.deleteUdGoodsByIds(ids));
    }
}
