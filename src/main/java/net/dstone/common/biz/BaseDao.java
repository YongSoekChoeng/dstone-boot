package net.dstone.common.biz;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class BaseDao extends net.dstone.common.core.BaseObject {

    @Autowired 
    @Qualifier("sqlSessionDb1") 
    protected SqlSessionTemplate sqlSessionDb1; 

}
