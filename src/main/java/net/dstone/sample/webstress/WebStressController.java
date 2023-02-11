package net.dstone.sample.webstress; 
 
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import net.dstone.common.utils.SystemUtil;
@Controller
@RequestMapping(value = "/sample/webstress/*")
public class WebStressController extends net.dstone.common.biz.BaseController { 
    
	public static String TEST_FILE_ROOT;
	static {
		if(SystemUtil.getSystemProperty("os.name").toUpperCase().startsWith("WINDOW")) {
			TEST_FILE_ROOT = "D:/Temp/samplewebstress";
		}else {
			TEST_FILE_ROOT = "/tmp/samplewebstress";
		}
	}
	
    /** 
     * 샘플Queue입력 (Syc)
     * @param request 
     * @param model 
     * @return 
     */ 
    @RequestMapping(value = "/insertWebStressBySync.do") 
    public ModelAndView insertWebStressBySync(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, ModelAndView mav) {
   		
    	/************************ 변수 선언 시작 ************************/
		String filePath = TEST_FILE_ROOT;
		net.dstone.common.utils.RequestUtil requestUtil = null;
   		/************************ 변수 선언 끝 **************************/
   		try {
   			/************************ 컨트롤러 로직 시작 ************************/
   			requestUtil = new net.dstone.common.utils.RequestUtil(request, response);		
   			net.dstone.common.utils.FileUtil.makeDir(filePath);
			String id = "TEST-" + net.dstone.common.utils.DateUtil.getToDate("HHmmss-") + new net.dstone.common.utils.GuidUtil().getNewGuid();
			String fileName = id + ".log";
			String fileConts = net.dstone.common.utils.DateUtil.getToDate("yyyyMMdd-HH:mm:ss") + "에 파일내용.";
			net.dstone.common.utils.FileUtil.writeFile(filePath, fileName, fileConts);

			net.dstone.sample.webstress.WebStressQueueItem item = new net.dstone.sample.webstress.WebStressQueueItem();
			item.setId(id);
			item.setProperty("filePath", filePath);
			item.setProperty("fileName", fileName);
			item.doTheJob();

			//Thread.sleep(5*1000);

   			mav.setViewName("/sample/view/webstress/webStress");
   			/************************ 컨트롤러 로직 끝 ************************/
   		
   		} catch (Exception e) {
   			handleException(request, response, e);
   			mav.setViewName(null);
   		}
   		return mav;
    } 
    
    /** 
     * 샘플Queue입력 (Asyc)
     * @param request 
     * @param model 
     * @return 
     */ 
    @RequestMapping(value = "/insertWebStressByAsync.do") 
    public ModelAndView insertWebStressByAsync(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, ModelAndView mav) {
   		
    	/************************ 변수 선언 시작 ************************/
		String filePath = TEST_FILE_ROOT;
		net.dstone.common.utils.RequestUtil requestUtil = null;
   		/************************ 변수 선언 끝 **************************/
		try {
   			/************************ 컨트롤러 로직 시작 ************************/
			requestUtil = new net.dstone.common.utils.RequestUtil(request, response);
			net.dstone.common.utils.FileUtil.makeDir(filePath);
			String id = "TEST-" + net.dstone.common.utils.DateUtil.getToDate("HHmmss-") + new net.dstone.common.utils.GuidUtil().getNewGuid();
			String fileName = id + ".log";
			String fileConts = net.dstone.common.utils.DateUtil.getToDate("yyyyMMdd-HH:mm:ss") + "에 파일내용.";
			net.dstone.common.utils.FileUtil.writeFile(filePath, fileName, fileConts);

			net.dstone.sample.webstress.WebStressQueueItem item = new net.dstone.sample.webstress.WebStressQueueItem();
			item.setId(id);
			item.setProperty("filePath", filePath);
			item.setProperty("fileName", fileName);
			net.dstone.common.queue.QueueHandler.getInstance().addQueue(item);
			
			//Thread.sleep(5*1000);

			mav.setViewName("/sample/view/webstress/webStress");
			/************************ 컨트롤러 로직 끝 ************************/

		} catch (Exception e) {
   			handleException(request, response, e);
   			mav.setViewName(null);
   		}
   		return mav;
    } 

} 
