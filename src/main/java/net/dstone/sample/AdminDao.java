package net.dstone.sample; 
 
import java.util.List;

import org.springframework.stereotype.Repository; 
 
@Repository 
public class AdminDao extends net.dstone.common.biz.BaseDao { 

    /* 
     * 샘플사용자정보 리스트조회(카운트) 
     */ 
    public int listUserCount(net.dstone.sample.vo.UserVo userVo) throws Exception { 
        Object returnObj = sqlSessionSample.selectOne("net.dstone.sample.AdminDao.listUserCount", userVo); 
        if (returnObj == null) {
            return 0;
        } else {
            return ((Integer) returnObj).intValue();
        }
    } 
    /* 
     * 샘플사용자정보 리스트조회 
     */ 
    public List<net.dstone.sample.vo.UserVo> listUser(net.dstone.sample.vo.UserVo userVo) throws Exception { 
        List<net.dstone.sample.vo.UserVo> list = sqlSessionSample.selectList("net.dstone.sample.AdminDao.listUser", userVo); 
        return list; 
    } 

} 
