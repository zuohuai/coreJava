package com.edu.design.singleton;
public class SingletonOther {
    
	/**
	 * 内部类静态属性（或静态块）会在内部类第一次被调用的时候按顺序被初始化（或执行）；
	 * 而类内部静态块的执行先于类内部静态属性的初始化，
	 * 会发生在类被第一次加载初始化的时候；类内部属性的初始化先于构造函数会发生在一个类的对象被实例化的时候。
	 * @author zuohuai
	 *
	 */
    public static class Inner{
        
        public final static SingletonOther testInstance = new SingletonOther(3);
        
        static {
            System.out.println("TestInner Static!");
        }
    }
    
    public static SingletonOther getInstance(){
        return Inner.testInstance;
    }
    
    public SingletonOther(int i ) {
        System.out.println("Test " + i +" Construct! ");
    }
    
    static {
        System.out.println("Test Stataic");
    }
    
    {
    	System.out.println("class Init");
    }
    public static SingletonOther testOut = new SingletonOther(1);
    
    public static void main(String args[]){
        SingletonOther t = new SingletonOther(2);
        SingletonOther.getInstance();
        SingletonOther.getInstance();
        SingletonOther.getInstance();
    }

}