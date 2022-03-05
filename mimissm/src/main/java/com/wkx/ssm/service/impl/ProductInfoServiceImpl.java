package com.wkx.ssm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wkx.ssm.mapper.ProductInfoMapper;
import com.wkx.ssm.pojo.ProductInfo;
import com.wkx.ssm.pojo.ProductInfoExample;
import com.wkx.ssm.pojo.vo.ProductInfoVo;
import com.wkx.ssm.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shkstart
 * @create 2022-02-04 14:31
 */
@Service
public class ProductInfoServiceImpl implements ProductInfoService {
    //切记：业务逻辑层中一定有数据访问层的对象
    @Autowired
    ProductInfoMapper productInfoMapper;
    @Override
    public List<ProductInfo> getAll() {


        return productInfoMapper.selectByExample(new ProductInfoExample());
    }

    @Override
    public PageInfo splitPage(int pageNum, int pageSize) {
        //分页插件使用PageHelper工具类完成分页设置
        PageHelper.startPage(pageNum, pageSize);

        //进行PageInfo的数据封装
        //进行有条件的查询操作，必须要创建ProductInfoExample对象
        ProductInfoExample example = new ProductInfoExample();
        //设置排序，按主键降序排列
        example.setOrderByClause("p_id desc");
        //设置完排序后，取集合，切记：一定在取集合之前，设置PageHelper.startPage(pageNum,pageSize)
        List<ProductInfo> list = productInfoMapper.selectByExample(example);
        //将查询到的集合封装进PageInfo对象中
        PageInfo<ProductInfo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public int save(ProductInfo info) {

        return productInfoMapper.insert(info);
    }

    @Override
    public ProductInfo getById(int pid) {
        return productInfoMapper.selectByPrimaryKey(pid);
    }

    @Override
    public int update(ProductInfo info) {
        return productInfoMapper.updateByPrimaryKey(info);
    }

    @Override
    public int delete(int pid) {
        return productInfoMapper.deleteByPrimaryKey(pid);
    }

    @Override
    public int deleteBatch(String[] ids) {
        return productInfoMapper.deleteBatch(ids);
    }

    @Override
    public List<ProductInfo> selectCondition(ProductInfoVo vo) {
        return productInfoMapper.selectCondition(vo);
    }

    @Override
    public PageInfo<ProductInfo> splitPageVo(ProductInfoVo vo, int pageSize) {
        //取出集合之前，先要设置PageHelper.startPage()
        PageHelper.startPage(vo.getPage(), pageSize);
        List<ProductInfo> list = productInfoMapper.selectCondition(vo);
        return new PageInfo<>(list);
    }

}
