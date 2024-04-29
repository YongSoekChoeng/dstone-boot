package net.dstone.sample.analyze;

import org.springframework.stereotype.Component;

@Component("TestBiz1") 
public class TestBiz1 {
	
	public void testHello(String name) {
		System.out.println("Hello " + name + " ~!!");
	}
}
