package com.wkx.ssm.service;

import com.wkx.ssm.pojo.Admin;

/**
 * @author shkstart
 * @create 2022-02-04 11:31
 */
public interface AdminService {
    //完成登录判断
    Admin login(String name,String pwd);

}
