package net.dstone.analyzer; 
 
import java.util.Map; 
import java.util.HashMap; 
import java.util.List; 
 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional; 
 
import net.dstone.common.biz.BaseService; 
import net.dstone.common.consts.ErrCd; 
import net.dstone.common.exception.BizException;
import net.dstone.common.utils.LogUtil; 
 
@Service 
public class ReportService extends BaseService { 
     
    LogUtil logger = getLogger(); 
     

    /********* 공통 입력/수정/삭제 DAO 정의부분 시작 *********/
    @Autowired 
    private net.dstone.analyzer.cud.AnalyzerCudDao analyzerCudDao; 
    /********* 공통 입력/수정/삭제 DAO 정의부분 끝 *********/
    /********* DAO 정의부분 시작 *********/
    @Autowired 
    private net.dstone.analyzer.ReportDao reportDao; 
    /********* DAO 정의부분 끝 *********/
    /** 
     * 종합결과 리스트조회 
     * @param paramVo 
     * @return 
     * @throws Exception 
     */ 
    public Map listOverAll(net.dstone.analyzer.vo.OverAllVo paramVo) throws BizException{ 
        // 필요없는 주석들은 제거하시고 사용하시면 됩니다.
        /************************ 변수 선언 시작 ************************/ 
        HashMap returnMap;                                      // 반환대상 맵 
        List<net.dstone.analyzer.vo.OverAllVo> list;            // 리스트 
        /************************ 변수 선언 끝 **************************/ 
        try { 
            /************************ 변수 정의 시작 ************************/ 
            returnMap = new HashMap(); 
            list = null; 
            /************************ 변수 정의 끝 **************************/ 
            
            /************************ 비즈니스로직 시작 ************************/ 
            //DAO 호출부분 구현
            list = reportDao.listOverAll(paramVo); 
            returnMap.put("returnObj", list); 
            
            /************************ 비즈니스로직 끝 **************************/ 
        } catch (Exception e) { 
            String errDetailMsg = this.getClass().getName() + ".listOverAll 수행중 예외발생. 상세사항:" + e.toString(); 
            logger.error(errDetailMsg); 
            throw new BizException(ErrCd.SYS_ERR, errDetailMsg);
        } 
        return returnMap; 
    } 

} 
