module jp.dkosilver.jsontreeview {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;

    opens jp.dkosilver.json.fx to javafx.fxml, com.fasterxml.jackson.databind;
    exports jp.dkosilver.json.fx;
}