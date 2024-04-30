package net.dstone.sample.analyze;

import org.springframework.beans.factory.annotation.Qualifier;

public abstract class BaseService {

    protected void d(Object msg) {
    	System.out.println(msg);
    }

    @Qualifier("TestDao1")
    protected TestDao1 testDao1;

    @Qualifier("TestDao2")
    protected TestDao2 testDao2;
    
    public abstract void testBaseService(String id); 
    
    protected void parentHellow(String name) {
    	System.out.println(name);
    }
    
    protected TestDao1 getTestDao1() {
    	return testDao1;
    }

    protected TestDao2 getTestDao2() {
    	return testDao2;
    }
    
}
