package net.dstone.analyzer; 
 
import java.util.List; 
import java.util.Map; 
 
import org.springframework.stereotype.Repository; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.beans.factory.annotation.Qualifier; 
import org.mybatis.spring.SqlSessionTemplate; 
 
@Repository 
public class ConfigurationDao extends net.dstone.common.biz.BaseDao { 

    /* 
     * 시스템정보 리스트조회(카운트) 
     */ 
    public int listSysCount(net.dstone.analyzer.vo.SysVo sysVo) throws Exception { 
        Object returnObj = sqlSessionAnalyzer.selectOne("net.dstone.analyzer.ConfigurationDao.listSysCount", sysVo); 
        if (returnObj == null) {
            return 0;
        } else {
            return ((Integer) returnObj).intValue();
        }
    } 
    /* 
     * 시스템정보 리스트조회 
     */ 
    public List<net.dstone.analyzer.vo.SysVo> listSys(net.dstone.analyzer.vo.SysVo sysVo) throws Exception { 
        List<net.dstone.analyzer.vo.SysVo> list = sqlSessionAnalyzer.selectList("net.dstone.analyzer.ConfigurationDao.listSys", sysVo); 
        return list; 
    } 


    /* 
     * 시스템정보 상세조회 
     */ 
    public net.dstone.analyzer.vo.SysVo getSys(net.dstone.analyzer.vo.SysVo sysVo) throws Exception { 
        return (net.dstone.analyzer.vo.SysVo) sqlSessionAnalyzer.selectOne("net.dstone.analyzer.ConfigurationDao.getSys", sysVo); 
    } 

} 
