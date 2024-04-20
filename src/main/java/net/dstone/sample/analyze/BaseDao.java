package net.dstone.sample.analyze;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class BaseDao {

    @Autowired 
    @Qualifier("sqlSessionDb1") 
    private SqlSessionTemplate sqlSessionDb1;

	public SqlSessionTemplate getSqlSessionDb1() {
		return sqlSessionDb1;
	}

	public void setSqlSessionDb1(SqlSessionTemplate sqlSessionDb1) {
		this.sqlSessionDb1 = sqlSessionDb1;
	} 
    
    protected void d(Object msg) {
    	System.out.println(msg);
    }
    
    public abstract void testBaseDao(String id);

}
