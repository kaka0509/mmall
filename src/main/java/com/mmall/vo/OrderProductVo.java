package com.mmall.vo;


import java.math.BigDecimal;
import java.util.List;

/**
 * 预览订单内商品
 * Created by Skye on 2017/6/7.
 */
public class OrderProductVo {

    //订单详情列表
    private List<OrderItemVo> orderItemVoList;
    //总价
    private BigDecimal productTotalPrice;
    //图片前缀
    private String imageHost;

    public List<OrderItemVo> getOrderItemVoList() {
        return orderItemVoList;
    }

    public void setOrderItemVoList(List<OrderItemVo> orderItemVoList) {
        this.orderItemVoList = orderItemVoList;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
