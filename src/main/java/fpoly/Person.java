package fpoly;

public class Person {
    private final String name;
    private final int age;

    public Person(String name, int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Invalid age: " + age);
        }
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
