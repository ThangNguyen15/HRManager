package model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.MetaConstants;
import domainapp.basics.util.Tuple;

/**
 * @author baorv <roanvanbao@gmail.com>
 * @version 1.0.0
 */
public class Position {

    @DAttr(name = "id", id = true, auto = true, length = 3, mutable = false, optional = false, type = DAttr.Type.Integer)
    private int id;
    private static int idCounter;

    @DAttr(name = "name", type = DAttr.Type.String, length = 20, optional = false)
    private String name;

    @DAttr(name = "employee", type = DAttr.Type.Domain, serialisable = false)
    @DAssoc(ascName = "employee-has-position", role = "position",
            ascType = DAssoc.AssocType.One2Many, endType = DAssoc.AssocEndType.One,
            associate = @DAssoc.Associate(type = Employee.class, cardMin = 1, cardMax = MetaConstants.CARD_MORE))
    private Employee employee;

    /**
     * Constructor with only name
     *
     * @param name
     */
    public Position(String name) {
        this(null, name, null);
    }

    public Position(Integer id, String name) {
        this(id, name, null);
    }

    /**
     * Constructor with name, employee
     *
     * @param name
     * @param employee
     */
    public Position(String name, Employee employee) {
        this(null, name, employee);
    }

    /**
     * Constructor with full properties
     *
     * @param id
     * @param name
     * @param employee
     */
    public Position(Integer id, String name, Employee employee) {
        this.id = nextId(id);
        this.name = name;
        this.employee = employee;
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
            //TODO: update this for the correct attribute if there are more than one auto attributes of this class 
            int maxIdVal = (Integer) maxVal;
            if (maxIdVal > idCounter) {
                idCounter = maxIdVal;
            }
        }
    }


    /**
     * Get id of position
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * set id for position
     *
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Set name for position
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Get name of position
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Set employee in position
     *
     * @param employee
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * Get employee of position
     *
     * @return
     */
    public Employee getEmployee() {
        return employee;
    }

}