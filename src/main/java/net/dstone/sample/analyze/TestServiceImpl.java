package net.dstone.sample.analyze;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("TestService") 
public class TestServiceImpl extends BaseService implements TestService {

    /********* DAO 정의부분 시작 *********/
    @Autowired
    private TestDao testDao;
    @Qualifier("TestDao21")
    private TestDao testDao21;
    @Qualifier(value ="TestDao22")
    private TestDao testDao22;
    @Resource(name = "TestDao3")
    private TestDao testDao3;
    /********* DAO 정의부분 끝 *********/
    
	@Override
	public void doTestService01(String name) {
		testDao.doTestDao01(name);
	}

	@Override
	public void testBaseService(String id) {
		testDao21.doTestDao01(id);
	}

	public void testMyService(String id) {
		d(id);
	}

}
