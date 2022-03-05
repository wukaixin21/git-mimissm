package com.wkx.ssm.controller;

import com.wkx.ssm.pojo.Admin;
import com.wkx.ssm.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.security.pkcs11.Secmod;

/**
 * @author shkstart
 * @create 2022-02-04 12:09
 */
@Controller
@RequestMapping("/admin")
public class AdminAction {
    //切记，在所有的界面层，一定会有业务逻辑层的对象
    @Autowired
    AdminService adminService;

    //实现登录判断，并进行相应的跳转
    @RequestMapping("/login")
    public String login(String name, String pwd, Model model){

        Admin admin = adminService.login(name, pwd);
        if(admin != null)
        {
            //登录成功
            model.addAttribute("admin", admin);
            return "main";
        }
        else {
            //登录失败
            model.addAttribute("errmsg", "用户名或密码不正确!");
            return "login";
        }
    }
}
