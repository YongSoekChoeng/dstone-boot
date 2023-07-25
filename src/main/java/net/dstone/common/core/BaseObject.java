package net.dstone.common.core;

import net.dstone.common.utils.LogUtil;

public class BaseObject {
	
	private LogUtil myLogger = null;

	protected LogUtil getLogger() {
		if(myLogger == null) {
			myLogger = new LogUtil(this);
		}
		return myLogger;
	}

	protected LogUtil getLogger(Object o) {
		if(myLogger == null) {
			myLogger = new LogUtil(o.getClass());
		}
		return myLogger;
	}
	
}
