package net.dstone.sample.analyze;

import org.springframework.beans.factory.annotation.Qualifier;

public abstract class BaseService {

    protected void d(Object msg) {
    	System.out.println(msg);
    }

    @Qualifier("baseTestDao")
    protected TestDao baseTestDao;
    
    public abstract void testBaseService(String id); 
    

    protected void parentHellow(String name) {
    	System.out.println(name);
    }
}
