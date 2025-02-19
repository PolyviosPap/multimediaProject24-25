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
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.databind;

    opens polpapntua.multimediaproject2425 to javafx.fxml;
    exports polpapntua.multimediaproject2425;
    exports polpapntua.multimediaproject2425.models;
    exports polpapntua.multimediaproject2425.enums;
    exports polpapntua.multimediaproject2425.controllers;
    opens polpapntua.multimediaproject2425.controllers to javafx.fxml;
}