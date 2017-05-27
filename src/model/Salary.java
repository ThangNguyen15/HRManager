package model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.*;
import domainapp.basics.util.Tuple;

/**
 *
 */
public class Salary {

    private static int idCounter;

    @DAttr(name = "id", id = true, auto = true, length = 3, mutable = false, optional = false, type = DAttr.Type.Integer)
    private int id;

    @DAttr(name = "employee", type = DAttr.Type.Domain, optional = false, mutable = true)
    @DAssoc(ascName = "employee-has-salary", role = "salary",  ascType = DAssoc.AssocType.Many2Many, endType = DAssoc.AssocEndType.Many,
            associate = @DAssoc.Associate(type = Employee.class, cardMin = 1, cardMax = MetaConstants.CARD_MORE))
    private Employee employee;

    @DAttr(name = "salaryReport", type = DAttr.Type.Domain, length = 20, optional = true)
    @DAssoc(ascName = "salaryReport-has-salary", role = "salary",
            ascType = DAssoc.AssocType.One2Many, endType = DAssoc.AssocEndType.Many,
            associate = @DAssoc.Associate(type = SalaryReport.class, cardMin = 1, cardMax = 1))
    private SalaryReport salaryReport;

    @DAttr(name = "baseSalary", type = DAttr.Type.Integer, mutable = false, optional = true, serialisable = false)
    private Integer baseSalary;

    @DAttr(name = "finalSalary", type = DAttr.Type.Integer, mutable = false, optional = true, serialisable = false)
    private Integer finalSalary;

    //Constructor for loading
    public Salary(Integer id, Employee employee, SalaryReport salaryReport, Integer baseSalary, Integer finalSalary) {
        this.id = nextID(id);
        this.employee = employee;
        this.salaryReport = salaryReport;
        this.baseSalary = baseSalary;
        this.finalSalary = finalSalary;
    }

    public Salary(Employee employee, SalaryReport salaryReport, Integer baseSalary, Integer finalSalary) {
        this(null, employee, salaryReport, baseSalary, finalSalary);
    }

    public Salary(Employee employee) {
        this(null, employee, null, null, null);
    }

    public int getId() {
        return id;
    }

    public Integer getBaseSalary() {
        if (employee.getPosition().getName().equals("Director")) {
            baseSalary = 80;
        }
        else if (employee.getPosition().getName().equals("Head of Deparment")) {
            baseSalary = 65;
        }
        else if (employee.getPosition().getName().equals("Team Leader")) {
            baseSalary = 50;
        }
        else if (employee.getPosition().getName().equals("Officer")) {
            baseSalary = 35;
        }
        return baseSalary;
    }

    public Employee getEmployee() {
        return employee;
    }

    public SalaryReport getSalaryReport() {
        return salaryReport;
    }

    @DOpt(type = DOpt.Type.LinkUpdater)
//    @AttrRef(name = "finalSalary")
    public Integer getFinalSalary() {
        finalSalary = getBaseSalary() * (30 - employee.getAttendance().getDayAbsence());
        return finalSalary;
    }

    //Auto Generate ID Section

    /**
     * @return id counter
     */
    public static int getIdCounter() {
        return idCounter;
    }

    /**
     * Get next identification
     *
     * @param currID
     * @return next id
     */
    protected static int nextID(Integer currID) {
        if (currID == null) { // generate one
            idCounter++;
            return idCounter;
        } else { // update
            int num;
            num = currID.intValue();

            // if (num <= idCounter) {
            // throw new
            // ConstraintViolationException(ConstraintViolationException.Code.INVALID_VALUE,
            // "Lỗi giá trị thuộc tính ID: {0}", num + "<=" + idCounter);
            // }

            if (num > idCounter) {
                idCounter = num;
            }
            return currID;
        }
    }

    /**
     * Update auto generate
     *
     * @param attrib
     * @param derivingValue
     * @param minVal
     * @param maxVal
     * @throws ConstraintViolationException
     */
    @DOpt(type = DOpt.Type.AutoAttributeValueSynchroniser)
    public static void updateAutoGeneratedValue(
            DAttr attrib,
            Tuple derivingValue,
            Object minVal,
            Object maxVal) throws ConstraintViolationException {

        if (minVal != null && maxVal != null) {
            //TODO: update this for the correct attribute if there are more than one auto attributes of this class
            if (attrib.name().equals("id")) {
                int maxIdVal = (Integer) maxVal;
                if (maxIdVal > idCounter)
                    idCounter = maxIdVal;
            } else if (attrib.name().equals("totalBonus")) {
                //Ignore
            }
        }
    }

}
