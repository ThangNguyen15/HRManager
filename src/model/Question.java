package model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.util.Tuple;

/**
 * 
 */
public class Question {

    @DAttr(name = "id", id = true, auto = true, length = 3, mutable = false, optional = false, type = DAttr.Type.Integer)
    private int id;
    private static int idCounter;

    @DAttr(name = "name", type = DAttr.Type.String, optional = false)
    private String name;
    
    @DAttr(name = "content", type = DAttr.Type.String, optional = false)
    private String content;

    @DAttr(name = "answer", type = DAttr.Type.String, optional = false)
    private String answer;


    /**
     * Constructor with full params
     * 
     * @param id
     * @param name
     * @param content
     * @param answer 
     */
    public Question(Integer id, String name, String content, String answer) {
        this.id = nextId(id);
        this.name = name;
        this.content = content;
        this.answer = answer;
    }

    /**
     * Constructor with three params
     * 
     * @param name
     * @param content
     * @param answer 
     */
    public Question(String name, String content, String answer){
        this(null, name, content, answer);
    }
    /**
     * Set name
     * @param name 
     */
    public void setName(String name){
        this.name = name;
    }
    
    /**
     * Get name
     * @return 
     */
    public String getName(){
        return this.name;
    }
    
    /**
     * Set content 
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Set answer
     * @param answer 
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * Get id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Get content
     * @return String
     */
    public String getContent() {
        return content;
    }

    /**
     * Get answer
     * @return String
     */
    public String getAnswer() {
        return answer;
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
     * Generate id for Question
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