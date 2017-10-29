/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class ChatServer extends Application {
  private static Socket clientSocket;
  static TextArea cssEditorFld;
  static TextArea cssEditorFld2;
  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Chat Server");
    Group root = new Group();
    Scene scene = new Scene(root, 700, 400, Color.WHITE);

    GridPane gridpane = new GridPane();
    gridpane.setPadding(new Insets(5));
    gridpane.setHgap(50);
    gridpane.setVgap(10);

    cssEditorFld = new TextArea();
    cssEditorFld.setPrefRowCount(10);
    cssEditorFld.setPrefColumnCount(100);
    cssEditorFld.setWrapText(true);
    cssEditorFld.setPrefWidth(450);
    cssEditorFld.setPrefHeight(300);
    cssEditorFld.setEditable(false);
    GridPane.setHalignment(cssEditorFld, HPos.CENTER);
    gridpane.add(cssEditorFld, 0, 1);

    cssEditorFld2 = new TextArea();
    cssEditorFld2.setPrefRowCount(10);
    cssEditorFld2.setPrefColumnCount(100);
    cssEditorFld2.setWrapText(true);
    cssEditorFld2.setPrefWidth(150);
    cssEditorFld2.setPrefHeight(300);
    cssEditorFld2.setEditable(false);
    GridPane.setHalignment(cssEditorFld, HPos.CENTER);
    gridpane.add(cssEditorFld2, 1, 1);
    cssEditorFld.setText("Server Start");

    root.getChildren().add(gridpane);
    primaryStage.setScene(scene);
    primaryStage.show();

    new Thread(new Runnable() {
      @Override
      public void run() {
        try (ServerSocket serverSocket = new ServerSocket(6010)) {
          while (true) {
            clientSocket = serverSocket.accept();
            Threading tes = new Threading(clientSocket);
            new Thread(tes).start();
          }
        } catch (IOException ex) {
        }
      }
    }).start();
  }

  /**
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

}
