package logic;

import server.Savers.Saver;
import elements.people.Professor;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ProfessorHomePageLogic {
    private static ProfessorHomePageLogic professorHomePageLogic;
    private static Logger log = LogManager.getLogger(ProfessorHomePageLogic.class);
    private ProfessorHomePageLogic(){

    }
    public static ProfessorHomePageLogic getInstance(){
        if (professorHomePageLogic==null)
            professorHomePageLogic=new ProfessorHomePageLogic();
        return professorHomePageLogic;
    }
    public void exit(Node node){
        log.info("user singed out.");
        //TODO: send the exit request to Server
        Stage stage=(Stage) (node.getScene().getWindow());
        stage.close();
    }
}
