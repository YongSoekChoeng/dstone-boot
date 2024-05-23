package net.dstone.analyzer; 
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import net.dstone.common.tools.analyzer.AppAnalyzer;
import net.dstone.common.utils.RequestUtil;
import net.dstone.common.utils.StringUtil;
@Controller
@RequestMapping(value = "/analyzer/analysis/*")
public class AnalysisController extends net.dstone.common.biz.BaseController { 

    /********* SVC 정의부분 시작 *********/
    @Autowired 
    private net.dstone.analyzer.ConfigurationService configurationService; 
    /********* SVC 정의부분 끝 *********/
    
    /** 
     * 분석작업 입력 
     * @param request 
     * @param model 
     * @return 
     */ 
    @RequestMapping(value = "/doAnalyzing.do") 
    public ModelAndView doAnalyzing(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, ModelAndView mav) throws Exception {
   		// 필요없는 주석들은 제거하시고 사용하시면 됩니다.
   		/************************ 뷰생성 시작 ************************/
   		if(isAjax(request)) { mav = new ModelAndView("jsonView"); }
   		/************************ 뷰생성 끝 **************************/
   		
   		/************************ 변수 선언 시작 ************************/
   		RequestUtil 						requestUtil;
   		//파라메터로 사용할 VO
   		net.dstone.analyzer.vo.SysVo		paramVo;
   		String								analyzeJobKind; 
   		/************************ 변수 선언 끝 **************************/
   		
   		/************************ 변수 정의 시작 ************************/
   		requestUtil 			= new RequestUtil(request, response);
   		/************************ 변수 정의 끝 ************************/
   		
   		/************************ 컨트롤러 로직 시작 ************************/
   		// 1. 파라메터 취합
   		paramVo					= new net.dstone.analyzer.vo.SysVo();
   		paramVo.setSYS_ID(requestUtil.getParameter("SYS_ID"));
   		analyzeJobKind	 		= requestUtil.getParameter("ANALYZE_JOB_KIND");
   		
   		// 2. 분석유틸리티 생성
   		paramVo 				= configurationService.getSys(paramVo);
   		net.dstone.common.tools.analyzer.AppAnalyzer appAnalyzer = net.dstone.common.tools.analyzer.AppAnalyzer.getInstance(paramVo.getCONF_FILE_PATH());
   		
   		// 3. 분석작업종류별 작업실행.
   		if( !StringUtil.isEmpty(analyzeJobKind) ) {
   			appAnalyzer.analyzeApp(Integer.valueOf(analyzeJobKind), false);
   		}
   		
   		boolean result 			= true;
   		
   		// 3. 결과처리
   		mav.addObject("RETURN_CD", net.dstone.common.biz.BaseController.RETURN_SUCCESS );
   		mav.addObject("returnObj", new Boolean(result)	);
   		/************************ 컨트롤러 로직 끝 ************************/
   		return mav;
    } 

} 
