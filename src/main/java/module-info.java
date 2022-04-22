module site.edu {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires log4j;
    requires javafx.swing;

    opens site.edu to javafx.fxml;
    exports site.edu;
    exports Controllers;
    opens Controllers to javafx.fxml;
}