package main.java.Ref;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

public class RefPractice {
    public static void main(String[] args) {
        testSoftReference();
    }
    private static List<Object> list = new ArrayList<Object>();
    private static void testSoftReference() {
        for(int i =0; i < 10; i++) {
            byte[] bytes = new byte[1024*1024];
            SoftReference<byte[]> sr = new SoftReference<byte[]>(bytes);
            list.add(sr);

        }
        System.gc();
        for(int i=0; i< list.size(); i++) {
            Object object = ((SoftReference) list.get(i)).get();
            System.out.println(object);
        }
    }
}
