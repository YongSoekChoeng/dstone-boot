package net.dstone.test;


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
		String TABLE_NAME = "TB_RP_REL_PRSN_M";
		
		net.dstone.common.utils.DateUtil.stopWatchStart("연습장");
		try {
			sql.append("SELECT ").append("\n"); 
			sql.append("	* ").append("\n");
			sql.append("FROM  ").append("\n");
			sql.append("	" + TABLE_NAME + "  ").append("\n");
			sql.append("WHERE 1=1 ").append("\n");
			sql.append("AND ROWNUM < 10 ").append("\n");
			
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			db.setQuery(sql.toString());
			ds.buildFromResultSet(db.select(), "");	
			ds.checkData();
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(db.getQuery());
		}finally{
			if(db != null){
				db.release();
			}
			net.dstone.common.utils.DateUtil.stopWatchEnd("연습장");
		}

	}
}
