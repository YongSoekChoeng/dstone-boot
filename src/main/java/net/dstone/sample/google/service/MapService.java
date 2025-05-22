package net.dstone.sample.google.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.dstone.common.biz.BaseService;
import net.dstone.common.config.ConfigProperty;

@Service
public class MapService extends BaseService {

	@Autowired 
	ConfigProperty configProperty; // 프로퍼티 가져오는 bean

}
