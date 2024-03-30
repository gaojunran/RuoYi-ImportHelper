package com.ruoyi.ud.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.ud.mapper.UdGoodsMapper;
import com.ruoyi.ud.domain.UdGoods;
import com.ruoyi.ud.service.IUdGoodsService;

/**
 * 商品管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-03-29
 */
@Service
public class UdGoodsServiceImpl implements IUdGoodsService 
{
    @Autowired
    private UdGoodsMapper udGoodsMapper;

    /**
     * 查询商品管理
     * 
     * @param id 商品管理主键
     * @return 商品管理
     */
    @Override
    public UdGoods selectUdGoodsById(Long id)
    {
        return udGoodsMapper.selectUdGoodsById(id);
    }

    /**
     * 查询商品管理列表
     * 
     * @param udGoods 商品管理
     * @return 商品管理
     */
    @Override
    public List<UdGoods> selectUdGoodsList(UdGoods udGoods)
    {
        return udGoodsMapper.selectUdGoodsList(udGoods);
    }

    /**
     * 新增商品管理
     * 
     * @param udGoods 商品管理
     * @return 结果
     */
    @Override
    public int insertUdGoods(UdGoods udGoods)
    {
        udGoods.setCreateTime(DateUtils.getNowDate());
        return udGoodsMapper.insertUdGoods(udGoods);
    }

    /**
     * 修改商品管理
     * 
     * @param udGoods 商品管理
     * @return 结果
     */
    @Override
    public int updateUdGoods(UdGoods udGoods)
    {
        udGoods.setUpdateTime(DateUtils.getNowDate());
        return udGoodsMapper.updateUdGoods(udGoods);
    }

    /**
     * 批量删除商品管理
     * 
     * @param ids 需要删除的商品管理主键
     * @return 结果
     */
    @Override
    public int deleteUdGoodsByIds(Long[] ids)
    {
        return udGoodsMapper.deleteUdGoodsByIds(ids);
    }

    /**
     * 删除商品管理信息
     * 
     * @param id 商品管理主键
     * @return 结果
     */
    @Override
    public int deleteUdGoodsById(Long id)
    {
        return udGoodsMapper.deleteUdGoodsById(id);
    }
}
