package net.dstone.common.queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.dstone.common.core.BaseObject;

public class QueueHandler extends BaseObject{
	
	/****************************  설정 시작 ****************************/
	/**
	 * 큐를 감시할 시간간격.(60초)
	 */
	protected int QUEUE_CHECK_INTERVAL = 60*1000; 
	/**
	 * 한번에 처리할 큐아이템 갯수. -1 이면 큐의 모든 아이템을 처리한다.
	 */
	protected int QUEUE_ITEM_SIZE_BY_ONE = 100;    
	/****************************  설정 끝   ****************************/
	
	protected static Map<String, QueueHandler> QUEUE_HANDLER_MAP = new HashMap<String, QueueHandler>();
	
	protected Queue queue = null;
	protected QueueThread queueThread = null;
	
	public static QueueHandler getInstance(String queueHandlerId){
		QueueHandler queueHandler = null;
		if( !QUEUE_HANDLER_MAP.containsKey(queueHandlerId) ) {
			queueHandler = new QueueHandler();
			QUEUE_HANDLER_MAP.put(queueHandlerId, queueHandler);
		}else {
			queueHandler = QUEUE_HANDLER_MAP.get(queueHandlerId);
		}
		return queueHandler;
	}

	public static QueueHandler getInstance(String queueHandlerId, int queueCheckInterval, int queueItemSizeByOne){
		QueueHandler queueHandler = null;
		if( !QUEUE_HANDLER_MAP.containsKey(queueHandlerId) ) {
			queueHandler = new QueueHandler();
			queueHandler.QUEUE_CHECK_INTERVAL = queueCheckInterval;
			queueHandler.QUEUE_ITEM_SIZE_BY_ONE = queueItemSizeByOne;
			QUEUE_HANDLER_MAP.put(queueHandlerId, queueHandler);
		}else {
			queueHandler = QUEUE_HANDLER_MAP.get(queueHandlerId);
			queueHandler.QUEUE_CHECK_INTERVAL = queueCheckInterval;
			queueHandler.QUEUE_ITEM_SIZE_BY_ONE = queueItemSizeByOne;
		}
		return queueHandler;
	}
	
	protected QueueHandler(){		
		init();
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
			synchronized(queue) {
				debug("||======================================= Adding QueueItem Start... =======================================||");
				debug(queueItem);
				debug("||======================================= Adding QueueItem End... =======================================||");
				queue.add(queueItem);
			}
		} catch (Exception e) {
			debug(e);
		}
	}
	
	public void removeQueueById(String queueItemId) {
		try {
			synchronized(queue) {
				debug("||======================================= Removing QueueItem Start... =======================================||");
				for(int i=0; i<queue.size(); i++) {
					QueueItem queueItem = queue.get(i);
					if(queueItemId.equals(queueItem.getId())) {
						queue.remove(i);
						break;
					}
				}
				debug("||======================================= Removing QueueItem End... =======================================||");
			}
		} catch (Exception e) {
			debug(e);
		}
	}
	
	public int getQueueSize(){
		int intSize = 0;
		if(this.queue != null){
			intSize = this.queue.size();
		}
		return intSize;
	}
	
	public void dumpQueue(){
		net.dstone.common.utils.LogUtil.sysout(queue);
	}
	
	@SuppressWarnings({ "serial" })
	class Queue extends ArrayList<QueueItem>{
		
		public boolean isEmpty(){
			boolean isEmpty = true;
			if(this.size() > 0){
				isEmpty = false;
			}
			return isEmpty;
		}
		
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
							Queue queueToBeWorked = fetchFromQueue();
							doTheJob(queueToBeWorked);
						}
					}
				} catch(InterruptedException e) {
					debug(e);
				}
			}
		}
		
		private Queue fetchFromQueue(){
			Queue queueToBeWorked = new Queue();
			try {
				synchronized(queue) {
					if(QUEUE_ITEM_SIZE_BY_ONE == -1){
						queueToBeWorked = (Queue)queue.clone();
						queue.clear();
					}else{
						int index = 1;
						for(int i=0; i<queue.size(); i++){
							if( index > QUEUE_ITEM_SIZE_BY_ONE ){
								break;	
							}
							queueToBeWorked.add(queue.get(i));
							queue.remove(i);
							i--;
							index++;
						}
					}
				}
			} catch (Exception e) {
				debug(e);
			}
			return queueToBeWorked;
		}
		
		private void doTheJob(Queue queueToBeWorked) {
			isWorking = true;
			try {
				QueueItem queueItem = null;
				if (queueToBeWorked != null) {
					for (int i = 0; i < queueToBeWorked.size(); i++) {
						try {
							queueItem = (QueueItem) queueToBeWorked.get(i);
							debug("<<<<<<<<<<<<<<<<<<<< ["+queueItem.getId()+"] doTheJob 시작 >>>>>>>>>>>>>>>>>>>>>>>");
							queueItem.doTheJob();
							debug("<<<<<<<<<<<<<<<<<<<< ["+queueItem.getId()+"] doTheJob 종료 >>>>>>>>>>>>>>>>>>>>>>>");
						} catch (Exception e) {
							debug(e);
						}
					}
				}
			} catch (Exception e) {
				debug(e);
			} finally {
				isWorking = false;
				if (queueToBeWorked != null) {
					queueToBeWorked.clear();
				}
			}
		}
		
		
	}

}
