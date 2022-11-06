package org.basecalculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(loadFXML());
        stage.setScene(scene);
        stage.show();
    }

    // Loads the app.fxml file. This contains a tabbed layout, which references each of the other layouts,
    // allowing them to be loaded
    private static Parent loadFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("app.fxml"));
        return fxmlLoader.load();
    }

    // Entry point into the application
    public static void main(String[] args) {
        launch();
    }
}