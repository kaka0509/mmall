package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Skye on 2017/6/4.
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;


    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product != null) {
            //将子图的第一个作为product的主图
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }

            if (product.getId() != null) {
                int rowcount = productMapper.updateByPrimaryKey(product);
                if (rowcount > 0) {
                    return ServerResponse.createBySuccess("更新产品成功");
                } else {
                    return ServerResponse.createByErrorMessage("更新产品失败");
                }


            } else {
                int rowcount = productMapper.insert(product);
                if (rowcount > 0) {
                    return ServerResponse.createBySuccess("新增产品成功");
                } else {
                    return ServerResponse.createByErrorMessage("新增产品失败");
                }
            }


        }
        return ServerResponse.createByErrorMessage("新增或更新产品参数不正确");
    }


    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("修改产品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("修改产品销售状态失败");
    }


    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("产品已经下架或者删除");
        } else {
            ProductDetailVo productDetailVo = assembleProductDetailVo(product);
            return ServerResponse.createBySuccess(productDetailVo);
        }

    }


    /**
     * 组装商品VO 减少直接返回POJO的数据量
     *
     * @param product
     * @return
     */
    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetailVo.setParentCategoryId(0);//默认根结点
        } else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }


    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        //pageHelper使用步骤：
        //1. startPage -start
        PageHelper.startPage(pageNum, pageSize);
        //2. 填充自己的sql查询逻辑,pageHelper将采用AOP方式增加limit，offset关键字
        //在进行查询时已经被拦截，结果并不是全部记录
        List<Product> productList = productMapper.selectList();
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem : productList) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        //3. pageHelper -将查出来的productList放进PageInfo构造函数中
        // 只有发起dao层请求的才会添加分页相关信息(利用productList.size()算出来)
        // 不允许用new PageInfo(productListVoList) 会丢失分页参数
        PageInfo pageResult = new PageInfo(productList);
        // 重置PageInfo中的返回结果List
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }


    /**
     * 组装分页用的ProductListVo
     *
     * @param product
     * @return
     */
    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());

        return productListVo;
    }

    /**
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return 后台查询产品集合(产品状态无关)
     */
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectByNameAndProductId(productName, productId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem : productList) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }
}
