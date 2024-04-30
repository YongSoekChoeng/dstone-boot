package net.dstone.sample.analyze;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("TestService") 
public class TestServiceImpl extends BaseService implements TestService {

    /********* DAO 정의부분 시작 *********/
    @Autowired
    private TestDao1 test1Dao;
    
    @Qualifier("TestDao1")
    private TestDao1 TestDao1;
    
    @Qualifier(value ="TestDao2")
    private TestDao2 testDao2; 
    
    @Resource(name = "TestDao2")
    private TestDao2 test2Dao;

    @Autowired
    private TestBiz2 testBiz2;
    /********* DAO 정의부분 끝 *********/
    
	@Override
	public void doTestService01(String name) {
		test1Dao.doTestDao01(name);
		
		TestDao1.doTestDao01(name);
		((Test1DaoImpl)TestDao1).testMyDao1(name);
		testDao2.doTestDao02(name);
		((Test2DaoImpl)testDao2).testMyDao2(name);
		test2Dao.doTestDao02(name);

		testBiz2.testBiz1ForAutowired(name); 
		
		testBaseService(name);
		parentHellow(name);
	}

	public void doTestService02(TestBiz1 testBiz1) {
		testBiz1.testHello("하하");
	}

	@Override
	public void testBaseService(String id) {
		//testDao1.doTestDao01(id); 
		getTestDao1().doTestDao01(id);
		getTestDao2().doTestDao02(id);
	}


}
