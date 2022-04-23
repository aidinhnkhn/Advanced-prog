package logic;

import Savers.Saver;
import elements.people.Professor;
import javafx.scene.Node;
import javafx.stage.Stage;

public class ProfessorHomePageLogic {
    private static ProfessorHomePageLogic professorHomePageLogic;
    private ProfessorHomePageLogic(){

    }
    public static ProfessorHomePageLogic getInstance(){
        if (professorHomePageLogic==null)
            professorHomePageLogic=new ProfessorHomePageLogic();
        return professorHomePageLogic;
    }
    public void exit(Node node){
        Saver.getInstance().saveProfessor((Professor) (LogicalAgent.getInstance().getUser()));
        Saver.getInstance().saveChanges();
        Stage stage=(Stage) (node.getScene().getWindow());
        stage.close();
    }
}
