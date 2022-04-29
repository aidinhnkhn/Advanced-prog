package logic;


import Controllers.SceneLoader;
import Savers.Saver;
import elements.people.Student;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class StudentHomePageLogic {
    private static StudentHomePageLogic studentHomePageLogic;
    private static Logger log = LogManager.getLogger(StudentHomePageLogic.class);
    private StudentHomePageLogic(){

    }
    public static StudentHomePageLogic getInstance(){
        if (studentHomePageLogic==null)
            studentHomePageLogic=new StudentHomePageLogic();
        return studentHomePageLogic;
    }
    public void exit(Node node){
        log.info("user singed out.");
        Saver.getInstance().saveStudent((Student)(LogicalAgent.getInstance().getUser()));
        Saver.getInstance().saveChanges();
        Stage stage=(Stage) (node.getScene().getWindow());
        stage.close();
    }
}
