module com.uniquindio.proyectop2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.uniquindio.proyectop2 to javafx.fxml;
    exports com.uniquindio.proyectop2;
}