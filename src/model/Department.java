package model;

import java.util.ArrayList;
import java.util.List;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.*;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.util.Tuple;

/**
 * Represents a employee department.
 *
 * @author dmle
 */

public class Department {
    @DAttr(name = "id", id = true, auto = true, length = 6, mutable = false, type = Type.Integer)
    private int id;
    private static int idCounter;

    @DAttr(name = "name", length = 20, type = Type.String)
    private String name;

    @DAttr(name = "employees", type = Type.Collection,
            serialisable = false, optional = false,
            filter = @Select(clazz = Employee.class))
    @DAssoc(ascName = "employee-has-department", role = "department",
            ascType = AssocType.One2Many, endType = AssocEndType.One,
            associate = @Associate(type = Employee.class,
                    cardMin = 1, cardMax = MetaConstants.CARD_MORE))
    private List<Employee> employees;

    // derived attributes
    private int employeesCount;

    public Department(String name) {
        this(null, name);
    }

    // constructor to create objects from data source
    public Department(Integer id, String name) {
        this.id = nextID(id);
        this.name = name;

        employees = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;

        employeesCount = employees.size();
    }

    public Integer getId() {
        return id;
    }

    @DOpt(type = DOpt.Type.LinkAdder)
    //only need to do this for reflexive association: @MemberRef(name="students")
    public boolean addEmployee(Employee employee) {
        if (!this.employees.contains(employee)) {
            employees.add(employee);
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewEmployee(Employee employee) {
        employees.add(employee);
        employeesCount++;

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkAdder)
    public boolean addEmployee(List<Employee> employees) {
        for (Employee employee : employees) {
            if (!this.employees.contains(employee)) {
                this.employees.add(employee);
            }
        }

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkAdderNew)
    public boolean addNewEmployee(List<Employee> students) {
        this.employees.addAll(students);
        employeesCount += students.size();

        // no other attributes changed
        return false;
    }

    @DOpt(type = DOpt.Type.LinkRemover)
    //only need to do this for reflexive association: @MemberRef(name="workers")
    public boolean removeEmployee(Employee employee) {
        boolean removed = employees.remove(employee);

        if (removed) {
            employeesCount--;
        }

        // no other attributes changed
        return false;
    }

    /**
     * @effects return <tt>employeesCount</tt>
     */
    @DOpt(type = DOpt.Type.LinkCountGetter)
    public Integer getEmployeesCount() {
        return employeesCount;
    }

    @DOpt(type = DOpt.Type.LinkCountSetter)
    public void setEmployeesCount(Integer count) {
        employeesCount = count;
    }

    public String toString() {
        return "Department(" + getId() + "," + getName() + ")";
    }

    private static Integer nextID(Integer currID) {
        if (currID == null) {
            idCounter++;
            return idCounter;
        } else {
            int num = currID.intValue();
            if (num > idCounter)
                idCounter = num;

            return currID;
        }
    }

    /**
     * @requires minVal != null /\ maxVal != null
     * @effects update the auto-generated value of attribute <tt>attrib</tt>, specified for <tt>derivingValue</tt>, using <tt>minVal, maxVal</tt>
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