package net.dstone.common.queue;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import net.dstone.common.task.TaskItem;

public class QueueHandler {
	
	/****************************  설정 시작 ****************************/
	/**
	 * 큐를 감시할 시간간격.
	 */
	public static int QUEUE_CHECK_INTERVAL = 500; 
	/**
	 * 큐에 아이템이 있을 경우 Fetch해올 큐아이템 갯수. -1 이면 큐의 모든 아이템을 Fetch해온다.
	 */
	public static int FETCH_SIZE_BY_ONE = -1; 
	/**
	 * Fetch해온  큐아이템을 처리 할 쓰레드 갯수.
	 */
	public static int THREAD_NUM_PER_ONE_FETCH = 100;
	/****************************  설정 끝   ****************************/
	
	protected static QueueHandler queueHandler = null;
	
	protected Queue queue = null;
	protected QueueThread queueThread = null;
	private static int workingQueueCount = 0;
	
	public static QueueHandler getInstance(){
		if(queueHandler == null){
			queueHandler = new QueueHandler();
		}
		return queueHandler;
	}
	
	protected QueueHandler(){		
		init();
	}

	private void debug(Object o){
		net.dstone.common.utils.LogUtil.sysout(o);
	}
	protected void init(){
		// 큐 초기화
		queue = new Queue();
		// 쓰레드 시작
		queueThread = new QueueThread();
		queueThread.setDaemon(true);
		queueThread.start();
	}
	
	public void addQueue(QueueItem queueItem) {
		try {
			
			debug("||======================================= Adding QueueItem Start... =======================================||");
			queue.offer(queueItem);
			debug("||======================================= Adding QueueItem End... =======================================||");
			
		} catch (Exception e) {
			debug(e);
		}
	}
	
	public int getQueueSize(){
		int intSize = 0;
		if(this.queue != null){
			intSize = this.queue.size();
		}
		intSize = intSize + workingQueueCount;
		return intSize;
	}
	
	public void dumpQueue(){
		net.dstone.common.utils.LogUtil.sysout(queue);
	}
	
	@SuppressWarnings({ "serial" })
	class Queue extends LinkedBlockingQueue<QueueItem>{
		
		@Override
		public boolean isEmpty(){
			boolean isEmpty = true;
			if(this.size() > 0){
				isEmpty = false;
			}
			return isEmpty;
		}

		@Override
		public String toString(){
			StringBuffer buff = new StringBuffer();
			Object[] oblArray = super.toArray();
			if(oblArray != null){
				for(int i=0; i<oblArray.length; i++){
					buff.append(oblArray[i]).append("\n");
				}
			}
			return buff.toString();
		}

	}
	
	class QueueThread extends Thread{
		boolean isWorking = false;
		public void run() {
			while (true) {
				try {
					if (queue.isEmpty()){
						Thread.sleep(QUEUE_CHECK_INTERVAL);
					}else{
						if( !this.isWorking ){
							doTheJob(fetchFromQueue());
						}
					}
				} catch(InterruptedException e) {
					debug(e);
				}
			}
		}
		
		private ArrayList<QueueItem> fetchFromQueue(){
			ArrayList<QueueItem> queueToBeWorked = null;
			try {
				if(FETCH_SIZE_BY_ONE == -1 || FETCH_SIZE_BY_ONE >= queue.size() ){
					queueToBeWorked = new ArrayList<QueueItem>(queue.size());
					queue.drainTo(queueToBeWorked);
				}else{
					queueToBeWorked = new ArrayList<QueueItem>(FETCH_SIZE_BY_ONE);
					queue.drainTo(queueToBeWorked, FETCH_SIZE_BY_ONE);
				}
				
				workingQueueCount = queueToBeWorked.size();
			} catch (Exception e) {
				debug(e);
			}
			return queueToBeWorked;
		}
		
		private void doTheJob(ArrayList<QueueItem> queueToBeWorked) {
			isWorking = true;
			try {
				if (queueToBeWorked != null) {
					
					net.dstone.common.task.TaskHandler.TaskConfig conf = net.dstone.common.task.TaskHandler.getInstance().getTaskConfig();
					conf.setTaskMode(net.dstone.common.task.TaskHandler.FIXED);
					conf.setThreadNumWhenFixed(THREAD_NUM_PER_ONE_FETCH);
					conf.setWaitTimeAfterShutdown(1);
					
					java.util.ArrayList<net.dstone.common.task.TaskItem> workList = new java.util.ArrayList<net.dstone.common.task.TaskItem>();
					for(int i=0; i<queueToBeWorked.size(); i++) {
						QueueItem queueItem = queueToBeWorked.get(i);
						workList.add(new net.dstone.common.task.TaskItem(){
							@Override
							public TaskItem doTheTask() {
								debug("<<<<<<<<<<<<<<<<<<<< ["+queueItem.getId()+"] doTheJob 시작 >>>>>>>>>>>>>>>>>>>>>>>");
								try {
									queueItem.doTheJob();
								} catch (Exception e) {
									throw e;
								} finally {
									workingQueueCount--;
								}
								debug("<<<<<<<<<<<<<<<<<<<< ["+queueItem.getId()+"] doTheJob 종료 >>>>>>>>>>>>>>>>>>>>>>>");
								
								return this;
							}
						});
					}
					workList = net.dstone.common.task.TaskHandler.getInstance().doTheTasks(conf, workList);

				}
			} catch (Exception e) {
				debug(e);
			} finally {
				isWorking = false;
			}
		}
		
		
	}

}
