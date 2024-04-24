package net.dstone.sample.analyze;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("TestService") 
public class TestServiceImpl extends BaseService implements TestService {

    /********* DAO 정의부분 시작 *********/
    @Autowired 
    private TestDao testDao;
    @Qualifier("TestDao2")
    private TestDao testDao2;
    /********* DAO 정의부분 끝 *********/
    
	@Override
	public void doTestService01(String name) {
		testDao.doTestDao01(name);
	}

	@Override
	public void testBaseService(String id) {
		testDao2.doTestDao01(id);
	}

	public void testMyService(String id) {
		d(id);
	}

}
