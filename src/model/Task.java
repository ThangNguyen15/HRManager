package model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.MetaConstants;
import domainapp.basics.util.Tuple;

/**
 * 
 */
public class Task {

   @DAttr(name = "id", id = true, auto = true, length = 3, mutable = false, optional = false, type = DAttr.Type.Integer)
    private int id;
    private static int idCounter;

    @DAttr(name = "name", type = DAttr.Type.String, optional = false)
    private String name;
    
    @DAttr(name = "content", type = DAttr.Type.String, optional = false)
    private String content;
    
    @DAttr(name = "worker", type = DAttr.Type.Domain, serialisable = false)
    @DAssoc(ascName = "worker-has-task", role = "task",
            ascType = DAssoc.AssocType.Many2Many, endType = DAssoc.AssocEndType.Many,
            associate = @DAssoc.Associate(type = Worker.class, cardMin = 1, cardMax = MetaConstants.CARD_MORE))
    private Worker worker;

    @DAttr(name = "startDate", type = DAttr.Type.String, optional = false)
    private String startDate;
    
    @DAttr(name = "completeDate", type = DAttr.Type.String, optional = false)
    private String completeDate;

    /**
     * Constructor with full params
     * 
     * @param id
     * @param name
     * @param content
     * @param worker
     * @param startDate
     * @param completeDate 
     */
    public Task(Integer id, String name, String content, Worker worker, String startDate, String completeDate){
        this.id = nextId(id);
        this.name = name;
        this.content = content;
        this.worker = worker;
        this.startDate = startDate;
        this.completeDate = completeDate;
    }

    /**
     * Constructor with omitting params
     * @param name
     * @param content
     * @param worker
     * @param startDate
     * @param completeDate 
     */
    public Task(String name, String content, Worker worker, String startDate, String completeDate){
        this(null, name, content, worker, startDate, completeDate);
    }
    /**
     * Set name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set content
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Set worker
     * @param worker
     */
    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    /**
     * Set start Date
     * @param startDate 
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Set complete date
     * @param completeDate 
     */
    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    /**
     * Get name
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Get content
     * @return String
     */
    public String getContent() {
        return content;
    }

    /**
     * Get worker
     * @return Worker
     */
    public Worker getWorker() {
        return worker;
    }

    /**
     * Get start date
     * @return String
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Get complete date
     * @return String
     */
    public String getCompleteDate() {
        return completeDate;
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
     * Generate id for city
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

}