package com.wkx.ssm.service.impl;

import com.wkx.ssm.mapper.ProductTypeMapper;
import com.wkx.ssm.pojo.ProductType;
import com.wkx.ssm.pojo.ProductTypeExample;
import com.wkx.ssm.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shkstart
 * @create 2022-02-08 10:33
 */
@Service("ProductTypeServiceImpl")
public class ProductTypeServiceImpl implements ProductTypeService {

    //在业务逻辑层一定会有数据访问层的对象
    @Autowired
    ProductTypeMapper productTypeMapper;

    @Override
    public List<ProductType> getAll() {
        return productTypeMapper.selectByExample(new ProductTypeExample());
    }
}
