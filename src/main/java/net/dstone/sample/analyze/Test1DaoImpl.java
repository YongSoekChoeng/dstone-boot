package net.dstone.sample.analyze;

import org.springframework.stereotype.Repository;

@Repository("TestDao21") 
public class Test1DaoImpl extends BaseDao implements TestDao{

	@Override
	public void doTestDao01(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void testBaseDao(String id) {
		d(id);
	}

	public void testMyDao(String id) {
		d(id);
	}

}
