package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StartupPage extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    // TODO Auto-generated method stub
    AnchorPane root = new AnchorPane();
    Scene scene = new Scene(root, 1022, 768);
    primaryStage.setTitle("Chaos game");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
