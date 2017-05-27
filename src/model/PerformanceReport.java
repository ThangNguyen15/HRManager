package model;

import java.util.ArrayList;
import java.util.List;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.def.Associate;
import domainapp.basics.model.meta.*;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.util.Tuple;


/**
 * Created by ThangNguyen on 5/15/2017.
 */
public class PerformanceReport {
    @DAttr(name = "id", id = true, auto = true, length = 3, mutable = false, optional = false, type = DAttr.Type.Integer)
    private int id;

    private static int idCounter;

    @DAttr(name = "month", type = Type.String, length = 20, optional = false, mutable = true)
    private String month;

    @DAttr(name = "employees", type = DAttr.Type.Collection, optional = false,
            serialisable = false, filter = @Select(clazz = Employee.class))
    @DAssoc(ascName = "performanceReport-has-employee", role = "performanceReport",
            ascType = DAssoc.AssocType.One2Many, endType = DAssoc.AssocEndType.One,
            associate = @DAssoc.Associate(type = Employee.class, cardMin = 1, cardMax = MetaConstants.CARD_MORE))
    private List<Employee> employees;
    private int employeesCount;

    @DAttr(name = "attendances", type = DAttr.Type.Collection, optional = true,
            serialisable = false, filter = @Select(clazz = Attendance.class))
    @DAssoc(ascName = "performanceReport-has-attendance", role = "performanceReport",
            ascType = AssocType.One2Many, endType = AssocEndType.One,
            associate = @DAssoc.Associate(type = Attendance.class, cardMin = 1, cardMax = MetaConstants.CARD_MORE))
    private List<Attendance> attendances;
    private int attendancesCount;

    @DAttr(name = "performanceRating", type = DAttr.Type.String, mutable = false, optional = true, length = 20)
    private String performanceRating;

    public PerformanceReport(String month) {
        this(null, month, null);
    }

    public PerformanceReport(String month, String performanceRating) {
        this(null,month,performanceRating);
    }

    public PerformanceReport(Integer id, String month, String performanceRating) {
        this.id = nextId(id);
        this.month = month;
        this.performanceRating = performanceRating;

        employees = new ArrayList<>();

        attendances = new ArrayList<>();
    }


    public Integer getId() {
        return id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getPerformanceRating() {
        return performanceRating;
    }

    //Report has many employees
    @DOpt(type = DOpt.Type.LinkAdder)
    // only need to do this for reflexive DAssoc:
    // @MemberRef(name="employee")
    public boolean addEmployee(Employee employee) {
        if (!this.employees.contains(employee)) {
            employees.add(employee);
            updatePerformanceRating(employee);
        }
        return true;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewEmployee(Employee employee) {
        employees.add(employee);
        employeesCount++;
        updatePerformanceRating(employee);
        return true;
    }

    @DOpt(type = DOpt.Type.LinkAdder)
    public boolean addPerformance(List<Employee> employees) {
        for (Employee employee : employees) {
            if (!this.employees.contains(employee)) {
                this.employees.add(employee);
                updatePerformanceRating(employees);
            }
        }

        return true;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewEmployee(List<Employee> employees) {
        this.employees.addAll(employees);
        employeesCount += employees.size();
        updatePerformanceRating(employees);
        // no other attributes changed
        return true;
    }

    @DOpt(type = DOpt.Type.LinkRemover)
    //only need to do this for reflexive DAssoc: @MemberRef(name="employees")
    public boolean removeEmployee(Employee employee) {
        boolean removed = employees.remove(employee);

        if (removed) {
            updatePerformanceRating(employee);
            employeesCount--;
        }

        // no other attributes changed
        return true;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
        employeesCount = employees.size();
    }

    public List<Employee> getEmployees() {
        return this.employees;
    }

    @DOpt(type = DOpt.Type.LinkCountGetter)
    public int getEmployeesCount() {
        return employeesCount;
    }

    @DOpt(type = DOpt.Type.LinkCountSetter)
    public void setEmployeesCount(int employeesCount) {
        this.employeesCount = employeesCount;
    }

    //Report has many attendances
    @DOpt(type = DOpt.Type.LinkAdder)
    // only need to do this for reflexive DAssoc:
    // @MemberRef(name="attendance")
    public boolean addAttendance(Attendance attendance) {
        if (!this.attendances.contains(attendance)) {
            attendances.add(attendance);
            updatePerformanceRate(attendance);
        }
        return true;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewAttendance(Attendance attendance) {
        attendances.add(attendance);
        attendancesCount++;
        updatePerformanceRate(attendance);
        return true;
    }

    @DOpt(type = DOpt.Type.LinkAdder)
    public boolean addAttendance(List<Attendance> attendances) {
        for (Attendance attendance : attendances) {
            if (!this.attendances.contains(attendance)) {
                this.attendances.add(attendance);
                updatePerformanceRate(attendances);
            }
        }

        return true;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewAttendance(List<Attendance> attendances) {
        this.attendances.addAll(attendances);
        attendancesCount += attendances.size();
        updatePerformanceRate(attendances);
        // no other attributes changed
        return true;
    }

    @DOpt(type = DOpt.Type.LinkRemover)
    //only need to do this for reflexive DAssoc: @MemberRef(name="attendance")
    public boolean removeAttendance(Attendance attendance) {
        boolean removed = attendances.remove(attendance);

        if (removed) {
            updatePerformanceRate(attendance);
            attendancesCount--;
        }
        // no other attributes changed
        return true;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
        attendancesCount = attendances.size();

    }

    public List<Attendance> getAttendances() {
        return this.attendances;
    }

    @DOpt(type = DOpt.Type.LinkCountGetter)
    public Integer getAttendancesCount() {
        return attendancesCount;
    }

    @DOpt(type = DOpt.Type.LinkCountSetter)
    public void setAttendancesCount(Integer attendancesCount) {
        this.attendancesCount = attendancesCount;
    }

    public boolean updatePerformanceRating(Employee employee) {
        Integer dayAbsence = employee.getAttendance().getDayAbsence();
        if (dayAbsence == 0) {
            performanceRating = "Good";
        } else if (dayAbsence > 0 && dayAbsence < 2) {
            performanceRating = "Normal";
        } else {
            performanceRating = "Bad";
        }
        return true;
    }

    @DOpt(type = DOpt.Type.LinkUpdater)
    public boolean updatePerformanceRating(List<Employee> employees) {
        for (Employee employee : employees) {
            Integer dayAbsence = employee.getAttendance().getDayAbsence();
            if (dayAbsence == 0) {
                performanceRating = "Good";
            } else if (dayAbsence > 0 && dayAbsence < 2) {
                performanceRating = "Normal";
            } else {
                performanceRating = "Bad";
            }
        }
        return true;
    }

    @DOpt(type = DOpt.Type.LinkUpdater)
    public boolean updatePerformanceRate(Attendance attendance) {
        Integer dayAbsence = attendance.getDayAbsence();
        if (dayAbsence == 0) {
            performanceRating = "Good";
        } else if (dayAbsence > 0 && dayAbsence < 2) {
            performanceRating = "Normal";
        } else {
            performanceRating = "Bad";
        }
        return true;
    }

    @DOpt(type = DOpt.Type.LinkUpdater)
    public boolean updatePerformanceRate(List<Attendance> attendances) {
        for (Attendance attendance : attendances) {
            Integer dayAbsence = attendance.getDayAbsence();
            if (dayAbsence == 0) {
                performanceRating = "Good";
            } else if (dayAbsence > 0 && dayAbsence < 2) {
                performanceRating = "Normal";
            } else {
                performanceRating = "Bad";
            }
        }
        return true;
    }

    //Auto Generate ID Section

    /**
     * @return id counter
     */
    public static int getIdCounter() {
        return idCounter;
    }

    /**
     * Generate id for position
     *
     * @param currID
     * @return
     */
    private static Integer nextId(Integer currID) {
        if (currID == null) {
            idCounter++;
            return idCounter;
        } else {
            if (currID > idCounter) {
                idCounter = currID;
            }
            return currID;
        }
    }

    /**
     * @param attrib
     * @param derivingValue
     * @param minVal
     * @param maxVal
     * @requires minVal != null /\ maxVal != null
     * @effects update the auto-generated value of attribute <tt>attrib</tt>,
     * specified for <tt>derivingValue</tt>, using <tt>minVal, maxVal</tt>
     */
        @DOpt(type = DOpt.Type.AutoAttributeValueSynchroniser)
    public static void updateAutoGeneratedValue(
            DAttr attrib,
            Tuple derivingValue,
            Object minVal,
            Object maxVal) throws ConstraintViolationException {

        if (minVal != null && maxVal != null) {
            if (attrib.name().equals("id")) {
                int maxIdVal = (Integer) maxVal;
                if (maxIdVal > idCounter)
                    idCounter = maxIdVal;
            }
        }
    }
}
