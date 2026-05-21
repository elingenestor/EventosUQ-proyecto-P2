module com.uniquindio.proyectop2 {
        requires javafx.controls;
        requires javafx.fxml;
        requires java.sql;

        requires com.h2database;
        requires mysql.connector.j;

        requires org.apache.poi.ooxml;
        requires com.opencsv;
        requires org.apache.pdfbox;

        opens com.uniquindio.proyectop2 to javafx.fxml;

        opens com.uniquindio.proyectop2.Model to javafx.base, javafx.fxml;
        opens com.uniquindio.proyectop2.Enums to javafx.base;

        // Permisos de reflexión para controladores válidos
        opens com.uniquindio.proyectop2.controladores.autenticacion to javafx.fxml;
        opens com.uniquindio.proyectop2.controladores.principal to javafx.fxml;
        opens com.uniquindio.proyectop2.controladores.eventos to javafx.fxml;
        opens com.uniquindio.proyectop2.controladores.compras to javafx.fxml;
        opens com.uniquindio.proyectop2.controladores.usuario to javafx.fxml;
        opens com.uniquindio.proyectop2.controladores.administrador to javafx.fxml;

        // Damos acceso a la nueva carpeta util donde está SesionActual
        opens com.uniquindio.proyectop2.util to javafx.fxml;

        exports com.uniquindio.proyectop2;
        exports com.uniquindio.proyectop2.Model;
        exports com.uniquindio.proyectop2.Enums;

        // Exportación de módulos para visibilidad del compilador
        exports com.uniquindio.proyectop2.controladores.autenticacion;
        exports com.uniquindio.proyectop2.controladores.principal;
        exports com.uniquindio.proyectop2.controladores.eventos;
        exports com.uniquindio.proyectop2.controladores.compras;
        exports com.uniquindio.proyectop2.controladores.usuario;
        exports com.uniquindio.proyectop2.controladores.administrador;
        exports com.uniquindio.proyectop2.util;
}
