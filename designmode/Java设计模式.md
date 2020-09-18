# Java设计模式

[toc]

## 1 单例模式

概括起来，要实现一个单例，我们需要关注的点无外乎下面几个：

- 构造函数需要是 private 访问权限的，这样才能避免外部通过 new 创建实例；
- 考虑对象创建时的线程安全问题；
- 考虑是否支持延迟加载；
- 考虑 getInstance() 性能是否高（是否加锁）。

如果你对这块已经很熟悉了，你可以当作复习。注意，下面的几种单例实现方式是针对 Java 语言语法的。

### 1.1 饿汉式

饿汉式的实现方式比较简单。在类加载的时候，instance 静态实例就已经创建并初始化好了，所以，instance 实例的创建过程是线程安全的。不过，这样的实现方式不支持延迟加载（在真正用到 IdGenerator 的时候，再创建实例），从名字中我们也可以看出这一点。具体的代码实现如下所示：


```java
package main.java.singleton.hunger;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {

    private AtomicLong id = new AtomicLong(0);
    private static final IdGenerator instance = new IdGenerator();
    private IdGenerator() {}
    public static IdGenerator getInstance() {
        return instance;
    }
    public long getId() {
        return id.incrementAndGet();
    }
}
```

## 2 工厂模式

一般情况下，工厂模式分为三种更加细分的类型：简单工厂、工厂方法和抽象工厂。在这三种细分的工厂模式中，简单工厂、工厂方法原理比较简单，在实际项目中也比较常用。而抽象工厂的原理稍微复杂点，在实际的项目中相对不常用。

### 2.1 简单工厂

首先，我们来看下什么是简单工厂模式，通过一个例子来解释一下。

## 3 建造者模式

建造者模式是让建造者类来负责对象的创建工作。实际上，工厂模式是用来创建不同但是相关类型的对象（继承同一父类或者接口的一组子类），有给定参数来决定创建那种类型的对象。建造者模式是用来创建一种类型的复杂对象，通过设置不同的可选参数，“定制化”的创建不同的对象。

建造者模式的使用场景：一个类中有很多属性，为了避免构造函数的参数列表过长，影响代码可读性和易用性。我们可以通过构造函数配合set()方法来解决，但是如果出现以下情况，需要用到建造者模式：

- 类的必填属性放到构造函数中，强制创建对象的时候就设置。如果必填的属性很多，把这些必填参数放到构造函数中设置。那构造函数就会出现参数过长。如果把必填属性通过set()方法设置，那校验这些必填属性是否已经填写的逻辑无处安放。
- 类的属性之间有依赖关系或者约束条件
- 希望创建不可变对象，也就是说，对象在创建好之后，就不能再修改内部的属性值。

下面我们看下具体的代码实现：

```java
package main.java.builder;

import com.sun.deploy.util.StringUtils;

public class ResourcePoolConfig {
    private String name;
    private int maxTotal;
    private int maxIdle;
    private int minIdle;

    private ResourcePoolConfig(Builder builder) {
        this.name = builder.name;
        this.maxTotal = builder.maxTotal;
        this.maxIdle = builder.maxIdle;
        this.minIdle = builder.minIdle;
    }

    public String getName() {
        return name;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }
    public static class Builder {
        private static final int DEFAULT_MAX_TOTAL = 8;
        private static final int DEFAULT_MAX_IDLE = 8;
        private static final int DEFAULT_MIN_IDLE = 0;

        private String name;
        private int maxTotal = DEFAULT_MAX_TOTAL;
        private int maxIdle = DEFAULT_MAX_IDLE;
        private int minIdle = DEFAULT_MIN_IDLE;

        public ResourcePoolConfig build() {
            //校验逻辑放到这里来做，包括必填项校验、依赖关系校验、约束条件校验等。
            if(isBlank(name)) {
                throw new IllegalArgumentException("...");
            }
            if(maxIdle > maxTotal) {
                throw new IllegalArgumentException("...");
            }
            if(minIdle > maxTotal || minIdle > maxIdle) {
                throw new IllegalArgumentException("...");
            }
            return new ResourcePoolConfig(this);
        }
        public Builder setName(String name) {
            if(isBlank(name)) {
                throw new IllegalArgumentException("...");
            }
            this.name = name;
            return this;
        }
        public Builder setMaxTotal(int maxTotal) {
            if(maxTotal <= 0) {
                throw new IllegalArgumentException("...");
            }
            this.maxTotal = maxTotal;
            return this;
        }
        public Builder setMaxIdle(int maxIdle) {
            if(maxIdle < 0) {
                throw new IllegalArgumentException("...");
            }
            this.maxIdle = maxIdle;
            return this;
        }

        public Builder setMinIdle(int minIdle) {
            if(minIdle < 0) {
                throw new IllegalArgumentException("...");
            }
            this.minIdle = minIdle;
            return this;
        }

    }
    public static boolean isBlank(String str) {
        int strLen;
        if(str != null && (strLen = str.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if(!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
    public static void main(String[] args) {
        ResourcePoolConfig config = new ResourcePoolConfig.Builder()
                .setName("dbconnectionpool")
                .setMaxTotal(16)
                .setMaxIdle(16)
                .setMinIdle(12)
                .build();
        System.out.println(config);
    }
}
```