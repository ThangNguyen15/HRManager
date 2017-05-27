package model;

/**
 * Created by ThangNguyen on 4/2/2017.
 */
public class Intern extends Worker {

    public Intern(String name, String email, City address, String phoneNumber, String startDate, Task task) {
        super(name, email, address, phoneNumber, startDate, task);
    }

    public Intern(Integer id, String name, String email, City address, String phoneNumber, String startDate, Task task) {
        super(id, name, email, address, phoneNumber, startDate, task);
    }

}
