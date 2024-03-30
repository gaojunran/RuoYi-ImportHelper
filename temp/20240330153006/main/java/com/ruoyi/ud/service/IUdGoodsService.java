package com.ruoyi.ud.service;

import java.util.List;
import com.ruoyi.ud.domain.UdGoods;

/**
 * 商品管理Service接口
 * 
 * @author ruoyi
 * @date 2024-03-29
 */
public interface IUdGoodsService 
{
    /**
     * 查询商品管理
     * 
     * @param id 商品管理主键
     * @return 商品管理
     */
    public UdGoods selectUdGoodsById(Long id);

    /**
     * 查询商品管理列表
     * 
     * @param udGoods 商品管理
     * @return 商品管理集合
     */
    public List<UdGoods> selectUdGoodsList(UdGoods udGoods);

    /**
     * 新增商品管理
     * 
     * @param udGoods 商品管理
     * @return 结果
     */
    public int insertUdGoods(UdGoods udGoods);

    /**
     * 修改商品管理
     * 
     * @param udGoods 商品管理
     * @return 结果
     */
    public int updateUdGoods(UdGoods udGoods);

    /**
     * 批量删除商品管理
     * 
     * @param ids 需要删除的商品管理主键集合
     * @return 结果
     */
    public int deleteUdGoodsByIds(Long[] ids);

    /**
     * 删除商品管理信息
     * 
     * @param id 商品管理主键
     * @return 结果
     */
    public int deleteUdGoodsById(Long id);
}
