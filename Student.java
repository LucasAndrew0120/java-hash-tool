public class Student
{

    String name;
    int age;
    double score;

    public Student(String name, int age, double score) {
        this.name = name;
        this.age = age;
        this.score = score;
    }

    void introduce()
    {
        System.out.println("我叫" + name + ",今年" + "岁" + age);

    }

    boolean isExcellent()
    {
        if (score >= 85)
        {
            return true;
        } else
            return false;
    }

    public static void main(String args[])
    {
        Student student1 = new Student("a", 12, 78);
        Student student2 = new Student("b", 13, 91);

        student1.introduce();
        student2.isExcellent();
    }
}