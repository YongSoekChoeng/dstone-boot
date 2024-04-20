package net.dstone.sample.analyze;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("TestService") 
public class TestServiceImpl extends BaseService implements TestService {

    /********* DAO 정의부분 시작 *********/
    @Autowired 
    private TestDao testDao;
    /********* DAO 정의부분 끝 *********/
    
	@Override
	public void doTestService01(String name) {
    	testDao.doTestDao01(name);
	}

	@Override
	public void testBaseService(String id) {
		// TODO Auto-generated method stub
		
	}

	public void testMyService(String id) {
		d(id);
	}

}
