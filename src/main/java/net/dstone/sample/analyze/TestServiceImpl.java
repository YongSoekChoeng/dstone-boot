package net.dstone.sample.analyze;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("TestService") 
public class TestServiceImpl extends BaseService implements TestService {

    /********* DAO 정의부분 시작 *********/
    @Autowired 
    private Test1Dao test1Dao;
    @Qualifier("Test2Dao")
    private Test2Dao test2Dao;
    /********* DAO 정의부분 끝 *********/
    
	@Override
	public void doTestService01(String name) {
		test1Dao.doTest1Dao01(name);
	}

	@Override
	public void testBaseService(String id) {
		test2Dao.doTest2Dao01(id);
	}

	public void testMyService(String id) {
		d(id);
	}

}
