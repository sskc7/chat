/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class ChatClient extends Application {
  int id;
  Socket clientSocket;
  String username = "Anonymous";
  BufferedReader br;
  PrintWriter out;
  private String text = "";
  @Override
  public void start(Stage primaryStage)
      throws UnknownHostException, IOException {
    primaryStage.setTitle("Gadu Client");
    Group root = new Group();
    Scene scene = new Scene(root, 500, 500, Color.WHITE);

    GridPane gridpane = new GridPane();
    gridpane.setPadding(new Insets(5));
    gridpane.setHgap(10);
    gridpane.setVgap(10);

    final TextArea cssEditorFld = new TextArea();
    cssEditorFld.setPrefRowCount(10);
    cssEditorFld.setPrefColumnCount(100);
    cssEditorFld.setWrapText(true);
    cssEditorFld.setPrefWidth(450);
    cssEditorFld.setPrefHeight(300);
    cssEditorFld.setEditable(false);

    GridPane.setHalignment(cssEditorFld, HPos.CENTER);
    gridpane.add(cssEditorFld, 0, 1);

    final TextArea cssEditorFld2 = new TextArea();
    cssEditorFld2.setPrefRowCount(10);
    cssEditorFld2.setPrefColumnCount(100);
    cssEditorFld2.setWrapText(false);
    cssEditorFld2.setPrefWidth(450);
    cssEditorFld2.setPrefHeight(80);
    int currentCaretPosition = cssEditorFld2.getCaretPosition();
    System.out.println("Position " + currentCaretPosition);
    GridPane.setHalignment(cssEditorFld2, HPos.CENTER);
    gridpane.add(cssEditorFld2, 0, 2);

    Button btn = new Button();
    btn.setText("Send");

    gridpane.add(btn, 0, 3);

    Button btn2 = new Button();
    btn2.setText("Disconnect");
    gridpane.add(btn2, 0, 4);
    root.getChildren().add(gridpane);
    primaryStage.setScene(scene);
    primaryStage.show();
    TextInputDialog dialog = new TextInputDialog("");
    dialog.setTitle("Imie");
    dialog.setHeaderText("Podaj swoje imie");
    Optional<String> result = dialog.showAndWait();
    String entered = "none.";
    if (result.isPresent()) {
      username = result.get();
    }

    // Server Construction
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          boolean scanning = true;
          InetAddress addr = InetAddress.getLocalHost();
          // InetAddress addr = InetAddress.getByName("192.168.1.105");
          if (clientSocket == null) {
            while (scanning) {
              try {
                clientSocket = new Socket(addr, 6010);
                scanning = false;
              } catch (Exception e) {
                Thread.sleep(2000);
              }
            }
          }
          if (br == null) {
            br = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
            id = Integer.parseInt(br.readLine());
          }
          if (out == null) {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
          }
          out.println(username);
          out.println("Połączył się  " + username);
        } catch (IOException ex) {
        } catch (InterruptedException ex) {
        }
      }
    }).start();

    // Sending2
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {

          btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              out.println("Disconnect" + id);
              out.println("Rozłączył się " + username);
              out = null;
            }
          });
          btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              event.consume();
              String msgToClient = cssEditorFld2.getText();
              cssEditorFld2.setText("");
              if (out == null)
                System.out.println("No connection");
              else {
                out.println("#" + username + " " + msgToClient);
              }
            }
          });
          cssEditorFld2.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
              if (keyEvent.getCode() == KeyCode.ENTER) {
                btn.fire();
                keyEvent.consume();
              }
            }
          });
        } catch (Exception e) {
        }
      }
    }).start();

    new Thread(new Runnable() {

      @Override
      public void run() {
        try {
          while (true) {
            Thread.sleep(20);
            try {
              String response;
              if (br == null)
                response = "null";
              else {
                response = br.readLine();
                cssEditorFld.setText(cssEditorFld.getText() + response + "\n");
                cssEditorFld.selectPositionCaret(cssEditorFld.getLength());
                cssEditorFld.deselect();
              }
            } catch (Exception e) {
            }
          }
        } catch (Exception e) {
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
