package net.dstone.sample.analyze;

import org.springframework.stereotype.Repository;

@Repository("TestDao22") 
public class Test2DaoImpl extends BaseDao implements TestDao{

	@Override
	public void doTestDao01(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void testBaseDao(String id) {
		d(id);
	}

	public void testMyDao2(String id) {
		d(id);
	}

}
