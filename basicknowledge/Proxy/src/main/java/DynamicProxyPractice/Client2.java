package DynamicProxyPractice;

import ProxyPractice.UserService;
import ProxyPractice.UserServiceImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Client2 {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        UserServiceImpl userService = new UserServiceImpl();
        ClassLoader classLoader = userService.getClass().getClassLoader();
        Class[] interfaces = userService.getClass().getInterfaces();
        InvocationHandler logHandler = new LogHandler(userService);
        UserService proxy = (UserService) Proxy.newProxyInstance(classLoader, interfaces, logHandler);
        proxy.select();
        proxy.update();
    }
}
