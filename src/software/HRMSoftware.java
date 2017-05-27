package software;

import model.*;
import domainapp.basics.apps.tool.DomainAppTool;
import domainapp.basics.exceptions.ApplicationRuntimeException;
import domainapp.basics.setup.Cmd;


/**
 * @overview
 *  Encapsulate the basic functions for setting up and running a software for the Human Resource Manager domain model.
 *
 * @author dmle
 *
 * @version
 */
public class HRMSoftware {

    // HRManager's domain model
    private static final Class[] model = {
            Worker.class,
            Employee.class,
            Intern.class,
            Attendance.class,
            City.class,
            Department.class,
            PerformanceReport.class,
            Position.class,
            Question.class,
            Salary.class,
            SalaryReport.class,
            Task.class
    };

    public static void main(String[] args) {
        // 1: configure and run software
        //configure();

        // 2: run software
        run();

        //deleteDomainData(Student.class, City.class);

        //deleteDomainSchema(Enrolment.class);

        //createDomainSchema(Enrolment.class, Student.class, City.class, SClass.class);

        //deleteApplicationConfig();
    }

    /**
     * @effects
     *  configure and run the software.
     *
     *  <p>Throws ApplicationRuntimeException if an error occured.
     *
     * @version
     */
    private static void configure() throws ApplicationRuntimeException {
        DomainAppTool.runSetUp(Cmd.Configure, model);
    }

    /**
     * @effects
     *  Run the software.
     *
     *  <p>Throws ApplicationRuntimeException if an error occured.
     *
     * @version
     *
     */
    private static void run() throws ApplicationRuntimeException {
        DomainAppTool.run(model);
    }

    ////// Other set-up commands ///////
    /**
     * @effects
     *  Delete domain data of the domain classes specified in <tt>domainClasses</tt>.
     *
     *  <p>Throws ApplicationRuntimeException if an error occured.
     *
     * @version
     */
    private static void deleteDomainData(Class...domainClasses) throws ApplicationRuntimeException {
        if (domainClasses == null || domainClasses.length == 0)
            return;

        DomainAppTool.runSetUp(Cmd.DeleteDomainData, domainClasses);
    }

    /**
     * @effects
     *  Delete domain schema of the domain classes specified in <tt>domainClasses</tt>.
     *
     *  <p>Throws ApplicationRuntimeException if an error occured.
     *
     * @version
     */
    private static void deleteDomainSchema(Class...domainClasses) throws ApplicationRuntimeException {
        if (domainClasses == null || domainClasses.length == 0)
            return;

        DomainAppTool.runSetUp(Cmd.DeleteDomainSchema, domainClasses);
    }

    /**
     * @effects
     *  Create domain schema of the domain classes specified in <tt>domainClasses</tt>.
     *
     *  <p>Throws ApplicationRuntimeException if an error occured.
     *
     * @version
     */
    private static void createDomainSchema(Class...domainClasses) throws ApplicationRuntimeException {
        if (domainClasses == null || domainClasses.length == 0)
            return;

        DomainAppTool.runSetUp(Cmd.CreateDomainSchema, domainClasses);
    }

    /**
     * @effects
     *  Delete application configuration
     *
     *  <p>Throws ApplicationRuntimeException if an error occured.
     *
     * @version
     */
    private static void deleteApplicationConfig() throws ApplicationRuntimeException {
        DomainAppTool.runSetUp(Cmd.DeleteConfig);
    }
}