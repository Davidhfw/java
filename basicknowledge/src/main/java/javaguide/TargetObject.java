package main.java.javaguide;

public class TargetObject {
    private String value;

    public TargetObject() {
        value = "JavaGuide";
    }
    public void publicMethod(String s) {
        System.out.println("I Love " + s);
    }
    private void privateMethod() {
        System.out.println("value is " + value);
    }
}
