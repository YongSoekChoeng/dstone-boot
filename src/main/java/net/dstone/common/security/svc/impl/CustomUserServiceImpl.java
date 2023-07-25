package net.dstone.common.security.svc.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.dstone.common.security.dao.CustomUserDao;
import net.dstone.common.security.svc.CustomUserService;

@Service("customUserService")
public class CustomUserServiceImpl implements CustomUserService {

    /********* DAO 정의부분 시작 *********/
	@Resource(name = "customUserDao")
    private CustomUserDao customUserDao; 
    /********* DAO 정의부분 끝 *********/
    
	@Override
	public Map<String, Object> loginProcess(Map<String, String> param) throws Exception {
		Map<String, Object> userInfo = customUserDao.selectUser(param);
		return userInfo;
	}

	@Override
	public void updateUserLoginTime(Map<String, String> param) throws Exception{
		customUserDao.updateUserLoginTime(param);
	}

	@Override
	public List<String> selectListAuthority(Map<String, String> param) throws Exception {
		return customUserDao.selectListAuthority(param);
	}

	@Override
	public List<Map<String, Object>> selectListUrlAndRole(Map<String, String> param) throws Exception {
		return customUserDao.selectListUrlAndRole(param);
	}

	@Override
	public List<Map<String, Object>> selectListUrlByRole(Map<String, String> param) throws Exception {
		return customUserDao.selectListUrlByRole(param);
	}

}
