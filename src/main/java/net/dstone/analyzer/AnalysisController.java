package net.dstone.analyzer; 
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import net.dstone.analyzer.taskitem.AnalysisItem;
import net.dstone.common.queue.QueueHandler;
import net.dstone.common.queue.QueueItem;
import net.dstone.common.task.TaskHandler;
import net.dstone.common.task.TaskHandler.TaskReport;
import net.dstone.common.tools.analyzer.AppAnalyzer;
import net.dstone.common.utils.RequestUtil;
@Controller
@RequestMapping(value = "/analyzer/analysis/*")
public class AnalysisController extends net.dstone.common.biz.BaseController { 

	private static String QUEUE_HANDLER_ID = "doAnalyzingQueue";
	
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
   		RequestUtil 			requestUtil;
   		String                  sysId;
   		String[]				analyzeJobKindArr; 
   		Iterator<Integer> 		keys;
   		/************************ 변수 선언 끝 **************************/
   		
   		/************************ 변수 정의 시작 ************************/
   		requestUtil 			= new RequestUtil(request, response);
   		sysId					= null;
   		analyzeJobKindArr	 	= null;
   		keys					= null;
   		/************************ 변수 정의 끝 ************************/
   		
   		/************************ 컨트롤러 로직 시작 ************************/
   		// 1. 파라메터 취합
   		sysId					= requestUtil.getParameter("SYS_ID");
   		analyzeJobKindArr	 	= requestUtil.getParameterValues("ANALYZE_JOB_KIND");
   		
   		// 2. 분석작업종류별 작업실행.
   		if( analyzeJobKindArr != null && analyzeJobKindArr.length > 0 ) {
   			net.dstone.analyzer.vo.SysVo paramVo = new net.dstone.analyzer.vo.SysVo();
   	   		paramVo.setSYS_ID(sysId);
   	   		paramVo = configurationService.getSys(paramVo);
   			if(paramVo != null) {
   				// 2-1. 기존 작업 중지
   				keys = AppAnalyzer.JOB_KIND_MAP.keySet().iterator();
   				while( keys.hasNext() ) {
   					String executorServiceId = AppAnalyzer.JOB_KIND_MAP.get(keys.next());
   					TaskHandler.getInstance().removeExecutorService(executorServiceId);
   				}

   				// 2-2. 새 작업 시작
   				int queueCheckInterval = 5*1000; 
   				int queueItemSizeByOne = 1;
   				
   				for(int i=0; i<analyzeJobKindArr.length; i++) {
   					String analyzeJobKind = analyzeJobKindArr[i];
   					String executorServiceId = "doAnalyzing";
   					String executorServiceItemId = executorServiceId + "-" + String.valueOf(i);
   	   				String confFilePath = paramVo.getCONF_FILE_PATH();
   	   				QueueHandler.getInstance(QUEUE_HANDLER_ID, queueCheckInterval, queueItemSizeByOne).addQueue(new QueueItem() {
   						@Override
   						public void doTheJob() {
   							TaskHandler taskHandler = TaskHandler.getInstance();
   							try {
   								if( !taskHandler.isExecutorServiceExists(executorServiceId) ) {
   									taskHandler.addSingleExecutorService(executorServiceId);
   								}
   	   							AnalysisItem analysisItem = new AnalysisItem();
   	   							analysisItem.setId(executorServiceItemId);
	   	    	   				analysisItem.setAnalyzeJobKind(Integer.valueOf(analyzeJobKind));
	   	    	   				analysisItem.setConfigFilePath(confFilePath);
	   	    	   				taskHandler.doTheSyncTask(executorServiceId, analysisItem);
							} catch (Exception e) {
								e.printStackTrace();
							}
   						}
   					});
   				}
   			}
   		}
   		
   		boolean result 			= true;
   		
   		// 3. 결과처리
   		mav.addObject("RETURN_CD", net.dstone.common.biz.BaseController.RETURN_SUCCESS );
   		mav.addObject("returnObj", new Boolean(result)	);
   		/************************ 컨트롤러 로직 끝 ************************/
   		return mav;
    }
    

    /** 
     * 분석작업 모니터링 
     * @param request 
     * @param model 
     * @return 
     */ 
    @RequestMapping(value = "/doMonitoring.do") 
    public ModelAndView doMonitoring(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, ModelAndView mav) throws Exception {
   		// 필요없는 주석들은 제거하시고 사용하시면 됩니다.
   		/************************ 뷰생성 시작 ************************/
   		if(isAjax(request)) { mav = new ModelAndView("jsonView"); }
   		/************************ 뷰생성 끝 **************************/
   		
   		/************************ 변수 선언 시작 ************************/
   		RequestUtil 						requestUtil;
   		//파라메터로 사용할 VO
   		String[]							analyzeJobKindArr; 
   		ArrayList<HashMap<String, String>>	taskReportList;
   		String								isCompleted;
   		/************************ 변수 선언 끝 **************************/
   		
   		/************************ 변수 정의 시작 ************************/
   		requestUtil 			= new RequestUtil(request, response);
   		taskReportList			= new ArrayList<HashMap<String, String>>();
   		isCompleted				= "N";
   		/************************ 변수 정의 끝 ************************/
   		
   		/************************ 컨트롤러 로직 시작 ************************/
   		// 1. 파라메터 취합
   		analyzeJobKindArr	 	= requestUtil.getParameterValues("ANALYZE_JOB_KIND");
   		
   		// 2. 분석작업종류별 모니터링실행.
		int limitSeconds = 10;
		for( int i=0; i<limitSeconds; i++) {
			if( TaskHandler.getInstance().getTaskItemCount() > 0 ) {
				break;
			}
			Thread.sleep(1 * 1000);
		}
		
		int analyzeJobKindNum = 0;
		int analyzeJobKindCompletedNum = 0;
		
		Iterator<Integer> keys = AppAnalyzer.JOB_KIND_MAP.keySet().iterator();
		while(keys.hasNext()) {
			Integer key = keys.next();
			String executorServiceId = AppAnalyzer.JOB_KIND_MAP.get(key);
			HashMap<String, String> taskReportMap = new HashMap<String, String>();
			TaskReport taskReport = TaskHandler.getInstance().getTaskReport(executorServiceId);
			if( taskReport != null && taskReport.getRate() != null ) {
				taskReportMap.put("taskId", executorServiceId);
				taskReportMap.put("taskRate", taskReport.getRate().toPlainString());
				taskReportList.add(taskReportMap);
				if( taskReport.getRate().intValue() > 0 ) {
					analyzeJobKindNum++;
				}
				if( taskReport.getRate().intValue() == 100 ) {
					analyzeJobKindCompletedNum++;
				}
			}
		}
		
		if( analyzeJobKindNum == analyzeJobKindCompletedNum && QueueHandler.getInstance(QUEUE_HANDLER_ID).getQueueSize() == 0 ) {
   			isCompleted = "Y";
   		}

debug("isCompleted["+isCompleted+"]" + " analyzeJobKindNum["+analyzeJobKindNum+"]" + " analyzeJobKindCompletedNum["+analyzeJobKindCompletedNum+"]" + " getQueueSize["+QueueHandler.getInstance(QUEUE_HANDLER_ID).getQueueSize()+"]");

   		TaskHandler.getInstance().checkExecutorServiceAll();
   		
   		// 3. 결과처리
   		mav.addObject("RETURN_CD", net.dstone.common.biz.BaseController.RETURN_SUCCESS );
   		mav.addObject("isCompleted", isCompleted);
   		mav.addObject("returnObj", taskReportList);
   		/************************ 컨트롤러 로직 끝 ************************/
   		return mav;
    }

} 
