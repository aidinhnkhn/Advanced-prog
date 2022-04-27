module site.edu {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.swing;
    requires com.google.gson;
    requires com.google.common;

    opens site.edu to javafx.fxml;
    exports site.edu;
    exports Controllers;
    opens Controllers to javafx.fxml;
    opens elements.people to com.google.gson, javafx.base;
    opens elements.courses to com.google.gson, javafx.base;
    opens elements.university to com.google.gson;
    opens elements.request to com.google.gson;
}