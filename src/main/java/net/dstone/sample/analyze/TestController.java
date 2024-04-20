package net.dstone.sample.analyze;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/test")
public class TestController extends BaseController {
	
	@RequestMapping("/doTestService01.do")
	public ModelAndView doTestService01(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) throws Exception{

		String name = "용시기";
		getTestService().doTestService01(name);
		
		return mav;
	}

}
