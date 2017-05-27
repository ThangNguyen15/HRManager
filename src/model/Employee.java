package model;

import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.MetaConstants;

/**
 * Created by ThangNguyen on 4/2/2017.
 */
public class Employee extends Worker {
    @DAttr(name = "position", type = DAttr.Type.Domain, length = 20, optional = false)
    @DAssoc(ascName = "employee-has-position", role = "employee", ascType = DAssoc.AssocType.One2Many,
            endType = DAssoc.AssocEndType.Many, associate = @DAssoc.Associate(type = Position.class, cardMin = 1, cardMax = 1))
    private Position position;

    @DAttr(name = "attendance", type = DAttr.Type.Domain, optional = false)
    @DAssoc(ascName = "employee-has-attendance", role = "employee", ascType = DAssoc.AssocType.One2Many,
            endType = DAssoc.AssocEndType.Many, associate = @DAssoc.Associate(type = Attendance.class, cardMin = 1, cardMax = 1))
    private Attendance attendance;

    @DAttr(name = "department", type = DAttr.Type.Domain, length = 20, optional = true)
    @DAssoc(ascName = "employee-has-department", role = "employee",
            ascType = DAssoc.AssocType.One2Many, endType = DAssoc.AssocEndType.Many,
            associate = @DAssoc.Associate(type = Department.class, cardMin = 1, cardMax = 1))
    private Department department;

    @DAttr(name = "performanceReport", type = DAttr.Type.Domain, length = 20, optional = true)
    @DAssoc(ascName = "performanceReport-has-employee", role = "employee",
            ascType = DAssoc.AssocType.One2Many, endType = DAssoc.AssocEndType.Many,
            associate = @DAssoc.Associate(type = PerformanceReport.class, cardMin = 1, cardMax = 1))
    private PerformanceReport performanceReport;

    @DAttr(name = "salary", type = DAttr.Type.Domain, optional = true, mutable = false)
    @DAssoc(ascName = "employee-has-salary", role = "employee", ascType = DAssoc.AssocType.Many2Many,
            endType = DAssoc.AssocEndType.Many, associate = @DAssoc.Associate(type = Salary.class, cardMin = 1, cardMax = MetaConstants.CARD_MORE))
    private Salary salary;

    public Employee(String name, String email, City address, String phoneNumber, String startDate, Task task, Position position,
                    Attendance attendance, Department department, PerformanceReport performanceReport,
                    Salary salary) {
        super(name, email, address, phoneNumber, startDate, task);
        this.position = position;
        this.attendance = attendance;
        this.department = department;
        this.performanceReport = performanceReport;
        this.salary = salary;
    }

    public Employee(Integer id, String name, String email, City address, String phoneNumber, String startDate, Task task, Position position,
                    Attendance attendance, Department department, PerformanceReport performanceReport, Salary salary) {
        super(id, name, email, address, phoneNumber, startDate, task);
        this.position = position;
        this.attendance = attendance;
        this.department = department;
        this.performanceReport = performanceReport;
        this.salary = salary;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public PerformanceReport getPerformanceReport() {
        return performanceReport;
    }

    public void setPerformanceReport(PerformanceReport performanceReport) {
        this.performanceReport = performanceReport;
    }

    public Salary getSalary() {
        return salary;
    }
}
