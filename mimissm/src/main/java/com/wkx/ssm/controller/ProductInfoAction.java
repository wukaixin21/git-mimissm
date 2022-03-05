package com.wkx.ssm.controller;

import com.github.pagehelper.PageInfo;
import com.wkx.ssm.pojo.ProductInfo;
import com.wkx.ssm.pojo.vo.ProductInfoVo;
import com.wkx.ssm.service.ProductInfoService;
import com.wkx.ssm.utils.FileNameUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.security.pkcs11.Secmod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author shkstart
 * @create 2022-02-04 14:35
 */
@Controller
@RequestMapping("/prod")
public class ProductInfoAction {
    public static final int PAGE_SIZE = 5;
    //异步上传的图片的名称
    String saveFileName = "";
    //切记：在界面层中，一定会有业务逻辑层的对象
    @Autowired
    ProductInfoService productInfoService;

    //显示全部商品不分页
    @RequestMapping("/getAll")
    public String getAll(HttpServletRequest request){
        List<ProductInfo> list = productInfoService.getAll();
        request.setAttribute("list", list);
        return "product";
    }

    //显示第1页的5条记录
    @RequestMapping("/split")
    public String split(HttpServletRequest request){
        PageInfo info = null;
        Object vo = request.getSession().getAttribute("prodVo");
        if(vo != null)
        {
            info = productInfoService.splitPageVo((ProductInfoVo)vo, PAGE_SIZE);
            request.getSession().removeAttribute("prodVo");
        }else {

            //得到第1页的数据
            info = productInfoService.splitPage(1, PAGE_SIZE);
        }
        request.setAttribute("info", info);
        return "product";
    }

    //ajax分页翻页处理
    @ResponseBody//ajax请求必备
    @RequestMapping("/ajaxSplit")
    public void ajaxSplit(ProductInfoVo vo,HttpSession session) {
        //取得当前page参数的页面的数据
        PageInfo info = productInfoService.splitPageVo(vo, PAGE_SIZE);
        session.setAttribute("info", info);
    }

    //异步ajax文件上传处理
    @ResponseBody
    @RequestMapping("/ajaxImg")
    public Object ajaxImg(MultipartFile pimage, HttpServletRequest request)
    {
        //提取生成文件名UUID+上传图片的后缀.jpg .png
        saveFileName = FileNameUtil.getUUIDFileName() + FileNameUtil.getFileType(pimage.getOriginalFilename());

        //得到项目中图片存储的路径
        String path = request.getServletContext().getRealPath("/image_big");

        //转存
        try {
            //transferTo 将上传的文件写到服务器上指定的文件中
            pimage.transferTo(new File(path + File.separator + saveFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //返回客户端JSON对象，封装图片的路径，为了在页面实现立即显示
        JSONObject object = new JSONObject();
        object.put("imgurl", saveFileName);


        return object.toString();
    }

    @RequestMapping("/save")
    public String save(ProductInfo info,Model model)
    {
        info.setpImage(saveFileName);
        info.setpDate(new Date());
        //info对象中有表单提交上来的5个数据，有异步Ajax上来的图片的名称数据，有上架时间的数据
        int num = -1;
        try {
            num = productInfoService.save(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(num > 0)
        {
            model.addAttribute("msg", "增加成功");
        }
        else
        {
            model.addAttribute("msg", "增加失败");
        }
        //清空saveFileName变量中的内容，为了下次增加或修改的异步ajax的上传处理
        saveFileName = "";
        //增加成功后应该重新访问数据库，所以跳转到分页显示的action上

        return "forward:/prod/split.action";
    }

    @RequestMapping("/one")
    public String one(int pid,ProductInfoVo vo,Model model,HttpSession session)
    {
        ProductInfo info = productInfoService.getById(pid);
        model.addAttribute("prod", info);
        //将多条件及页码放入session中，更新处理结束后分页时读取条件和页码进行处理
        session.setAttribute("prodVo", vo);
        return "update";
    }

    //更新
    @RequestMapping("/update")
    public String update(ProductInfo info,HttpServletRequest request)
    {
        //因为Ajax的异步图片上传，如果有上传过，
        //则saveFileName里有上传上来的图片的名称
        //如果没有使用异步Ajax上传过图片，则saveFileName=""
        //实体类info使用隐藏表单域上来的pImage原始图片的名称
        if (!saveFileName.equals(""))
        {
            info.setpImage(saveFileName);
        }
        //完成更新处理
        int num = -1;
        try {
            num = productInfoService.update(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(num > 0)
        {
            //此时说明更新成功
            request.setAttribute("msg", "更新成功！");
        }
        else{
            //此时说明更新失败
            request.setAttribute("msg", "更新失败!");
        }
        //处理完成更新后，saveFileName里有可能有数据，
        //而下一次更新时要使用这个变量作为判断的依据，就会出错，所以必须清空saveFileName
        saveFileName = "";

        return "forward:/prod/split.action";
    }

    //不加@ResponseBody因为还没处理结束 下一步跳转到ajax分页的action
    @RequestMapping("/delete")
    public String delete(int pid,ProductInfoVo vo,HttpServletRequest request){
        int num = -1;

        try {
            num = productInfoService.delete(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(num > 0)
        {
            request.setAttribute("msg", "删除成功!");
            request.getSession().setAttribute("deleteProdVo", vo);
        }
        else
        {
            request.setAttribute("msg", "删除失败!");
        }

        //删除结束后跳到分页显示

        return "forward:/prod/deleteAjaxSplit.action";
    }

    @ResponseBody
    @RequestMapping(value = "/deleteAjaxSplit",produces = "text/html;charset=UTF-8")
    public Object deleteAjaxSplit(HttpServletRequest request)
    {
        //取得第1页的数据
        PageInfo info = null;
        Object vo = request.getSession().getAttribute("deleteProdVo");
        if(vo != null)
        {
            info = productInfoService.splitPageVo((ProductInfoVo) vo, PAGE_SIZE);
        }
        else {

            info = productInfoService.splitPage(1, PAGE_SIZE);
        }
        request.getSession().setAttribute("info", info);
        return request.getAttribute("msg");
    }

    //批量删除商品
    @RequestMapping("/deleteBatch")
    public String deleteBatch(String pids,HttpServletRequest request)
    {
        //将上传上来的字符串截开，形成商品id的字符数组
        String[] ps = pids.split(",");


        try {
            int num = productInfoService.deleteBatch(ps);
            if(num > 0)
            {
                request.setAttribute("msg", "批量删除成功!");
            }else{
                request.setAttribute("msg", "批量删除失败!");
            }
        } catch (Exception e) {
            request.setAttribute("msg", "商品不可删除");
        }

        return "forward:/prod/deleteAjaxSplit.action";
    }

    //多条件查询功能实现
    @ResponseBody
    @RequestMapping("/condition")
    public void condition(ProductInfoVo vo,HttpSession session){
        List<ProductInfo> list = productInfoService.selectCondition(vo);
        session.setAttribute("list", list);



    }

}
