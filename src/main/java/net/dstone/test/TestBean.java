package net.dstone.test;

import net.dstone.common.utils.StringUtil;

public class TestBean {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestBean.test();
	}

	
	public static void test() {

		
		String DBID = "DBID_3";
		net.dstone.common.utils.DbUtil db = null;
		net.dstone.common.utils.DataSet ds = new net.dstone.common.utils.DataSet();
		
		StringBuffer sql = new StringBuffer();
		int genNum = 1000000;
		
		net.dstone.common.utils.DateUtil.stopWatchStart("연습장");
		try {
			sql.append("INSERT INTO SAMPLE_TEST (TEST_ID, TEST_NAME, FLAG_YN, INPUT_DT) VALUES (?, ?, 'N', NOW())");
			
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection(false);
			db.setQuery(sql.toString());
			
			for(int i=0; i<genNum; i++){
				db.pstmt.setString(1, StringUtil.filler(String.valueOf((i+1)), 10, "0"));
				db.pstmt.setString(2, String.valueOf((i+1))+"-이름");
				db.pstmt.addBatch();
				//db.pstmt.clearParameters();
				if( i%5000 == 0){
					db.pstmt.executeBatch();
				}
			}
		
			db.pstmt.executeBatch();
			
			db.commit();
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(db.getQuery());

			db.rollBack();
		}finally{
			if(db != null){
				db.release();
			}
			net.dstone.common.utils.DateUtil.stopWatchEnd("연습장");
		}


	}
}
