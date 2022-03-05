package com.wkx.ssm.test;

import com.wkx.ssm.utils.MD5Util;
import org.junit.Test;

/**
 * @author shkstart
 * @create 2022-02-04 11:14
 */
public class MyTest {
    @Test
    public void testMD5(){
        String mi = MD5Util.getMD5("000000");
        System.out.println(mi);
    }

}
