module com.company.filehandlingapp {
    requires java.naming;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires java.persistence;

    opens com.company.filehandlingapp to javafx.fxml;
    opens com.company.filehandlingapp.model.item to org.hibernate.orm.core, javafx.base;
    exports com.company.filehandlingapp;
}