package net.dstone.sample; 
 
import java.util.List; 
import java.util.Map; 
 
import org.springframework.stereotype.Repository; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.beans.factory.annotation.Qualifier; 
import org.mybatis.spring.SqlSessionTemplate; 
 
@Repository("userDao")
public class UserDao extends net.dstone.common.biz.BaseDao { 

    /* 
     * 샘플사용자정보 리스트조회(카운트) 
     */ 
    public int listUserCount(net.dstone.sample.vo.UserVo userVo) throws Exception { 
        Object returnObj = sqlSessionSample.selectOne("net.dstone.sample.UserDao.listUserCount", userVo); 
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
        List<net.dstone.sample.vo.UserVo> list = sqlSessionSample.selectList("net.dstone.sample.UserDao.listUser", userVo); 
        return list; 
    } 

} 
