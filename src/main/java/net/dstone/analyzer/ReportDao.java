package net.dstone.analyzer; 
 
import java.util.List; 
import java.util.Map; 
 
import org.springframework.stereotype.Repository; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.beans.factory.annotation.Qualifier; 
import org.mybatis.spring.SqlSessionTemplate; 
 
@Repository 
public class ReportDao extends net.dstone.common.biz.BaseDao { 

    /* 
     * 종합결과 리스트조회(카운트) 
     */ 
    public int listOverAllCount(net.dstone.analyzer.vo.OverAllVo overAllVo) throws Exception { 
        Object returnObj = sqlSessionAnalyzer.selectOne("net.dstone.analyzer.ReportDao.listOverAllCount", overAllVo); 
        if (returnObj == null) {
            return 0;
        } else {
            return ((Integer) returnObj).intValue();
        }
    } 
    /* 
     * 종합결과 리스트조회 
     */ 
    public List<net.dstone.analyzer.vo.OverAllVo> listOverAll(net.dstone.analyzer.vo.OverAllVo overAllVo) throws Exception { 
        List<net.dstone.analyzer.vo.OverAllVo> list = sqlSessionAnalyzer.selectList("net.dstone.analyzer.ReportDao.listOverAll", overAllVo); 
        return list; 
    } 

} 
