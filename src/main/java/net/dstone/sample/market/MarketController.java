package net.dstone.sample.market; 
 
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import net.dstone.common.utils.RequestUtil;
import net.dstone.sample.market.mart.FruitMarket;
import net.dstone.sample.market.sell.actor.AppleSeller;
@Controller
@RequestMapping(value = "/sample/market/*")
public class MarketController extends net.dstone.common.biz.BaseController { 
    

    /********* SVC 정의부분 시작 *********/
    @Autowired 
    private net.dstone.sample.UserService userService; 
    /********* SVC 정의부분 끝 *********/
    
    /** 
     * 샘플사용자정보 리스트조회 
     * @param request 
     * @param model 
     * @return 
     */ 
    @RequestMapping(value = "/fruitMarket/test.do") 
    public ModelAndView fruitMarketTest(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, ModelAndView mav) throws Exception {
   		// 필요없는 주석들은 제거하시고 사용하시면 됩니다.
   		/************************ 뷰생성 시작 ************************/
   		if(isAjax(request)) { 
   			mav = new ModelAndView("jsonView"); 
   		}else{
   			mav = new ModelAndView(""); 
   		}
   		/************************ 뷰생성 끝 **************************/
   		
   		/************************ 변수 선언 시작 ************************/
   		RequestUtil 									requestUtil;
   		Map 											returnObj;
   		FruitMarket										fruitMarket;
   		/************************ 변수 선언 끝 **************************/
   		
   		/************************ 변수 정의 시작 ************************/
   		requestUtil 			= new RequestUtil(request, response);
   		returnObj				= null;
   		fruitMarket				= new FruitMarket();
   		/************************ 변수 정의 끝 ************************/
   		
   		/************************ 컨트롤러 로직 시작 ************************/
   		fruitMarket.setSellerForTrade("sample.item.Apple", 1000, 20);
   		fruitMarket.setSellerForTrade("sample.item.Peache", 1500, 30);
   		
   		fruitMarket.setBuyerForTrade("sample.buy.actor.HouseWife", 10000);
   		
   		fruitMarket.trade("sample.buy.actor.HouseWife", "sample.item.Apple", 15);

   		/************************ 컨트롤러 로직 끝 ************************/
   		return mav;
    } 

} 
