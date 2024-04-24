package net.dstone.sample.analyze;

import org.springframework.stereotype.Repository;

@Repository("Test2Dao") 
public class Test2DaoImpl extends BaseDao implements Test2Dao{

	@Override
	public void doTest2Dao01(String name) {
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
