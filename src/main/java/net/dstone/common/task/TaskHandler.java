package net.dstone.common.task;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.glassfish.jersey.internal.jsr166.SubmittableFlowPublisher;

import net.dstone.common.core.BaseObject;
import net.dstone.common.utils.DateUtil;
import net.dstone.common.utils.StringUtil;

public class TaskHandler extends BaseObject{
	
	public class TaskReport{

		int monitoringSec = Integer.parseInt(DateUtil.getToDate("HHmmss"));
		
		int tryCount = 0;
		int successCount = 0;
		int errorCount = 0;
		BigDecimal rate = BigDecimal.ZERO;
		
		public int getTryCount() {
			return tryCount;
		}
		public void setTryCount(int tryCount) {
			this.tryCount = tryCount;
		}
		public void addTryCount() {
			this.tryCount++;
		}
		public void addTryCount(int tryCount) {
			this.tryCount = this.tryCount + tryCount;
		}
		
		public int getSuccessCount() {
			return successCount;
		}
		public void setSuccessCount(int successCount) {
			this.successCount = successCount;
		}
		public void addSuccessCount() {
			this.successCount++;
		}
		public void addSuccessCount(int successCount) {
			this.successCount = this.successCount + successCount;
		}
		
		public int getErrorCount() {
			return errorCount;
		}
		public void setErrorCount(int errorCount) {
			this.errorCount = errorCount;
		}
		public void addErrorCount() {
			this.errorCount++;
		}
		public void addErrorCount(int errorCount) {
			this.errorCount = this.errorCount + errorCount;
		}

		public int getMonitoringSec() {
			return monitoringSec;
		}
		public void setMonitoringSec(int monitoringSec) {
			this.monitoringSec = monitoringSec;
		}

		public BigDecimal getRate() {
			return rate;
		}
		public void setRate(BigDecimal rate) {
			this.rate = rate;
		}
		@Override
		public String toString() {
			return "TaskReport [monitoringSec=" + monitoringSec + ", tryCount=" + tryCount + ", successCount="
					+ successCount + ", errorCount=" + errorCount + ", rate=" + rate + "]";
		}
	}
	
	private Map<String, ExecutorService> EXECUTOR_SERVICE_MAP;
	private Map<String, TaskReport> EXECUTOR_SERVICE_REPORT_MAP;
	private int EXECUTOR_SERVICE_REPORT_INTERVAL = 10;

	private ThreadFactory threadFactory = new ThreadFactory() {
        public Thread newThread(Runnable r) {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        }
    };
	
	/**
	 * 싱글 쓰레드풀 생성.
	 * @param executorServiceId(쓰레드풀아이디)
	 * @throws Exception
	 */
	public TaskHandler addSingleExecutorService(String executorServiceId) throws Exception{
		getLogger().info("TaskHandler.addSingleExecutorService("+executorServiceId+") has been called !!!");
		if(!EXECUTOR_SERVICE_MAP.containsKey(executorServiceId)) {
			ExecutorService executorService = null;
	        executorService = Executors.newSingleThreadExecutor(threadFactory);
			EXECUTOR_SERVICE_MAP.put(executorServiceId, executorService);
			EXECUTOR_SERVICE_REPORT_MAP.put(executorServiceId, new TaskReport());
		}else {
			throw new Exception("["+executorServiceId+"]은 이미 존재하는 ExecutorService 입니다.");
		}
		return this;
	}
	
	/**
	 * Fixed 쓰레드풀 생성
	 * @param executorServiceId(쓰레드풀아이디)
	 * @param threadNumWhenFixed(쓰레드갯수)
	 * @throws Exception
	 */
	public TaskHandler addFixedExecutorService(String executorServiceId, int threadNumWhenFixed) throws Exception{
		getLogger().info("TaskHandler.addFixedExecutorService("+executorServiceId+", "+threadNumWhenFixed+") has been called !!!");
		if(!EXECUTOR_SERVICE_MAP.containsKey(executorServiceId)) {
			ExecutorService executorService = null;
			if(threadNumWhenFixed < 1) {
				threadNumWhenFixed = Runtime.getRuntime().availableProcessors() * (1) * (1);
			}
			executorService = Executors.newFixedThreadPool(threadNumWhenFixed, threadFactory);
			EXECUTOR_SERVICE_MAP.put(executorServiceId, executorService);
			EXECUTOR_SERVICE_REPORT_MAP.put(executorServiceId, new TaskReport());
		}else {
			throw new Exception("["+executorServiceId+"]은 이미 존재하는 ExecutorService 입니다.");
		}
		return this;
	}
	
	/**
	 * Cached 쓰레드풀 생성
	 * @param executorServiceId(쓰레드풀아이디)
	 * @throws Exception
	 */
	public TaskHandler addCachedExecutorService(String executorServiceId) throws Exception{
		getLogger().info("TaskHandler.addCachedExecutorService("+executorServiceId+") has been called !!!");
		if(!EXECUTOR_SERVICE_MAP.containsKey(executorServiceId)) {
			ExecutorService executorService = null;
	        executorService = Executors.newCachedThreadPool(threadFactory);
			EXECUTOR_SERVICE_MAP.put(executorServiceId, executorService);
			EXECUTOR_SERVICE_REPORT_MAP.put(executorServiceId, new TaskReport());
		}else {
			throw new Exception("["+executorServiceId+"]은 이미 존재하는 ExecutorService 입니다.");
		}
		return this;
	}

	/**
	 * Custom 쓰레드풀 생성
	 * @param executorServiceId(쓰레드풀아이디)
	 * @param corePoolSize(기본풀사이즈)
	 * @param maximumPoolSize(최대퓰사이즈)
	 * @param queueCapacity(큐용량-corePoolSize보다 쓰레드요청이 많아졌을 경우 이 수치만큼 큐잉한다. 이 수치가 넘어가게 되면 maximumPoolSize까지 쓰레드가 생성됨. corePoolSize+queueCapacity+maximumPoolSize 를 넘어가는 요청이 발생 시 RejectedExecutionException이 발생)
	 * @param keepAliveTime(쓰레드미사용시 제거대기시간-corePoolSize보다 쓰레드요청이 많아졌을 경우 queueCapacity까지 큐잉되다가 큐잉초과시 maximumPoolSize까지 쓰레드가 생성되는데 keepAliveTime시간만큼 유지했다가 다시 corePoolSize로 돌아가는동안 유지되는 시간을 의미)
	 * @throws Exception
	 */
	public TaskHandler addCustomExecutorService(String executorServiceId, int corePoolSize, int maximumPoolSize, int queueCapacity, long keepAliveTime) throws Exception{
		getLogger().info("TaskHandler.addCustomExecutorService("+executorServiceId+", "+corePoolSize+", "+maximumPoolSize+" ,"+queueCapacity+", "+keepAliveTime+") has been called !!!");
		if(!EXECUTOR_SERVICE_MAP.containsKey(executorServiceId)) {
			ThreadPoolExecutor executorService;
			BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(queueCapacity);
	        executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, java.util.concurrent.TimeUnit.SECONDS, workQueue, threadFactory);
			EXECUTOR_SERVICE_MAP.put(executorServiceId, executorService);
			EXECUTOR_SERVICE_REPORT_MAP.put(executorServiceId, new TaskReport());
		}else {
			throw new Exception("["+executorServiceId+"]은 이미 존재하는 ExecutorService 입니다.");
		}
		return this;
	}
	
	private ExecutorService getExecutorService(String executorServiceId){
		ExecutorService executorService = null;
		executorService = EXECUTOR_SERVICE_MAP.get(executorServiceId);
		return executorService;
	}
	
	public boolean isExecutorServiceExists(String executorServiceId) {
		boolean isExists = false;
		isExists = EXECUTOR_SERVICE_MAP.containsKey(executorServiceId);
		return isExists;
	}
	public TaskReport getExecutorServiceTaskReport(String executorServiceId){
		TaskReport taskReport = null;
		taskReport = EXECUTOR_SERVICE_REPORT_MAP.get(executorServiceId);
		return taskReport;
	}
	
	public void removeExecutorService(String executorServiceId){
		if(EXECUTOR_SERVICE_MAP.containsKey(executorServiceId)) {
			ExecutorService executorService = this.getExecutorService(executorServiceId);
			this.close(executorService);
			EXECUTOR_SERVICE_MAP.remove(executorServiceId);
			EXECUTOR_SERVICE_REPORT_MAP.remove(executorServiceId);
		}
	}
	
	/******************************************************************/
	protected static TaskHandler taskHandler = null;
	
	public static TaskHandler getInstance(){
		if(taskHandler == null){
			taskHandler = new TaskHandler();
		}
		return taskHandler;
	}
	
	protected TaskHandler(){		
		init();
	}

	protected void init(){
		EXECUTOR_SERVICE_MAP = new HashMap<String, ExecutorService>();
		EXECUTOR_SERVICE_REPORT_MAP = new HashMap<String, TaskReport>();
	}

	/**
	 * @param executorServiceId
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public TaskItem doTheSyncTask(String executorServiceId, TaskItem item) throws Exception{
		ExecutorService executorService = null;
		Future<TaskItem> future = null;
		try {
			net.dstone.common.utils.DateUtil.stopWatchStart("TaskHandler["+executorServiceId+"].doTheSyncTask");
			executorService = this.getExecutorService(executorServiceId);
			
			item.setExecutorServiceId(executorServiceId);

			future = executorService.submit((Callable<TaskItem>)item);
			
			if(future != null){
				item = future.get();
			}

		} catch (Exception e) {
			getLogger().info( this.getClass().getName() + ".doTheSyncTask() 작없중 예외발생. ID[" + item.getId() + "] 상세내용:" + e.toString());
			throw e;
		} finally {
			net.dstone.common.utils.DateUtil.stopWatchEnd("TaskHandler["+executorServiceId+"].doTheSyncTask");
			//this.close(executorService);
		}
		return item;
	}
	
	/**
	 * @param executorServiceId
	 * @param item
	 * @throws Exception
	 */
	public void doTheAsyncTask(String executorServiceId, TaskItem item) throws Exception{
		ExecutorService executorService = null;
		try {
			net.dstone.common.utils.DateUtil.stopWatchStart("TaskHandler["+executorServiceId+"].doTheAyncTask");
			executorService = this.getExecutorService(executorServiceId);
			
			item.setExecutorServiceId(executorServiceId);

			executorService.execute((Runnable)item);

		} catch (Exception e) {
			getLogger().info( this.getClass().getName() + ".doTheAyncTask() 작없중 예외발생. ID[" + item.getId() + "] 상세내용:" + e.toString());
			throw e;
		} finally {
			net.dstone.common.utils.DateUtil.stopWatchEnd("TaskHandler["+executorServiceId+"].doTheAyncTask");
			//this.close(executorService);
		}
	}

	/**
	 * @param executorServiceId
	 * @param itemList
	 * @return
	 * @throws Exception
	 */
	public ArrayList<TaskItem> doTheSyncTasks(String executorServiceId, ArrayList<TaskItem> itemList) throws Exception{
		ExecutorService executorService = null;
		ArrayList<TaskItem> returnVal = new ArrayList<TaskItem>();
		List<Future<TaskItem>> futureList = new ArrayList<Future<TaskItem>>();
		
		String msg = "TaskHandler["+executorServiceId+"].doTheSyncTasks(TaskItem갯수:"+itemList.size()+")";
		
		try {
			net.dstone.common.utils.DateUtil.stopWatchStart(msg);
			executorService = getExecutorService(executorServiceId);
			for(TaskItem item : itemList) {
				item.setExecutorServiceId(executorServiceId);
			}
			
			futureList = executorService.invokeAll(itemList);
			
			if(futureList != null){
				Future<TaskItem> future = null;
				for(int i=0; i<futureList.size(); i++ ){
					future = futureList.get(i);
					TaskItem item = future.get();
				    try {
				    	returnVal.add(item);
				    } catch (Exception e) {
				    	returnVal.add(null);
				    } finally {

				    }
				}
			}
			//getLogger().info("Active 쓰레드갯수[doTheTasks시작시점:"+startThreadCount+"개 ==>> doTheTasks종료시점:"+Thread.activeCount()+"개]");
		} catch (Exception e) {
			getLogger().info( this.getClass().getName() + ".doTheSyncTasks() 작없중 예외발생. 상세내용:" + e.toString());
			throw e;
		} finally {
			net.dstone.common.utils.DateUtil.stopWatchEnd(msg);
			//this.close(executorService);
		}
		return returnVal;
	}
	
	/**
	 * @param executorServiceId
	 * @param itemList
	 * @throws Exception
	 */
	public void doTheAsyncTasks(String executorServiceId, ArrayList<TaskItem> itemList) throws Exception{
		ExecutorService executorService = null;
		
		String msg = "TaskHandler["+executorServiceId+"].doTheAsyncTasks(TaskItem갯수:"+itemList.size()+")";
		
		try {
			net.dstone.common.utils.DateUtil.stopWatchStart(msg);
			executorService = getExecutorService(executorServiceId);
			for(TaskItem item : itemList) {
				item.setExecutorServiceId(executorServiceId);
				executorService.execute(item);
			}
			//getLogger().info("Active 쓰레드갯수[doTheTasks시작시점:"+startThreadCount+"개 ==>> doTheTasks종료시점:"+Thread.activeCount()+"개]");
		} catch (Exception e) {
			getLogger().info( this.getClass().getName() + ".doTheAsyncTasks() 작없중 예외발생. 상세내용:" + e.toString());
			throw e;
		} finally {
			net.dstone.common.utils.DateUtil.stopWatchEnd(msg);
			//this.close(executorService);
		}
	}
	
	private void close(ExecutorService executorService){
		try {
			if( executorService != null ){
				executorService.shutdown();
	            int waitTimeAfterShutdown = 30;
		        if (executorService.awaitTermination(waitTimeAfterShutdown, TimeUnit.SECONDS)) {
		        	getLogger().debug(LocalTime.now() + " 모든 Task가 종료.");
		        } else {
		        	getLogger().debug(LocalTime.now() + " "+waitTimeAfterShutdown+"초 대기하였으나 일부 Task가 종료되지 않아서 강제종료 처리.");

		        }
			}
		} catch (Exception e) {
			getLogger().debug( this.getClass().getName() + ".close() 작없중 예외발생. 상세내용:" + e.toString());
		}

	}
	
	/**
	 * 작업진행모니터링
	 */
	public TaskReport doMonitoring(String executorServiceId) {
		TaskReport taskReport = this.getExecutorServiceTaskReport(executorServiceId);
		if( taskReport != null ) {
			boolean isTimeToReport = false;
			
			if( taskReport.getTryCount() > 0) {
				if( taskReport.getTryCount() == (taskReport.getSuccessCount() + taskReport.getErrorCount()) ) {
					isTimeToReport = true;
				}else {
					String beforeSec = StringUtil.filler(String.valueOf(taskReport.getMonitoringSec()), 6, "0");
					int hour = Integer.parseInt(beforeSec.substring(0, 2));
					int min = Integer.parseInt(beforeSec.substring(2, 4));
					int sec = Integer.parseInt(beforeSec.substring(4));
					
					java.util.Calendar beforeCal = java.util.Calendar.getInstance();
					beforeCal.set(java.util.Calendar.HOUR_OF_DAY, hour);
					beforeCal.set(java.util.Calendar.MINUTE, min);
					beforeCal.set(java.util.Calendar.SECOND, sec);
					
					beforeCal.add(java.util.Calendar.SECOND, EXECUTOR_SERVICE_REPORT_INTERVAL);
					
					java.util.Calendar currentCal = java.util.Calendar.getInstance();				
					if( beforeCal.compareTo(currentCal) <= 0 ) {
						isTimeToReport = true;
						taskReport.setMonitoringSec(Integer.parseInt(DateUtil.getToDate("HHmmss")));
					}
				}
			}
			
			if(isTimeToReport) {
				StringBuffer buff = new StringBuffer();
				BigDecimal rate = new BigDecimal(taskReport.getSuccessCount());
				rate = rate.divide(new BigDecimal(taskReport.getTryCount()), 2, BigDecimal.ROUND_HALF_UP);
				rate = rate.multiply(new BigDecimal(100));
				taskReport.setRate(rate);
				
				buff.append("\n\n");
				buff.append("||@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 쓰레드풀아이디ID["+executorServiceId+"] 작업진행현황  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@||").append("\n");
				buff.append("총진행대상건수["+taskReport.getTryCount()+"] 진행완료건수["+taskReport.getSuccessCount()+"] 작업진행률["+ rate.intValue() +"%]").append("\n");
				buff.append("||@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 쓰레드풀아이디ID["+executorServiceId+"] 작업진행현황  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@||").append("\n");
				debug(buff);
			}
		}
		return taskReport;
	}
	
	public TaskReport getTaskReport(String executorServiceId){
		return EXECUTOR_SERVICE_REPORT_MAP.get(executorServiceId);
	}
	
	public Map<String, TaskReport> getTaskReportAll(){
		return EXECUTOR_SERVICE_REPORT_MAP;
	}
	
}
