# 学习笔记
-----------
### 1. 字节码
-----------
#### 字节码类型

按照性质分成4类
1. 栈操作指令 如iconst iload istore
2. 程序类控制指令 如 if_icmpge
3. 对象操作指令 如 invokevirtual
4. 算术运算和类型转化指令 如 iinc

[字节码指令大全](https://www.yuque.com/huaxin803/gb2r7y/gp9xdq#FGtjO)
#### java代码
```java
public class Main{
    public static void main(String[] args) {
        int a = 1 + 2;
        int b = 2 - 1;
        int c = 2 * 2;
        int d = 10 / 2;
        for (int i = 0; i < d; i++) {
            if (i == 0) {
                System.out.println(i);
            }
        }
    }
}
```
---------

#### 编译字节码
```
javac -help 可以列出可能出现的选项 其中-d: 指定放置生成的类文件的位置 -g:var用来生成本地变量表
javac -g:vars -d /Users/kevin/learning/jdk8/target /Users/kevin/learning/jdk8/src/java/Main.java
javap -c -s -v -l /Users/kevin/learning/jdk8/target/Main.class
```
#### 字节码
```
kevindeMBP:java kevin$ javap -v -c -l /Users/kevin/learning/jdk8/target/Main.class
Classfile /Users/kevin/learning/jdk8/target/Main.class
  Last modified 2020-10-21; size 552 bytes
  MD5 checksum b060362ab10cf7d234cea8196e332f99
public class Main
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #5.#24         // java/lang/Object."<init>":()V
   #2 = Fieldref           #25.#26        // java/lang/System.out:Ljava/io/PrintStream;
   #3 = Methodref          #27.#28        // java/io/PrintStream.println:(I)V
   #4 = Class              #29            // Main
   #5 = Class              #30            // java/lang/Object
   #6 = Utf8               <init>
   #7 = Utf8               ()V
   #8 = Utf8               Code
   #9 = Utf8               LocalVariableTable
  #10 = Utf8               this
  #11 = Utf8               LMain;
  #12 = Utf8               main
  #13 = Utf8               ([Ljava/lang/String;)V
  #14 = Utf8               i
  #15 = Utf8               I
  #16 = Utf8               args
  #17 = Utf8               [Ljava/lang/String;
  #18 = Utf8               a
  #19 = Utf8               b
  #20 = Utf8               c
  #21 = Utf8               d
  #22 = Utf8               StackMapTable
  #23 = Class              #17            // "[Ljava/lang/String;"
  #24 = NameAndType        #6:#7          // "<init>":()V
  #25 = Class              #31            // java/lang/System
  #26 = NameAndType        #32:#33        // out:Ljava/io/PrintStream;
  #27 = Class              #34            // java/io/PrintStream
  #28 = NameAndType        #35:#36        // println:(I)V
  #29 = Utf8               Main
  #30 = Utf8               java/lang/Object
  #31 = Utf8               java/lang/System
  #32 = Utf8               out
  #33 = Utf8               Ljava/io/PrintStream;
  #34 = Utf8               java/io/PrintStream
  #35 = Utf8               println
  #36 = Utf8               (I)V
{
  public Main();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   LMain;

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=6, args_size=1 // stack 操作数栈深为2 局部变量表的slot为6 方法入参为1
         0: iconst_3            // 将常量3压入栈
         1: istore_1            // 将栈顶常量3保存进局部变量表slot1中
         2: iconst_1
         3: istore_2
         4: iconst_4
         5: istore_3
         6: iconst_5
         7: istore        4     // 当istore的参数大于3时不再采用istore_n的方式
         9: iconst_0
        10: istore        5
        12: iload         5
        14: iload         4
        16: if_icmpge     38    // 如果一个int类型值大于或者等于另外一个int类型值，则跳转
        19: iload         5
        21: ifne          32    // 如果不等于0，则跳转
        24: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
        27: iload         5
        29: invokevirtual #3                  // Method java/io/PrintStream.println:(I)V
        32: iinc          5, 1 // slot5 + 1
        35: goto          12
        38: return
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
           12      26     5     i   I
            0      39     0  args   [Ljava/lang/String;
            2      37     1     a   I
            4      35     2     b   I
            6      33     3     c   I
            9      30     4     d   I
      StackMapTable: number_of_entries = 3
        frame_type = 255 /* full_frame */
          offset_delta = 12
          locals = [ class "[Ljava/lang/String;", int, int, int, int, int ]
          stack = []
        frame_type = 19 /* same */
        frame_type = 250 /* chop */
          offset_delta = 5
}

```