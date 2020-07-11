package ProxyPractice;

import java.util.Date;

public class UserServiceProxy implements UserService {
    private UserService target; //被代理对象

    public UserServiceProxy(UserService target) {
        this.target = target;
    }
    public void select() {
        before();
        target.select();
        after();
    }
    public void update() {
        before();
        target.update();
        after();
    }
    private void before() {     // 在执行方法之前执行
        System.out.println(String.format("log start time [%s] ", new Date()));
    }
    private void after() {      // 在执行方法之后执行
        System.out.println(String.format("log end time [%s] ", new Date()));
    }
}
