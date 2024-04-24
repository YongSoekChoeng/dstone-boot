package net.dstone.sample.analyze;

import org.springframework.stereotype.Repository;

@Repository("Test1Dao") 
public class Test1DaoImpl extends BaseDao implements Test1Dao{

	@Override
	public void doTest1Dao01(String name) {
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
