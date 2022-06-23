package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.HouseImage;
import com.atguigu.result.Result;
import com.atguigu.service.HouseImageService;
import com.atguigu.util.QiniuUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * @PROJECT_NAME: shf-parent
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/6/23 19:27
 */
@Controller
@RequestMapping("/houseImage")
public class HouseImageContorller extends BaseController {

    private static String PAGE_UPLOAD_SHOW = "house/upload";
    private static String ACTION_LIST = "redirect:/house/detail/";


    @Reference
    HouseImageService houseImageService;

    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId,
                         @PathVariable("id") Long id) {
        HouseImage houseImage = houseImageService.getById(id);
        String imageName = houseImage.getImageName();
        houseImageService.delete(id);
        QiniuUtils.deleteFileFromQiniu(imageName);
        return ACTION_LIST + houseId;
    }


    @RequestMapping("/upload/{houseId}/{type}")
    @ResponseBody
    public Result upload(@PathVariable("houseId") Long houseId,
                         @PathVariable("type") Integer type, @RequestParam("file") MultipartFile[] files) throws IOException {
        //接收上传文件再上传到七牛云
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                byte[] bytes = file.getBytes();
                //String originalFilename = file.getOriginalFilename();
                //QiniuUtils.upload2Qiniu(bytes,originalFilename);
                String newFilename = UUID.randomUUID().toString();
                //将图片上传到七牛云
                QiniuUtils.upload2Qiniu(bytes, newFilename);
                String imageUrl = "http://rdv0xys64.hn-bkt.clouddn.com/"+newFilename;
                //将图片路径信息等保存到数据库
                HouseImage houseImage = new HouseImage();
                houseImage.setHouseId(houseId);
                houseImage.setImageName(newFilename);
                houseImage.setImageUrl(imageUrl);
                houseImage.setType(type);
                houseImageService.insert(houseImage);
            }
        }
        return Result.ok();
    }

    @RequestMapping("/uploadShow/{houseId}/{type}")
    public String uploadShow(@PathVariable("houseId") Long houseId,
                             @PathVariable("type") Integer type, Map map) {
        map.put("houseId", houseId);
        map.put("type", type);
        return PAGE_UPLOAD_SHOW;
    }
}
