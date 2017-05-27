package model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.*;
import domainapp.basics.util.Tuple;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThangNguyen on 5/27/2017.
 */
public class SalaryReport {
    @DAttr(name = "id", id = true, auto = true, length = 3, mutable = false, optional = false, type = DAttr.Type.Integer)
    private int id;

    private static int idCounter;

    @DAttr(name = "month", type = DAttr.Type.String, length = 20, optional = false, mutable = true)
    private String month;

    @DAttr(name = "employees", type = DAttr.Type.Collection, optional = false,
            serialisable = false, filter = @Select(clazz = Employee.class))
    @DAssoc(ascName = "salaryReport-has-employee", role = "salaryReport",
            ascType = DAssoc.AssocType.One2Many, endType = DAssoc.AssocEndType.One,
            associate = @DAssoc.Associate(type = Employee.class, cardMin = 1, cardMax = MetaConstants.CARD_MORE))
    private List<Employee> employees;
    private int employeesCount;

    @DAttr(name = "salaries", type = DAttr.Type.Collection, optional = true,
            serialisable = false, filter = @Select(clazz = Salary.class))
    @DAssoc(ascName = "salaryReport-has-salary", role = "salaryReport",
            ascType = DAssoc.AssocType.One2Many, endType = DAssoc.AssocEndType.One,
            associate = @DAssoc.Associate(type = Salary.class, cardMin = 1, cardMax = MetaConstants.CARD_MORE))
    private List<Salary> salaries;
    private int salariesCount;

    @DAttr(name = "totalSalary", type = DAttr.Type.Integer, mutable = false, optional = true, length = 20, min = 0)
    private int totalSalary;

    public SalaryReport(String month) {
        this(null, month, null);
    }

    public SalaryReport(String month, Integer totalSalary) {
        this(null, month, totalSalary);
    }

    public SalaryReport(Integer id, String month, Integer totalSalary) {
        this.id = nextId(id);
        this.month = month;
        this.totalSalary = (totalSalary != null) ? totalSalary : 0;

        employees = new ArrayList<>();

        salaries = new ArrayList<>();
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

    public Integer getTotalSalary() {
        return totalSalary;
    }

    //Report has many employees
    @DOpt(type = DOpt.Type.LinkAdder)
    // only need to do this for reflexive DAssoc:
    // @MemberRef(name="employee")
    public boolean addEmployee(Employee employee) {
        if (!this.employees.contains(employee)) {
            employees.add(employee);
            updateTotalCost(employee);
        }
        return true;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewEmployee(Employee employee) {
        employees.add(employee);
        employeesCount++;
        updateTotalCost(employee);
        return true;
    }

    @DOpt(type = DOpt.Type.LinkAdder)
    public boolean addPerformance(List<Employee> employees) {
        for (Employee employee : employees) {
            if (!this.employees.contains(employee)) {
                this.employees.add(employee);
                updateTotalCost(employees);
            }
        }

        return true;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewEmployee(List<Employee> employees) {
        this.employees.addAll(employees);
        employeesCount += employees.size();
        updateTotalCost(employees);
        // no other attributes changed
        return true;
    }

    @DOpt(type = DOpt.Type.LinkRemover)
    //only need to do this for reflexive DAssoc: @MemberRef(name="employees")
    public boolean removeEmployee(Employee employee) {
        boolean removed = employees.remove(employee);

        if (removed) {
            updateTotalCost(employee);
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
    public boolean addSalary(Salary salary) {
        if (!this.salaries.contains(salary)) {
            salaries.add(salary);
            updateTotalSalary(salary);
        }
        return true;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewSalary(Salary salary) {
        salaries.add(salary);
        salariesCount++;
        updateTotalSalary(salary);
        return true;
    }

    @DOpt(type = DOpt.Type.LinkAdder)
    public boolean addSalary(List<Salary> salaries) {
        for (Salary salary : salaries) {
            if (!this.salaries.contains(salary)) {
                this.salaries.add(salary);
                updateTotalSalary(salaries);
            }
        }

        return true;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewSalary(List<Salary> salaries) {
        this.salaries.addAll(salaries);
        salariesCount += salaries.size();
        updateTotalSalary(salaries);
        // no other attributes changed
        return true;
    }

    @DOpt(type = DOpt.Type.LinkRemover)
    //only need to do this for reflexive DAssoc: @MemberRef(name="attendance")
    public boolean removeAttendance(Salary salary) {
        boolean removed = salaries.remove(salary);

        if (removed) {
            updateTotalSalary(salary);
            salariesCount--;
        }
        // no other attributes changed
        return true;
    }

    public void setSalaries(List<Salary> salaries) {
        this.salaries = salaries;
        salariesCount = salaries.size();

    }

    public List<Salary> getSalaries() {
        return this.salaries;
    }

    @DOpt(type = DOpt.Type.LinkCountGetter)
    public Integer getSalariesCount() {
        return salariesCount;
    }

    @DOpt(type = DOpt.Type.LinkCountSetter)
    public void setSalariesCount(Integer salariesCount) {
        this.salariesCount = salariesCount;
    }

    public boolean updateTotalCost(Employee employee) {
        this.totalSalary += employee.getSalary().getFinalSalary();
        return true;
    }

    @DOpt(type = DOpt.Type.LinkUpdater)
    public boolean updateTotalCost(List<Employee> employees) {
        for (Employee employee : employees) {
            this.totalSalary += employee.getSalary().getFinalSalary();
        }
        return true;
    }

    @DOpt(type = DOpt.Type.LinkUpdater)
    public boolean updateTotalSalary(Salary salary) {
        this.totalSalary += salary.getFinalSalary();
        return true;
    }

    @DOpt(type = DOpt.Type.LinkUpdater)
    public void updateTotalSalary(List<Salary> salaries) {
        this.totalSalary = 0;
        for (Salary salary : salaries) {
            this.totalSalary += salary.getFinalSalary();
        }
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
