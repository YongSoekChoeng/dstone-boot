package net.dstone.common.task;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.Callable;

import net.dstone.common.core.BaseObject;
import net.dstone.common.utils.StringUtil;

public abstract class TaskItem extends BaseObject implements Callable<TaskItem>{

	public abstract TaskItem doTheTask();
	
	@Override
	public TaskItem call() throws Exception {
		return (TaskItem)this.doTheTask();
	}
	
	private String strId = "";
	private HashMap<String, Object> prop = new HashMap<String, Object>();

	/**
	 * @return
	 * 2007. 12. 23.
	 * Comment :
	 */
	public String getId() {
		return strId;
	}

	/**
	 * @param string
	 * 2007. 12. 23.
	 * Comment :
	 */
	public void setId(String string) {
		strId = string;
	}
	
	/**
	 * @param strKey
	 * @param obj
	 * 2007. 12. 23.
	 * Comment :
	 */
	public void setObj(String strKey, Object obj){
		this.prop.put(strKey, obj);
	}
	
	/**
	 * @param strKey
	 * @return
	 * 2007. 12. 23.
	 * Comment :
	 */
	public Object getObj(String strKey){
		return this.prop.get(strKey);
	}
	
	/**
	 * @param strKey
	 * @param strVal
	 * 2007. 12. 23.
	 * Comment :
	 */
	public void setProperty(String strKey, String strVal){
		this.prop.put(strKey, strVal);
	}
	
	/**
	 * @param strKey
	 * @return
	 * 2007. 12. 23.
	 * Comment :
	 */
	public String getProperty(String strKey){
		return this.prop.get(strKey).toString();
	}
	
	/**
	 * @param strKey
	 * @param strDefaultVal
	 * @return
	 * 2007. 12. 23.
	 * Comment :
	 */
	public String getProperty(String strKey, String strDefaultVal){
		return StringUtil.nullCheck(this.prop.get(strKey), strDefaultVal);
	}
	
	/**
	 * @return
	 * 2007. 12. 23.
	 * Comment :
	 */
	public HashMap getProp(){
		return this.prop;
	}
	
	private int totalCount = -1;
	private int doneCount = -1;
	private int interval = -1;

	protected static int DEFAULT_INTERVAL = 10;
	
	/**
	 * 작업진행과정을 모니터링하기위한 초기작업
	 * @param totalCount-총작업진행대상
	 */
	public void initMonitoringCount(int totalCount) {
		this.totalCount = totalCount;
		this.interval = DEFAULT_INTERVAL;
		if(this.totalCount > -1) {
			doneCount = 0;
		}
	}

	/**
	 * 작업진행과정을 모니터링하기위한 초기작업
	 * @param totalCount-총작업진행대상
	 * @param interval-보고간격건수
	 */
	public void initMonitoringCount(int totalCount, int interval) {
		this.totalCount = totalCount;
		this.interval = interval;
		if(this.totalCount > -1) {
			doneCount = 0;
		}
	}
	
	/**
	 * 작업진행건수증가
	 */
	public void addMonitoringDoneCount() {
		doneCount++;
		if( totalCount > 0 && doneCount > 0 && doneCount%interval == 0) {
			StringBuffer buff = new StringBuffer();
			BigDecimal rate = new BigDecimal(doneCount);
			rate = rate.divide(new BigDecimal(totalCount), 2, BigDecimal.ROUND_HALF_UP);
			rate = rate.multiply(new BigDecimal(100));
			buff.append("\n\n");
			buff.append("||@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 쓰레드ID["+this.getId()+"] 작업진행현황  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@||").append("\n");
			buff.append("총진행대상건수["+totalCount+"] 진행완료건수["+doneCount+"] 작업진행률["+ rate.intValue() +"%]").append("\n");
			buff.append("||@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 쓰레드ID["+this.getId()+"] 작업진행현황  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@||").append("\n");
			debug(buff);
		}
	}

	/**
	 * 작업진행과정 종료
	 */
	public void endMonitoringCount() {
		if( totalCount > 0 && doneCount > 0 ) {
			StringBuffer buff = new StringBuffer();
			BigDecimal rate = new BigDecimal(doneCount);
			rate = rate.divide(new BigDecimal(totalCount), 2, BigDecimal.ROUND_HALF_UP);
			rate = rate.multiply(new BigDecimal(100));
			buff.append("\n\n");
			buff.append("||@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 쓰레드ID["+this.getId()+"] 최종진행현황  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@||").append("\n");
			buff.append("총진행대상건수["+totalCount+"] 진행완료건수["+doneCount+"] 작업진행률["+ rate.intValue() +"%]").append("\n");
			buff.append("||@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 쓰레드ID["+this.getId()+"] 최종진행현황  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@||").append("\n");
			debug(buff);
		}
	}
	
	@Override
	public String toString() {
		return "TaskItem [strId=" + strId + ", prop=" + prop + "]";
	}
	
}
