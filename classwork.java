import java.util.*;
public class classwork
{
    char name;
    static int number; // 学号
    int classnumber;

    // 写个类方法
    void setname()
    {
        System.out.println("姓名：" + name);
    }

    static void printname(classwork student)
    {
        System.out.println("姓名：" + student.name);
    }

    // 写个函数
    void setnumber()
    {
        System.out.println("学号：" + number);
    }

    public static void main(String[] args)
    {
        classwork student1 = new classwork();
        student1.name = 'A';
        classwork.number = 123456;
        student1.classnumber = 1;

        classwork student2 = new classwork();
        student2.name = 'B';
        classwork.number = 654321;
        student2.classnumber = 2;

        // 调用类方法
        printname(student1);
        // 调用函数
        student1.setnumber();
        student2.setnumber();
    }
}