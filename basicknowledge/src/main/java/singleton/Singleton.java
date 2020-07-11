package main.java.singleton;

//public class Singleton {
//    private static Singleton instance = new Singleton();
//    public static Singleton getInstance() {
//        return instance;
//    }
//}
//不能保证只有一个实例被创建出来，因为java有默认的构造函数

//public class Singleton {
//    private static Singleton instance;
//    private Singleton() {};
//    public static Singleton getInstance() {
//        if (instance == null) {
//            instance = new Singleton();
//        }
//        return instance;
//    }
//}
//单线程不存在问题，多线程会存在同步问题
public class Singleton {
    private static volatile Singleton singleton = null;
    private Singleton() {}

    public static Singleton getSingleton() {
        //避免重复进入同步块
        if (singleton == null) {
            //同步.class，意味着对同步方法调用
            synchronized (Singleton.class) {
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}

