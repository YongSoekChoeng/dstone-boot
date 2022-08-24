package net.dstone.sample.rest; 
 
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RestDeptController extends net.dstone.common.biz.BaseController { 
    

    /********* SVC 정의부분 시작 *********/
    @Autowired 
    private net.dstone.sample.dept.DeptService deptService; 
    /********* SVC 정의부분 끝 *********/
    
    /** 
     * 샘플부서정보 리스트조회 
     * @param request 
     * @param model 
     * @return 
     */ 

    @RequestMapping(value = "/rest/dept/listSampleDept/{PAGE_NUM}/{PAGE_SIZE}", method = RequestMethod.GET)
    public List<net.dstone.sample.dept.vo.SampleDeptVo> listSampleDept(@PathVariable("PAGE_NUM") int pAGE_NUM, @PathVariable("PAGE_SIZE") int pAGE_SIZE) {
   		
   		/************************ 변수 선언 시작 ************************/
    	List<net.dstone.sample.dept.vo.SampleDeptVo>   returnObj = null;
   		//파라메터
   		//String										ACTION_MODE;
   		//파라메터로 사용할 VO
   		net.dstone.sample.dept.vo.SampleDeptVo 					paramVo;
   		//net.dstone.sample.dept.vo.SampleDeptVo[] 				paramList;
   		/************************ 변수 선언 끝 **************************/
   		try {
   			/************************ 변수 정의 시작 ************************/
   			paramVo					= null;
   			//paramList				= null;
   			returnObj				= null;
   			/************************ 변수 정의 끝 ************************/
   			
   			/************************ 컨트롤러 로직 시작 ************************/
   			// 1. 파라메터 바인딩
   			// 일반 파라메터 받는경우
   			//ACTION_MODE				= requestUtil.getParameter("ACTION_MODE", "LIST_PLAIN");
   			// 싱글 VALUE 맵핑일 경우
   			paramVo 				= new net.dstone.sample.dept.vo.SampleDeptVo();
   			// 멀티 VALUE 맵핑일 경우
   			//paramList 			= (net.dstone.sample.dept.vo.SampleDeptVo[])bindMultiValues(requestUtil, "net.dstone.sample.dept.vo.SampleDeptVo");
   			/*** 페이징파라메터 세팅 시작 ***/
   			paramVo.setPAGE_NUM(pAGE_NUM);
   			paramVo.setPAGE_SIZE(pAGE_SIZE);
   			/*** 페이징파라메터 세팅 끝 ***/
   			// 2. 서비스 호출
   			returnObj 				= (List<net.dstone.sample.dept.vo.SampleDeptVo>)deptService.listSampleDept(paramVo);
   			// 3. 결과처리
   			/************************ 컨트롤러 로직 끝 ************************/
   		
   		} catch (Exception e) {
   			e.printStackTrace();
   		}
   		return returnObj;
    } 


} 
