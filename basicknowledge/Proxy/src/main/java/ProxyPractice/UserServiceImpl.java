package ProxyPractice;

import javax.rmi.ssl.SslRMIClientSocketFactory;

public class UserServiceImpl implements UserService {
    public void select() {
        System.out.println("查询SelectById");
    }
    public void update() {
        System.out.println("查询UpdateId");
    }
}
