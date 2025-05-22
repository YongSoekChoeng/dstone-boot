package net.dstone.sample.google.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import net.dstone.common.config.ConfigProperty;
import net.dstone.sample.google.service.MapService;

@Controller
@RequestMapping("/google/map")
@RequiredArgsConstructor
public class MapController extends net.dstone.common.biz.BaseController { 

	@Autowired 
	ConfigProperty configProperty; // 프로퍼티 가져오는 bean

    @Autowired 
    private MapService mapService;

    @RequestMapping("/initPage.do")
    public String loginPage(Model model) {
    	
    	String mapsPlatformKey = configProperty.getProperty("interface.google.maps-platform-key");
    	
    	info("mapsPlatformKey["+mapsPlatformKey+"]");
    	
        model.addAttribute("mapsPlatformKey", mapsPlatformKey);
        
        return "/sample/google/maps/map";
    }
    
}
