package com.wkx.ssm.test;

import com.wkx.ssm.mapper.ProductInfoMapper;
import com.wkx.ssm.pojo.ProductInfo;
import com.wkx.ssm.pojo.vo.ProductInfoVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author shkstart
 * @create 2022-03-04 16:40
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_dao.xml","classpath:applicationContext_service.xml"})
public class MyTest1 {
    @Autowired
    ProductInfoMapper mapper;

    @Test
    public void testSelectCondition(){


    }



}
