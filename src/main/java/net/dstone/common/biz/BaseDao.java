package net.dstone.common.biz;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class BaseDao extends net.dstone.common.core.BaseObject {

    @Autowired 
    @Qualifier("sqlSessionCommon") 
    protected SqlSessionTemplate sqlSessionCommon; 

    @Autowired 
    @Qualifier("sqlSessionSample") 
    protected SqlSessionTemplate sqlSessionSample; 

    @Autowired 
    @Qualifier("sqlSessionSampleOracle") 
    protected SqlSessionTemplate sqlSessionSampleOracle; 

    @Autowired 
    @Qualifier("sqlSessionAnalyzer") 
    protected SqlSessionTemplate sqlSessionAnalyzer; 

}
