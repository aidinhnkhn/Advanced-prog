package logic;


import Controllers.SceneLoader;
import Savers.Saver;
import elements.people.Student;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;


public class StudentHomePageLogic {
    private static StudentHomePageLogic studentHomePageLogic;
    private StudentHomePageLogic(){

    }
    public static StudentHomePageLogic getInstance(){
        if (studentHomePageLogic==null)
            studentHomePageLogic=new StudentHomePageLogic();
        return studentHomePageLogic;
    }
    public void exit(Node node){
        Saver.getInstance().saveStudent((Student)(LogicalAgent.getInstance().getUser()));
        Saver.getInstance().saveChanges();
        Stage stage=(Stage) (node.getScene().getWindow());
        stage.close();
    }
}
