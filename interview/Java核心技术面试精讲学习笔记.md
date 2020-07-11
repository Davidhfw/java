# 《Java核心技术面试精讲》学习笔记

[TOC]

## 1 谈谈你对Java平台的理解？"Java是解释执行"，这句话正确吗？

### 典型回答

Java本身是一种面向对象的语言，最显著的特性有两个方面，一是所谓的"**书写一次，　到处运行**"，能够非常容易地获得跨平台能力；另外就是**垃圾收集**，Java通过垃圾收集器回收分配内存，大部分情况下，程序员不需要自己操心内存的分配和回收。

我们日常会接触到JRE或者JDK。JRE是java运行环境，包含了JVM和Java类库，以及一些模块等。而JDK可以看做是JRE的一个超集，提供了更多工具，比如编译器、各种诊断工具等。

对于Java解释执行这句话，这个说法不太准确。我们开发的Java的源代码，首先通过Javac编译成字节码，然后在运行时，通过Java虚拟机内嵌的解释器将字节码转换为最终的机器码。但是常见的JVM，比如我们大多数情况下用的Oracle JDK提供的Hotspot JVM，都提供了JIT编译器，也就是通常所说的动态编译器，JIT能够在运行时将热点代码编译成机器码，这种情况下，部分热点代码就属于**编译执行**，而不是解释执行。

### 考点分析

题目很笼统，你要尽量表现出自己的思维深入并系统化，Java知识理解的也比较全面。可根据下图做部分补充。

![](/home/rapheal-wu/learning/Interview/Java/IMG_3100.JPG)

## 2 请对比Exception和Error，另外，运行时异常与一般异常有什么区别？

### 典型回答

Exception和Error都是继承了Throwable类，在Java中只有Throwable类型的实例才可以被抛出或者捕获，它是异常处理机制的基本组成类型。

Exception和Error体现了Java平台设计者对不同异常情况的分类。Exception是程序正常运行中，可以预料的意外情况，可能并且应该被捕获，进行相应处理。

Error是指在正常情况下，不大可能出现的情况，绝大部分的Error都会导致程序（比如JVM自身）处于非正常的、不可恢复状态。既然是非正常情况，所以不便于也不需要捕获，常见的比如OutOfMemoryError之类，都是Error的子类。

Exception又分为可检查异常和不检查异常，可检查异常在源代码里必须显示地进行捕获处理，这是编译期检查的一部分。前面介绍的不可查的Error，是Throwable不是Exception。

不检查异常就是所谓的运行时异常，类似NullPointerException、ArrayIndexOutOfBoundsException之类，通常是可以编码避免的逻辑错误，具体根据需要来判断是否需要捕获，并不会在编译期强制要求。

### 考点分析

- 第一，理解Throwable、Exception、Error的设计和分类。

![](/home/rapheal-wu/learning/Interview/javagit/JavaInterview/Exception_java.JPG)

其中有些子类型，比如**NoClassDefFoundError和ClassNotFoundException**有什么区别，这也是个经典的入门题目。

### 区别

| **ClassNotFoundException**                                   | **NoClassDefFoundError**                          |
| ------------------------------------------------------------ | ------------------------------------------------- |
| 从java.lang.Exception继承，是一个Exception类型               | 从java.lang.Error继承，是一个Error类型            |
| 当动态加载Class的时候找不到类会抛出该异常                    | 当编译成功以后执行过程中Class找不到导致抛出该错误 |
| 一般在执行Class.forName()、ClassLoader.loadClass()或ClassLoader.findSystemClass()的时候抛出 | 由JVM的运行时系统抛出                             |

https://tech101.cn/2018/06/23/ClassNotFoundException_vs_NoClassDefFoundError

- 理解Java语言中操作Throwable的元素和实践。掌握最基本的语法是必须的，如try-catch-finally块，throw、throws关键字等。

### 知识扩展

- 第一，尽量不要捕获类似Exception这样的通用异常，而应该捕获特定异常，如InterruptedException；
- 第二，不要异常；

## 3 谈谈final、finally、finalize有什么不同？

final可以用来修饰类、方法、变量，分别有不同的意义，final修饰的class代表不可以继续扩展，final的变量是不可以修改的，而final的方法也就是不可以重写的(override)。

finally则是Java保证重点代码一定要被执行的一种机制。我们可以使用try-finally或try-catch-finally来进行类似关闭JDBC连接、保证unlock锁等动作。

finalize是基础类java.lang.Object的一个方法，它的设计目的是保证对象在被垃圾收集前完成特定资源的回收。finalize机制现在已经不推荐使用，并且在JDK9开始被标记为deprecated。

## 4 强引用、软引用、弱引用、幻想引用有什么区别？

### 典型回答

不同的引用类型、主要体现的是对象不同的可达性状态和对垃圾收集的影响。

所谓强引用，就是我们最常见的普通对象引用，只要还有强引用指向一个对象，就能表明对象还“活着”，垃圾收集器不会碰这种对象。对于一个普通的对象，如果没有其他的引用关系，只要超过了引用的作用域活着显示地将相应（强）引用赋值为null，就是可以被垃圾收集的了，当然具体回收时机还是要看垃圾收集策略。

软引用，是一种相对强引用弱化一些的引用，可以让对象豁免一些垃圾收集，只有当JVM认为内存不足时，才会去试图回收软引用指向的对象。JVM会确保在抛出OutOfMemoryError之前，清理软引用指向的对象。软引用通常用来实现内存敏感的缓存，如果还有空闲内存，就可以暂时保留缓存，当内存不足时清理掉，这样就保证了使用缓存的同时，不会耗尽内存。

弱引用并不能是对象豁免垃圾收集，仅仅是提供一种访问在弱引用状态下对象的途径。这样就可以用来构建一种没有特定约束的关系，比如，维护一种非强制性的映射关系，如果试图获取时对象还在，就使用它，否则重现实例化。它同样是很多缓存实现的选择。

对于幻想引用，有时候也翻译为虚引用，你不能通过它访问对象。幻象引用仅仅是提供了一种确保对象被finalize以后，做某些事情的机制，比如，通常用来做所谓的Post-Mortem清理机制，Java平台自身的Cleaner机制等，也有人利用幻象引用监控对象的创建和销毁。

### 考点分析

在Java语言中，除了基本数据类型外，其他的都是指向各类对象的对象引用；Java中根据其生命周期的长短，将引用分为4类。

#### 1 强引用

特点：我们平常典型编码Object obj = new Object()中的obj就是强引用。通过关键字new创建的对象所关联的引用就是强引用。  当JVM内存空间不足，JVM宁愿抛出OutOfMemoryError运行时错误（OOM），使程序异常终止，也不会靠随意回收具有强引用的“存活”对象来解决内存不足的问题。对于一个普通的对象，如果没有其他的引用关系，只要超过了引用的作用域或者显式地将相应（强）引用赋值为 null，就是可以被垃圾收集的了，具体回收时机还是要看垃圾收集策略。

#### 2 软引用

特点：软引用通过SoftReference类实现。 软引用的生命周期比强引用短一些。只有当 JVM 认为内存不足时，才会去试图回收软引用指向的对象：即JVM 会确保在抛出  OutOfMemoryError  之前，清理软引用指向的对象。软引用可以和一个引用队列（ReferenceQueue）联合使用，如果软引用所引用的对象被垃圾回收器回收，Java虚拟机就会把这个软引用加入到与之关联的引用队列中。后续，我们可以调用ReferenceQueue的poll()方法来检查是否有它所关心的对象被回收。如果队列为空，将返回一个null,否则该方法返回队列中前面的一个Reference对象。

应用场景：软引用通常用来实现内存敏感的缓存。如果还有空闲内存，就可以暂时保留缓存，当内存不足时清理掉，这样就保证了使用缓存的同时，不会耗尽内存。

#### 3 弱引用

弱引用通过WeakReference类实现。  弱引用的生命周期比软引用短。在垃圾回收器线程扫描它所管辖的内存区域的过程中，一旦发现了具有弱引用的对象，不管当前内存空间足够与否，都会回收它的内存。由于垃圾回收器是一个优先级很低的线程，因此不一定会很快回收弱引用的对象。弱引用可以和一个引用队列（ReferenceQueue）联合使用，如果弱引用所引用的对象被垃圾回收，Java虚拟机就会把这个弱引用加入到与之关联的引用队列中。

应用场景：弱应用同样可用于内存敏感的缓存。

#### 4 虚引用

特点：虚引用也叫幻象引用，通过PhantomReference类来实现。无法通过虚引用访问对象的任何属性或函数。幻象引用仅仅是提供了一种确保对象被 finalize  以后，做某些事情的机制。如果一个对象仅持有虚引用，那么它就和没有任何引用一样，在任何时候都可能被垃圾回收器回收。虚引用必须和引用队列  （ReferenceQueue）联合使用。当垃圾回收器准备回收一个对象时，如果发现它还有虚引用，就会在回收对象的内存之前，把这个虚引用加入到与之关联的引用队列中。
ReferenceQueue queue = new ReferenceQueue ();
PhantomReference pr = new PhantomReference (object, queue); 
程序可以通过判断引用队列中是否已经加入了虚引用，来了解被引用的对象是否将要被垃圾回收。如果程序发现某个虚引用已经被加入到引用队列，那么就可以在所引用的对象的内存被回收之前采取一些程序行动。

应用场景：可用来跟踪对象被垃圾回收器回收的活动，当一个虚引用关联的对象被垃圾收集器回收之前会收到一条系统通知。

## 5 理解Java的字符串，String、StringBuffer、StringBuilder有什么区别？

### 典型回答

String是Java语言非常基础和重要的类，提供了构造和管理字符串的各种基本逻辑。它是典型的Immutable类，被声明成为final class，所有属性也都是final的。也由于它的不可变性，类似于拼接、裁剪字符串等动作，都会产生新的String对象。由于字符串操作的普遍性，所以操作相关的效率往往对应用性能有明显的影响。

StringBuffer是为解决上面提到拼接产生太多中间对象的问题而提供的一个类，我们可以用append或者add方法，把字符串添加到已有序列的末尾或者制定位置。StringBuffer本质是一个线程安全的可修改字符序列，它保证了线程安全，也随之带来了额外的性能开销，所以除非有线程安全的需要，不然还是推荐使用它的后继者，也就是StringBuilder。

StringBuilder是Java1.5中新增的，在能力上和StringBuffer是没有区别的，但是它去掉了线程安全的部分，有效减少了开销，是绝大部分情况下进行字符串拼接的首选。

### 应用场景

- 在字符串内容不经常发生变化的业务场景优先使用String类。例如：常量声明、少量字符串拼接操作等。如果有大量的字符串内容拼接，避免使用"+"操作，因为这样会产生大量无用的中间对象，耗费空间且执行效率低下（新建对象、回收对象花费大量的时间）。
- 在频繁进行字符串的运算（如拼接、替换和删除等），并且运行在多线程环境下，建议使用StringBuffer，例如XML解析、HTTP参数解析与封装。
- 在频繁进行字符串的运算（如拼接、替换和删除等），并且运行在单线程环境下，建议使用StringBuilder，例如SQL语句拼装、JSON封装等。

## 6 谈谈Java反射机制，动态代理是基于什么原理？

### 典型回答

反射机制是Java语言提供的一种基础功能，赋予程序在运行时自省的能力，通过反射我们可以直接操作类或者对象，比如获取某个对象的类定义，获取类声明的属性和方法，调用方法或者构造对象，甚至可以运行时修改类定义。

动态代理是一种方便运行时动态构建代理、动态处理代理方法调用的机制，很多场景都是利用类似机制做到的，比如用来包装RPC调用、面向切面的编程（AOP）。

实现动态代理的方式很多，比如JDK自身提供的动态代理，就是主要利用上面提到的反射机制。还有其他的实现方式，比如利用传说中更高性能的字节码操作机制，类似于ASM、cglib（基于ASM）、Javaassist等。

### 知识扩展

#### 1 反射机制及其演进

Class、Field、Method、Constructor等，这些完全就是操作类和对象的元数据对应。setAccessible的应用场景非常普遍，遍布我们的日常开发、测试、依赖注入等各种框架中。比如，在O/R Mapping框架中，我们为一个Java实体对象，运行时自动生成setter、getter的逻辑，这是加载或者持久化数据非常必要的，框架通常可以利用反射做这个事情，而不需要开发者手动类似的重复代码。

另一个典型场景就是绕过API访问控制。日常开发时可能被迫要调用内部API去做些事情，比如。自定义的高性能NIO框架需要显示地释放DirectBuffer，使用反射绕开限制是一种常见的办法。

#### 2 动态代理

首先，它是一个代理机制。代理可以看作是对调用目标的一个包装，这样我们对目标代码的调用不是直接发生的，而是通过代理完成。其实很多动态代理场景，可以看作是装饰器模式的应用。通过代理可以让调用者与实现者之间解耦。比如进行RPC调用，框架内部的寻址、序列化、反序列化等，通过代理，可以提供更加友善的界面。

cglib动态代理采取的是创建目标类的子类的方式，因为是子类化，我们可以达到近似使用被调用者本身的效果，在Spring编程中，框架会处理这种情况。

JDK Proxy的优势：

- 最小化依赖关系，减少依赖意味着简化开发和维护，JDK本身的支持，可能比cglib更加可靠。
- 平滑进行JDK版本升级，而字节码类库通常需要进行更新以保证在新版Java上能够使用。
- 代码实现简单。

cglib框架的优势：

- 有时候调用目标可能不便实现额外接口，从某种角度看，限定调用者实现接口是有些侵入性的实践，类似于cglib动态代理就没有这种限制。
- 只操作我们关心的类，而不必为其他相关类增加工作量。
- 高性能。

参考资料：

- https://www.cnblogs.com/hanganglin/p/4485999.html
- https://blog.csdn.net/h_xiao_x/article/details/72774496

## 7 int和Integer有什么区别？谈谈Integer的值缓存范围。

### 典型回答

int是整形数字，是Java的8个原始数据类型（boolean、byte、short、char、int、float、double、long）之一。Java语言虽然号称一切都是对象，但原始数据类型是列外。

Integer是int对应的包装类，它有一个int类型的字段存储数据，并且提供了基本操作，比如数学运算、int和字符串之间转换等。在Java5中，引入了自动装箱和自动拆箱功能。Java可以根据山下文，自动进行转换、极大得简化了相关编程。

关于Integer的值缓存，这涉及Java 5中的另一个改进。构建Integer对象的传统方式是直接调用构造器，直接new一个对象。但是根据实践，我们发现大部分数据操作都是集中在有限的、较小的数值范围，因而，在Java中新增了静态工厂方法valueOf，在调用它的时候会利用一个缓存机制，带来了明显的性能改进，这个值默认是-128到127之间。

### 知识扩展

#### 1 理解自动装箱、拆箱

自动装箱实际上是一种语法糖。语法糖可以简单理解为Java平台为我们自动进行一些转换，保证不同的写法在运行时等价，它们发生在编译阶段，也就是生成的字节码是一致的。原则山，建议避免无意中的装箱、拆箱行为。使用原始数据类型、数组甚至本地代码实现等，在性能极度敏感的场景往往具有比较大的优势，用其替换包装类、动态数组等可以作为性能优化的备选项。

## 8　对比Vector、ArrayList、LinkedList有何区别？

### 典型回答

这三者都是实现集合框架中的List，也就是所谓的有序集合，因此具体功能比较近似，比如都提供按照位置进行定位、添加或者删除的操作，都提供迭代器以遍历其内容等。但因为具体的设计区别，在行为、性能、线程安全等方面，表现又有很大不同。

Vector是Java早期提供线程安全的动态数组，如果不需要线程安全，并不建议选择，毕竟同步是有额外开销的。Vector内部是使用对象数组来保存数据，可以根据需要自动的增加容量，当数组已满时，会创建新的数组，并拷贝原有数组数据。

ArrayList是应用更加广泛的动态数组实现，它本身不是线程安全的，所以性能要好很多。与Vector近似，ArrayList也是可以根据需要调整容量，不过两者的调整逻辑有区别，Vector在扩容时会提高一倍，而ArrayList则是增加50%。

LinkedList顾名思义是Java提供的双向链表，所以它不需要像上面两种那样调整容量，它也不是线程安全的。

### 考点分析

不同容器的适用场景：

- Vector和ArrayList作为动态数组，其内部元素以数组形式顺序存储，所以非常适合随机访问的场合，除了尾部插入和删除元素，往往性能会相对较差，比如在中间位置插入元素，需要移动后续所有元素。
- LinkedList进行节点插入、删除要高效的多，但是随机访问性能币动态数组慢。

## 9 对比Hashtable、HashMap、TreeMap有什么不同？

###　典型回答

Hashtable、HashMap、TreeMap都是最常见的一些Map实现，是以键值对的形式存储和操作数据的容器类型。

Hashtable是早期Java类库提供的一个哈希表实现，本身是同步的，不支持null键和值，由于同步导致的性能开销，所以已经很少被推荐使用。

HashMap是应用更加广泛的哈希表实现，行为上大致与HashTable一致，主要区别在于HashMap不是同步的，支持null键和值等。通常情况下，HashMap进行put或者get操作，可以达到常数时间的性能，所以它是绝大部分利用键值对存取场景的首选，比如，实现一个用户ID和用户信息对应的运行时存储结构。

TreeMap则是基于红黑树的一种提供顺序访问的Map，和HashMap不同，它的get，put，remove之类的操作都是O(log(n))的时间复杂度，具体顺序可以由指定的Comparator来决定，或者根据键的自然顺序来判断。

### 知识扩展

#### 1 Map整体结构

![](/home/rapheal-wu/learning/Interview/Java/Map_structure.JPG)()

## 10 如何保证集合是线程安全的？ConcurrentHashMap如何实现高效的线程安全？

### 典型回答

Java提供了不同层面的线程安全支持。在传统集合框架内部，除了Hashtable等同步容器，还提供了所谓的同步包装器，我们可以调用Ｃollections工具类提供的包装方法，来获取一个同步的包装容器，但是它们都是利用非常粗粒度的同步方式，在高并发情况下，性能比较低下。

另外，更加普遍的选择是利用并发包提供的线程安全容器类，它提供了：

- 各种并发容器，比如ConcurrentHashMap、CopyOnWriteArrayList。
- 各种线程安全队列(Queue/Deque)，如ArrayBlockingQueue、SynchronousQueue。
- 各种有序容器的线程安全版本等。

具体保证线程安全的方式，包括有从简单的synchronized方式，到基于更加精细化的、比如基于分离锁实现的ConcurrentHashMap等并发实现等。具体选择要看开发的场景需求，总体来说，并发包内提供的容器通用场景，远优于早期的简单同步实现。

在java8和之后的版本中，ConcurrentHashMap发生了那些变化呢？

- 总体结构上，他的内部存储结构为大的桶数组，然后内部也是一个个所谓的链表结构，同步的粒度更细致一些；
- 其内部仍然有Segment定义，但仅仅是为了保证序列化时的兼容性而已，不再有任何结构上的用处。
- 因为不在使用Segment，初始化操作大大简化，修改为lazy-load形式，这样可以有效避免初始化开销，解决了老版本很多人抱怨的这一点。
- 数据存储利用volatile来保证可见性。
- 使用CAS等操作，在特定场景进行无锁并发操作；
- 使用Unsafe、LongAdder之类底层手段，进行极端情况的优化。

![](/home/rapheal-wu/learning/Interview/javagit/JavaInterview/hashmap_sturcture.PNG)

先看看现在的数据存储内部实现。我们发现Key是final的，与此同时val则声明为volatile，以保证可见性。

```java
static class Node<K, V> implements Map.Entry<K, V> {
    final int hash;
    final K key;
    volatile V val;
    volatile Node<K, V> next;
}
```

## 11 Java提供了那些IO方式？NIO如何实现多路复用？

### 典型回答

Java IO方式有很多种，基于不同的IO抽象模型和交互方式，可以进行简单区分。

首先，传统的java.io包，它基于流模型实现，提供了我们最熟知的一些IO功能，比如File抽象、输入输出流等。交互方式是同步、阻塞的方式，也就是说，在读取输入流或者写入输出流时，在读、写动作完成之前，线程会一直阻塞在哪里，它们之间的调用是可靠的线性顺序。

java.io包的好处是代码比较简单、直观，缺点则是IO效率和扩展性存在局限性，容易成为应用性能的瓶颈。

很多时候，人们也把java.net下面提供的部分网络API，比如Socket、ServerSocket、HttpURLConnection也归类到同步阻塞IO类库，因为网络通信同样是IO行为。

第二，在Java１.4中引入了NIO框架(java.nio包)，提供了Channel、Selector、Buffer等新的抽象，可以构建多路复用的、同步非阻塞IO程序，同时提供了更接近操作系统底层的高性能数据操作方式。

第三，在Java７中，NIO有了进一步的改进，也就是NIO2，引入了异步阻塞非阻塞IO方式，也叫AIO。异步IO操作基于事件和回调机制，可以简单理解为，应用操作直接返回，而不会阻塞在那里，当后台处理完成，操作系统会通知相应线程进行后续工作。

### 考点分析

- 基础API功能与设计，InputStream/OutputStream和Reader/Writer的关系和区别。
- NIO、NIO 2的基本组成。
- 给定场景，分别用不同模型实现，分析BIO、NIO等模式的设计与实现原理。
- NIO提供的高性能数据操作方式是基于什么原理，如何使用？
- 从开发角度看，NIO自身实现存在那些问题？有什么改进的想法吗？

### 知识扩展

首先，澄清一些概念

- 区分同步或异步，简单来说，同步是一种可靠的有序运行机制，当我们进行同步操作时，后续的任务是等待当前调用返回，才会进行下一步；而异步则相反，其他任务不需要等待当前调用返回，通常依靠事件、回调等机制来实现任务间次序关系。
- 区分阻塞与非阻塞。在进行阻塞操作时，当前线程会处于阻塞状态，无法从事其他任务，只有当条件就绪才继续，比如ServerSocket新连接简历完毕，或数据读取、写入操作完成；而非阻塞则是不管IO操作是否结束，直接返回，相应操作在后台继续处理。

java.io

- IO不仅仅是对文件的操作，网络编程中，比如socket通信，都是典型的IO操作目标。
- 输入流、输出流（InputStream/OutputStream）是用于读取或写入字节的，例如操作图片文件。
- 而Reader/Writer则是用于操作字符，增加了字符编解码等功能，适用于类似从文件中读取或者写入文本信息。本质上计算机操作的都是字节，不管是网络通信还是文件读取，而Reader/Writer相当于构建了应用逻辑和原始数据之间的桥梁。
- ＢufferedOutputStream等带缓冲区的实现，可以避免频繁的磁盘读写，进而提高IO处理效率。这种设计利用了缓冲区，将批量数据进行一次操作，但在使用中千万别忘记flush。
- 参考下面这张类图，很多IO工具类都实现了Closeable接口，因为需要进行资源释放。比如，打开FileInputStream，它就会获取相应的文件描述符，需要利用try-with-resources、try-finally等机制保证FileInputStream被明确关闭，进而相应文件描述符也会失效，否则将导致资源无法被释放。利用Cleaner或finalize机制作为资源释放的最后把关，也是很有必要。

![](/home/rapheal-wu/learning/Interview/javagit/JavaInterview/io.JPG)

Java NIO概览

首先熟悉下NIO的主要组成部分：

- Buffer，高效的数据容器，除了布尔类型，所有原始数据类型都有对应的Buffer实现。
- Channel，类似于Linux之类的操作系统上看到的文件描述符，是NIO中被用来支持批量式IO操作的一种抽象。File或Socket，通常被认为是比较高层次的抽象，而Channel则是更加操作系统底层的一种抽象，这也使得NIO得以充分利用现代操作系统底层机制，获得特定场景的性能优化，例如DMA(Direct Memory Access)等，不同层次的抽象是相互关联的，可以通过Socket获取Channel，反之亦然。
- Selector，是NIO实现多路复用的基础，它提供了一种高效的机制，可以检测到注册在Selector上的多个Channel中，是否有Channel处于就绪状态，进而实现了单线程对多Channel的高效管理

## 12 Java有几种文件拷贝方式？哪一种最高效？

### 典型回答

Java有多种比较典型的文件拷贝实现方式，比如：

利用java.io类库，直接为源文件构建一个FileInputStream读取，然后再为目标文件构建一个FileOutputStream，完成写入工作。

```java
public static void copyFileByStream(File source, File dest) throws IOException {
    try (InputStream is = new FileInputStream(source);
        OutputStream os = new FileOutputStream(dest);) {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
    }
}
```

或者，利用java.nio类库提供的transferTo或者transferFrom方法实现。

```java
public static void copyFileByChannel(File source, File dest) throws IOException {
    try (FileChannel sourceChannel = new FileInputStream(source).getChannel();
        FileChannel targetChannel = new FileOutputStream(dest).getChannel();) {
        for (long count = sourceChannel.size(); count > 0;) {
            long transferred = sourceChannel.transferTo(sourceChannel.position(), count, targetChannel);
            count -= transferred;
        }
    }
}
```

当然，Java标准类库本身已经提供了几种Files.copy()的实现。

对于Copy的效率，这个其实和操作系统和配置等情况有关，总体上来说，NIO transferTo/From的方式可能更快，因为它能更利用现代操作系统底层机制，避免不必要拷贝和上下文切换。

### 考点分析

- 不同的copy方式，底层机制有什么区别？
- 为什么零拷贝可能有性能优势？
- Buffer分类与使用。
- Direct Buffer对垃圾收集等方面的影响与实践选择。

### 知识扩展

#### 1 拷贝实现机制分析

首先，你需要理解用户态空间和内核态空间，操作系统内核、硬件驱动等运行在内核态空间，具有相对高的特权；而用户态空间，则是给普通应用和服务使用的。

当使用输入输出流进行读写时，实际上是进行了多次上下文切换，比如应用读取数据时，先在内核态将数据从磁盘读取到内存中，再切换到用户态将数据从内核缓存读取到用户缓存。写入也是类似操作，仅仅步骤相反。

而基于NIO transferTo的实现方式，在Linux和Unix上，则会使用到零拷贝技术，数据传输并不需要用户态参与，省去了上下文切换的开销和不必要的内存拷贝，进而可能提高了应用拷贝性能。注意，transferTo不仅仅是可以用在文件拷贝中，也可以用在读取磁盘文件，然后进行Socket发送，同样可以享受这种机制带来的性能和扩展性提高。

### 2 Java IO/NIO源码结构

Java标准库也提供了文件拷贝方法(java.nio.file.Files.copy)。它是如何实现的呢？

实际上有几个不同的copy方法。

```java
public static Path copy(Path source, Path target, CopyOption... options) throws IOException
public static long copy(InputStream in, Path target, CopyOption...	options) throws IOException
public static long copy(Path source, OutputStream out) throws IOException
```

可以看到，copy不仅仅是支持文件之间操作，没有限定输入输出流一定是针对文件的，这是两个很实用的工具方法。

所以，这个最常见的copy方法其实不是利用transferTo，　而是本地技术实现的用户态拷贝。

提高类似拷贝等IO操作的性能，有一些宽泛的原则：

- 在程序中，使用缓存等机制，合理减少IO次数
- 使用transferTo等机制，减少上下文切换和额外IO操作。
- 尽量减少不必要的转换过程，比如编解码；对象序列化和反序列化，比如操作文本文件或者网络通信，如果不是过程中需要使用文本信息，可以考虑不要将二进制信息转换成字符串，直接传输二进制信息。

### 3 掌握NIO Buffer

Buffer 有几个基本属性：

- capacity，它反映这个Buffer到底有多大，也就是数组的长度。
- position，要操作的数据其实位置。
- limit，相当于操作的限额。
- mark，记录上一次position的位置，默认是0。

### 4 Direct Buffer和垃圾收集

- Direct Buffer：如果我们看Buffer的方法定义，你会发现它定义了isDirect()方法，返回当前Buffer是否是Direct类型。这是因为Java提供了堆内和堆外(Direct)Buffer，我们可以以它的allocate或者allocateDirect方法直接创建。
- MappedByteBuffer：它将文件按照指定大小映射为内存区域，当程序访问这个内存区域时将直接操作这块文件数据，省去了将数据从内核空间向用户空间传输的损耗，本质上也是一种Direct Buffer。

## 13 谈谈接口和抽象类有什么区别？

### 典型回答

接口和抽象类是Java面向对象设计的两个基础机制。

接口是对行为的抽象，他是抽象方法的集合，利用接口可以达到API定义和实现分离的目的。接口，不能实例化；不能包含任何非常量成员，任何field都是隐含着public static final的意义；同时，没有非静态方法实现，也就是说要么是抽象方法，要么是静态方法。Java标准类库中，定义了非常多的接口，如java.util.List。

抽象类是不能实例化的类，用abstact关键字修饰class，其目的主要是代码重用。除了不能实例化，形式上和一般的Java类并没有太大的区别，可以有一个或多个抽象方法，也可以没有抽象方法。抽象类大多用于抽取相关Java类的共用方法实现或者是共同成员变量，然后通过继承的方式达到代码复用的目的。Java标准库中，比如collection框架，很多通用部分被抽取成为抽象类，比如java.util.AbstractList。

Java类实现interface使用implements关键词，继承abstract class则使用extends关键词。

### 知识扩展

java不支持多继承。

### 面向对象设计

- 封装：目的是影藏事务内部的实现细节，以便提高安全性和简化编程。
- 继承：代码复用的基础机制，继承可以看作是非常紧耦合的一种关系，父类代码修改，子类行为也会变动，在实践中过度滥用继承，可能会起到反效果。
- 多态：你可能会立即想到重写和重载、向上转型。简单来说，重写是父子类中相同名字和参数的方法，不同的实现；重载则是相同名字的方法，但是不同的参数，本质上，这些方法签名是不一样的。

面向对象编程，掌握基本的设计原则是必须的，今天介绍最通用的部分，也就是所谓的S.O.L.I.D原则。

- 单一职责，类或者对象最好是只有单一职责，在程序设计中如果发现某个类承担着多种义务，可以考虑进行拆分。
- 开关原则，设计要对扩展开放，对修改关闭，换句话说，程序设计应保证平滑的扩展性，尽量避免因为新增同类功能而修改已有实现，这样可以少产出些回归问题。
- 里氏替换，这是面向对象的基本要素之一，进行继承关系抽象时，凡是可以用父类或者基类的地方，都可以用子类替换。
- 接口分离：进行接口和类设计时，如果在一个接口里面定义了太多的方法，其子类很可能面对两难，就是只有部分方法对它是有意义的，这就破坏了程序的内聚性。对于这种情况，可以通过拆分成单一的多个接口，将行为进行解耦。在未来维护中，如果某个接口设计有变，不会对使用其他接口的子类构成影响。
- 依赖反转，实体应该依赖于抽象而不是实现。也就是说高层次模块，不应该依赖于低层次模块，而是应该基于抽象。实践这一原则是保证产品代码之间适当耦合度的法宝。

## 14 谈谈你知道的设计模式？请手动实现单例模式，Spring等框架中使用了那些模式？

### 典型回答

大致按照模式的应用目标分类，设计模式可以分为创建型模式、结构型模式和行为型模式。

- 创建型模式，是对对象创建过程的各种问题和解决方案的总结，包括各种工厂模式，单例模式、构建器模式、原型模式。
- 结构型模型，是针对软件设计结构的总结，关注于类、对象继承、组合方式的实践经验。常见的结构型模式，包括桥接模式、适配器模式、装饰者模式、代理模式、组合模式、外观模式、享元模式等。
- 行为型模式，是从类或对象之间交互、职责划分等角度总结的模式。比较常见的行为型模式有策略模式、解释器模式、命令模式、观察者模式、迭代器模式、模板方法模式和访问者模式。

### 考点分析

识别装饰器模式，可以通过识别类设计特征来进行判断，也就是其类构造函数以相同的抽象类或者接口为输入参数。

例如，BufferedInputStream经过包装，为输入流过程增加缓存，类似这种装饰器还可以进行多次嵌套，不断地增加不同层次的功能。

```java
public BufferedInputStream(InputStream in)
```

工厂模式：下面这个创建HttpRequest的过程，就是典型的构建器模式，通常会被实现成fluent风格的API，也有人叫方法链。

```java
HttpRequest request = HttpRequest.newBuilder(new URL(url))
    											.header(headerAlice, valueAlice)
    											.headers(headerBob, valueBob, 
                                                        headerCarl, valueCarl, 
                                                        headerBob, value2Bob)
    											.GET()
    											.build();
```

### 知识扩展

实现一个日常非常熟悉的单例模式。

```java
public class Singleton{
    private static Singleton instance = new Singleton();
    public static Singleton getInstance() {
        return instance;
    }
}
```

是不是感觉缺了点什么？原来，Java会自动为没有明确声明构造函数的类，定义一个public的无参数的构造函数，所以上面的例子并不能保证额外的对象不会被创建出来。

可以为单例定义一个private的构造函数，专栏第10讲中介绍了ConcurrentHashMap时，提到过标准类库中很多地方使用懒加载，改善初始内存开销，单例模式同样适用。

```java
public class Singleton{
    private static Singleton instance;
    private Singleton() {
    }
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

这个实现在单线程环境中不存在问题，但是如果处于并发场景，就需要考虑线程安全，最熟悉的莫过于“双检锁”，其要点在于：

- 这里的volatile能够提供可见性，以及保证getInstance返回的初始化安全的对象。
- 在同步之后进行null检查，以尽量避免进入相对昂贵的同步块。
- 直接在class级别进行同步，保证线程安全的类方法调用。

```java
public class Singleton{
    private static volatile Singleton singleton = null;
    private Singleton() {
    }
    public static Singleton getSingleton() {
        if (singleton == null) { //尽量避免重复进入同步块
            synchronized (Singleton.class) {// 同步.class，　意味着对同类方法调用
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}
```

Spring 如何在API设计中使用设计模式，你至少要有个大体印象。如：

- BeanFactory和ApplicationContext应用了工厂模式；
- 在Bean的创建中，Spring也为不同scope定义的对象，提供了单例和原型等模式实现；
- AOP领域则是使用了代理模式、装饰器模式、适配器模式等。
- 各种事件监听器，是观察者模式的典型应用。
- 类似JdbcTemplate等则是应用了模板模式。

## 15 synchronized和ReentrantLock有什么区别？有人说synchronized最慢，这话靠谱吗？

### 典型回答

synchronized是Java内建的同步机制，所以也有人称其为Intrinsic Locking，它提供了互斥的语义和可见性，当一个线程已经获取当前锁时，其它试图获取的线程只能等待或者阻塞在那里。

在Java５以前，synchronized是仅有的同步手段，在代码中，sychronized可以用来修饰方法，也可以使用在特定的代码块上，本质上synchronized方法等同于把方法全部语句用synchronized块包起来。

ReentrantLock，通常翻译为再入锁，是Java 5提供的锁实现，他的语义和synchronized基本相同。再入锁通过代码直接调用lock()方法获取，代码书写也更加灵活。与此同时，ReentrantLock提供了很多实用的方法，能够实现很多synchronized无法做到的细节控制，比如可以控制fairness，也就是公平性，或者利用定义条件等。但是，编码中也需要注意，必须要明确调用unlock()方法释放，不然就会一直持有该锁。

synchronized和ReentrantLock的性能不能一概而论，早期版本synchronized在很多场景向下性能相差较大，在后续版本进行了较多改进，在低竞争场景中表现可能优于ReentrantLock。

### 知识扩展

锁作为并发的基础工具之一，你至少需要掌握：

- 理解什么是线程安全。
- synchronized、ReentrantLock底层实现；理解膨胀锁、降级；理解偏斜锁、自旋锁、轻量级锁、重量级锁等概念。
- 掌握并发包中java.util.concurrent.lock各种不同实现和案例分析。

### 知识扩展

#### 1 线程安全

- 原子性，简单说就是相关操作不会中途被其他线程干扰，一般通过同步机制实现。
- 可见性，是一个线程修改了某个共享变量，其状态能够立即被其他线程知晓，通常被解释为将线程本地的状态反映到主内存上，volatile就是负责保证可见性的。
- 有序性，是保证线程内串行语义，避免指令重排等。

## 17 一个线程两次调用start()方法会出现什么情况呢？谈谈线程的生命周期和状态转移。

### 典型回答

Java线程是不允许启动两次的，第二次调用必然会抛出IllegalThreadStateException，这是一种运行时异常，多次调用start被认为是编程错误。

关于线程生命周期的不同状态，在Java5以后，线程状态被明确定义在其公共内部枚举类型java.lang.Thread.State中，分别是：

- 新建，表示状态被创建出来还没有真正启动的状态，可以认为它是个Java内部状态；

- 就绪，表示该线程已经在JVM中执行，当然由于执行需要计算资源，它可能是正在运行，也可能还在等待系统分配给它CPU片段，在就绪队列里面排队。

- 在其他一些分析中，会额外区分一种状态RUNNING，但是从Java API角度，并不能表示出来。

- 阻塞，这个状态和我们前面两讲介绍的同步非常相关，阻塞表示线程在等待Monitor Lock。比如，线程试图通过Synchronized去获取某个锁，但是其他线程已经独占了，那么当前线程就会处于阻塞状态。

- 等待，表示正在等待其他线程采取某些操作，一个常见的场景是类似生产者消费者模式，发现任务条件尚未满足，就让当前消费者线程等待，另外的生产者线程去准备任务数据，然后通过类似notify等动作，通知消费线程可以继续工作了，Thread.join()也会令线程进入等待状态。

- 计时等待，其进入条件和等待状态类似，但是调用的是存在超时条件的方法，比如wait或join等方法的指定超时版本，如下面示例：

  ```java
  public 	final native void wait(long timeout) throws InterruptedException;
  ```

- 终止，不管是意外退出还是正常执行结束，线程已经完成使命，终止运行。

在第二次调用start()方法的时候，线程可能处于终止或者其他(非NEW)状态，但是不论如何，都是不可以再次启动的。

### 知识扩展

#### 1 线程是什么？

从操作系统的角度，可以简单认为，线程是系统调度的最小单元，一个进程可以包含多个线程，作为任务的真正运作着，有自己的栈、寄存器、本地存储等，但是会和进程内其他线程共享文件描述符、虚拟地址空间等。

在具体实现中，线程还分为内核线程和用户线程，现在的模型是一对一映射到操作系统内核线程。

## 26 如何监控和诊断JVM堆内和堆外内存的使用？

### 典型回答

了解JVM内存的方法有很多，具体能力范围也有区别，简单总结如下：

- 可以使用综合性的图形化工具，如JConsole、VisualVM等，这些工具具体使用起来相对比较直观，直接连接到Java进程，然后就可以在图形化界面里掌握内存的使用情况。

以JConsole为例，其内存页面可以显示常见的堆内存和各种堆外部分使用状态。

- 也可以用命令行工具进行运行时查询，如jstat和jmap等工具都提供一些选项，可以查看堆、方法取等使用数据。
- 使用jmap等提供的命令，生成堆转储文件，然后利用jhat或Eclipse MAT等堆存储分析工具进行详细分析。
- 如果你使用的是Tomcat、Weblogic等Java EE服务器，这些服务器同样提供了内存管理相关的功能。
- 另外，从某种程度上将，GC日志等输出，同样包含着丰富的信息。

这里有一个相对特殊的部分，就是堆外存