package net.dstone.analyzer; 
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;

import net.dstone.common.utils.BeanUtil;
import net.dstone.common.utils.DateUtil;
import net.dstone.common.utils.RequestUtil;
import net.dstone.common.utils.StringUtil;
@Controller
@RequestMapping(value = "/analyzer/report/*")
public class ReportController extends net.dstone.common.biz.BaseController { 
    

    /********* SVC 정의부분 시작 *********/
    @Autowired 
    private net.dstone.analyzer.ReportService reportService; 
    /********* SVC 정의부분 끝 *********/
    
    /** 
     * 종합결과 리스트조회 
     * @param request 
     * @param model 
     * @return 
     */ 
    @RequestMapping(value = "/listOverAll.do") 
    public ModelAndView listOverAll(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, ModelAndView mav) throws Exception {
   		// 필요없는 주석들은 제거하시고 사용하시면 됩니다.
   		/************************ 뷰생성 시작 ************************/
   		if(isAjax(request)) { mav = new ModelAndView("jsonView"); }
   		/************************ 뷰생성 끝 **************************/
   		
   		/************************ 변수 선언 시작 ************************/
   		RequestUtil 									requestUtil;
   		Map 											returnObj;
   		//파라메터
   		//String										ACTION_MODE;
   		//파라메터로 사용할 VO
   		net.dstone.analyzer.vo.OverAllVo 					paramVo;
   		//net.dstone.analyzer.vo.OverAllVo[] 				paramList;
   		/************************ 변수 선언 끝 **************************/
   		
   		/************************ 변수 정의 시작 ************************/
   		requestUtil 			= new RequestUtil(request, response);
   		paramVo					= null;
   		//paramList				= null;
   		returnObj				= null;
   		/************************ 변수 정의 끝 ************************/
   		
   		/************************ 컨트롤러 로직 시작 ************************/
   		// 1. 파라메터 바인딩
   		paramVo 				= (net.dstone.analyzer.vo.OverAllVo)bindSingleValue(requestUtil, new net.dstone.analyzer.vo.OverAllVo());

   		// 2. 서비스 호출
   		returnObj 				= reportService.listOverAll(paramVo);
   		
   		// 3. 결과처리
   		mav.addObject("returnObj", returnObj	);
   		/************************ 컨트롤러 로직 끝 ************************/
   		return mav;
    } 

} 
