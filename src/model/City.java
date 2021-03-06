package model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.*;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.util.Tuple;

/**
 * A domain class whose objects are city names. This class is used as
 * the <code>allowedValues</code> of the domain attributes of
 * other domain classes (e.g. Worker.address).
 *
 * <p>Method <code>toString</code> overrides <code>Object.toString</code> to
 * return the string representation of a city name which is expected by
 * the application.
 *
 * @author baorv <roanvanbao@gmail.com>
 *
 */
public class City {

    @DAttr(name="id",id=true,auto=true,length=3,mutable=false,optional=false,type=Type.Integer)
    private int id;
    private static int idCounter;

    @DAttr(name="name",type=Type.String,length=20,optional=false)
    private String name;

    @DAttr(name="worker",type=Type.Domain,optional=true,serialisable=false)
    @DAssoc(ascName="worker-has-city",role="city", ascType=AssocType.One2Many, endType=AssocEndType.One,
            associate=@Associate(type=Worker.class,cardMin=1,cardMax= MetaConstants.CARD_MORE))
    private Worker worker;

    // from object form: Worker is not included
    public City(String cityName) {
        this(null, cityName, null);
    }

    // from object form: Worker is included
    public City(String cityName, Worker worker) {
        this(null, cityName, worker);
    }

    // from data source
    public City(Integer id, String cityName) {
        this(id, cityName, null);
    }

    // based constructor (used by others)
    private City(Integer id, String cityName, Worker worker) {
        this.id = nextId(id);
        this.name = cityName;
        this.worker = worker;
    }

    private static int nextId(Integer currID) {
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
     * @requires
     *  minVal != null /\ maxVal != null
     * @effects
     *  update the auto-generated value of attribute <tt>attrib</tt>, specified for <tt>derivingValue</tt>, using <tt>minVal, maxVal</tt>
     */
    @DOpt(type=DOpt.Type.AutoAttributeValueSynchroniser)
    public static void updateAutoGeneratedValue(
            DAttr attrib,
            Tuple derivingValue,
            Object minVal,
            Object maxVal) throws ConstraintViolationException {

        if (minVal != null && maxVal != null) {
            //TODO: update this for the correct attribute if there are more than one auto attributes of this class
            int maxIdVal = (Integer) maxVal;
            if (maxIdVal > idCounter)
                idCounter = maxIdVal;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Worker getWorker() {
        return worker;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public void setNewWorker(Worker worker) {
        this.worker = worker;
        // do other updates here (if needed)
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public String toString() {
        return name;
    }
}

