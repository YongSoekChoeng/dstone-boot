package net.dstone.common.mq.rabbitmq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import net.dstone.common.biz.BaseController;
import net.dstone.common.mq.rabbitmq.service.ProducerService;
import net.dstone.common.utils.DataSet;
import net.dstone.common.utils.StringUtil;

@Controller
@ConditionalOnProperty(name = "spring.rabbitmq.enabled", havingValue = "true")
@RequestMapping(value = "/dstone-mq/rabbitmq/producer/*")
public class ProducerController extends BaseController {

	@Autowired
	ProducerService producerService;

    /** 
     * 생산자(Proceduer)가 메시지를 전송.
     * @param request 
     * @param model 
     * @return 
     */ 
    @RequestMapping(value = "/sendMessage.do") 
    public ModelAndView send(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, ModelAndView mav) throws Exception {
   		
   		/************************ 변수 선언 시작 ************************/
   		net.dstone.common.utils.RequestUtil requestUtil;
   		String exchangeId;
   		String routingKey;
   		DataSet messageDs;
   		/************************ 변수 선언 끝 **************************/
   		
   		/************************ 변수 정의 시작 ************************/
   		requestUtil 			= new net.dstone.common.utils.RequestUtil(request, response);
   		exchangeId				= requestUtil.getParameter("exchangeId", "");
   		routingKey				= requestUtil.getParameter("routingKey", "");
   		messageDs				= new DataSet();
   		/************************ 변수 정의 끝 ************************/
   		
   		/************************ 컨트롤러 로직 시작 ************************/
   		if( StringUtil.isEmpty(exchangeId) ) {
   			throw new Exception("exchange아이디가 null 혹은 빈 값입니다. exchangeId["+exchangeId+"]");
   		}
   		/*
   		if( StringUtil.isEmpty(routingKey) ) {
   			throw new Exception("라우팅키가 null 혹은 빈 값입니다. routingKey["+routingKey+"]");
   		}
   		*/
   		// 1. 파라메터 바인딩
   		messageDs.setDatum("message", requestUtil.getParameter("message", ""));
   		// 2. 서비스 호출
   		if( !StringUtil.isEmpty(messageDs.getDatum("message")) ) {
   			producerService.sendMessage(exchangeId, routingKey, messageDs);
   		}
   		// 3. 결과처리
   		/************************ 컨트롤러 로직 끝 ************************/
   		mav.setView(null);
   		return mav;
    }
    
}
