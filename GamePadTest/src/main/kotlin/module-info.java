module com.example.gamepadtest {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires org.controlsfx.controls;
    requires jinput;
    requires java.logging;
    requires kotlinx.coroutines.core.jvm;

    opens com.example.gamepadtest to javafx.fxml;
    exports com.example.gamepadtest;
}