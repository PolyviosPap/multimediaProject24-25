module polpapntua.multimediaproject2425 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires org.apache.logging.log4j;

    opens polpapntua.multimediaproject2425 to javafx.fxml;
    exports polpapntua.multimediaproject2425;
    exports polpapntua.multimediaproject2425.models;
    exports polpapntua.multimediaproject2425.enums;
    exports polpapntua.multimediaproject2425.controllers;
    opens polpapntua.multimediaproject2425.controllers to javafx.fxml;
}