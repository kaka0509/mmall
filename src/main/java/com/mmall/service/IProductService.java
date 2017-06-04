package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * Created by Skye on 2017/6/4.
 */
public interface IProductService {

    /**
     * Service层：更新或新增产品
     *
     * @param product
     * @return
     */
    ServerResponse saveOrUpdateProduct(Product product);

    /**
     * Service层 修改产品销售状态
     *
     * @param productId
     * @param status
     * @return
     */
    ServerResponse<String> setSaleStatus(Integer productId, Integer status);


    /**
     * Service层 获得product详情
     *
     * @param productId
     * @return
     */
    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);


    /**
     * 使用mybatis-pageHelper生成所有商品分页数据
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    /**
     * 后台查询产品集合(产品状态无关)
     *
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);
}
