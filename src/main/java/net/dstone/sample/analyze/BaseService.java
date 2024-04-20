package net.dstone.sample.analyze;

public abstract class BaseService {

    protected void d(Object msg) {
    	System.out.println(msg);
    }

    public abstract void testBaseService(String id);
}
