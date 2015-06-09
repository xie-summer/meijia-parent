package com.simi.action.dict;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.github.pagehelper.PageInfo;
import com.simi.action.BaseController;
import com.simi.oa.auth.AuthPassport;
import com.simi.oa.common.ConstantOa;
import com.simi.po.model.dict.DictAd;
import com.simi.service.dict.AdService;
import com.meijia.utils.RandomUtil;
import com.meijia.utils.TimeStampUtil;

@Controller
@RequestMapping(value = "/dict")
public class DictAdController extends BaseController {

    @Autowired
    private AdService adService;

	@AuthPassport
	@RequestMapping(value = "/ad", method = { RequestMethod.GET })
	public String list(HttpServletRequest request, Model model
			) {

		model.addAttribute("requestUrl", request.getServletPath());
		model.addAttribute("requestQuery", request.getQueryString());

		model.addAttribute("searchModel");
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);

		PageInfo result=adService.searchVoListPage( pageNo, pageSize);

		model.addAttribute("contentModel",result);

		return "dict/ad";
	}


	@AuthPassport
	@RequestMapping(value = "/adForm", method = { RequestMethod.GET })
	public String adForm(Model model,
			@RequestParam(value = "id") Long id,
			HttpServletRequest request) {

		if (id == null) {
			id = 0L;
		}

		DictAd dictAd = adService.initAd();
		if (id != null && id > 0) {
			dictAd = adService.selectByPrimaryKey(id);


		}

		model.addAttribute("adModel", dictAd);

		return "dict/adForm";
	}


	@AuthPassport
	@RequestMapping(value = "/adForm", method = { RequestMethod.POST })
	public String doAdForm(HttpServletRequest request, Model model,
			@ModelAttribute("dictAd") DictAd dictAd,
			BindingResult result) throws IOException {

		Long id = Long.valueOf(request.getParameter("id"));

		 //创建一个通用的多部分解析器.
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        String path = request.getSession().getServletContext().getRealPath("/WEB-INF/upload/ad");
        String addr = request.getRemoteAddr();
        int port = request.getServerPort();
		 if(multipartResolver.isMultipart(request))
	        {
	             //判断 request 是否有文件上传,即多部分请求...
	            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)(request);
	            Iterator<String> iter = multiRequest.getFileNames();
	            while(iter.hasNext()){
	                MultipartFile file = multiRequest.getFile(iter.next());
	                if(file!=null && !file.isEmpty()){

	                	  String fileName = file.getOriginalFilename();
	                      String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
	                      // 新的图片文件名 = 获取时间戳+随机六位数+"."图片扩展名
	                      String before = TimeStampUtil.getNow()+String.valueOf(RandomUtil.randomNumber());
	                      String newFileName = String.valueOf(before+"."+extensionName);
	                     //获取系统发布后upload路径
	                     FileUtils.copyInputStreamToFile(file.getInputStream(), new File(path,newFileName));
	                     String imgUrl =  "/simi-oa/upload/ad/"+newFileName;
	                     dictAd.setImgUrl(imgUrl);

	                     //生成缩略图
	                     BufferedImage bufferedImage1 = new BufferedImage(60,60,BufferedImage.TYPE_INT_BGR);
	                     BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
	                     Image image = bufferedImage.getScaledInstance(60,60,Image.SCALE_DEFAULT);
	                     bufferedImage1.getGraphics().drawImage(image, 0, 0, null);
	                     String newFileName1 = String.valueOf(before+"_small."+extensionName);

	                     FileOutputStream out = new FileOutputStream(path + "/" + newFileName1);
	                     ImageIO.write(bufferedImage1, "jpg",out);//把图片输出
	                }
	            }
	        }

		//更新或者新增
		if (id != null && id > 0) {
			dictAd.setUpdateTime(TimeStampUtil.getNow() / 1000);
			adService.updateByPrimaryKeySelective(dictAd);
		} else {
			dictAd.setId(Long.valueOf(request.getParameter("id")));
			//dictAd.setImgUrl("");
			dictAd.setAddTime(TimeStampUtil.getNow()/1000);
			dictAd.setUpdateTime(0L);
			dictAd.setEnable((short)0);
			//dictAd.setEnable((short)0);

			adService.insertSelective(dictAd);
		}



		return "redirect:ad";
	}

	//删除
/*	@RequestMapping(value ="/delete/{id}", method = {RequestMethod.GET})
	public String deleterAdminRole(Model model,@PathVariable(value="id") String id,HttpServletRequest response)  {
		Long ids = 0L;
		if (id != null && NumberUtils.isNumber(id)) {
			ids = Long.valueOf(id.trim());
		}
		String path = "redirect:/dict/ad";
		int result = adService.deleteByPrimaryKey(ids);
		if(result>0){
			return path;
		}else{
			return "error";
		}
	}*/

}
