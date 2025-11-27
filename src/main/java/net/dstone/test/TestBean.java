package net.dstone.test;

import net.dstone.common.utils.StringUtil;

public class TestBean {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestBean.DB테스트();
	}

	
	public static void 암복호화() {

		/*****************************************************/

		/*****************************************************/

		net.dstone.common.utils.DateUtil.stopWatchStart("01.암복호화");

		try {

			String plainStr = "";
			String encStr = "VElaOj1pF3IdRCrKXHIQFVRCQv4hE6Oniig5ohsQ1wY=";
			String decStr = "";

			if (!net.dstone.common.utils.StringUtil.isEmpty(plainStr)) {
				encStr = net.dstone.common.utils.EncUtil.encrypt(plainStr);
				System.out.println("plainStr[" + plainStr + "] ==>> encStr[" + encStr + "]");
			}

			if (!net.dstone.common.utils.StringUtil.isEmpty(encStr)) {
				decStr = net.dstone.common.utils.EncUtil.decrypt(encStr);
				System.out.println("encStr[" + encStr + "] ==>> decStr[" + decStr + "]");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			net.dstone.common.utils.DateUtil.stopWatchEnd("01.암복호화");
		}

	}

	public static void DB테스트() {
		String DBID = "DBID_0";
		net.dstone.common.utils.DbUtil db = null;
		net.dstone.common.utils.DataSet ds = new net.dstone.common.utils.DataSet();
		StringBuffer sql = new StringBuffer();
		int genNum = 100;
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
