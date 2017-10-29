/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 *
 * @author user
 */
public class Threading implements Runnable {
  private static Socket clientSocket;
  private static final List<PrintWriter> listPrintWriters = new ArrayList();
  private static int id = 0;
  static ArrayList<String> names = new ArrayList();
  public Threading(Socket socket) {
    this.clientSocket = socket;
  }

  @Override
  public void run() {

    BufferedReader br;
    PrintWriter out;
    try {
      br = new BufferedReader(
          new InputStreamReader(clientSocket.getInputStream()));
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      listPrintWriters.add(out);
      out.println(id + "");
      String name = br.readLine();
      names.add(name);
      ChatServer.cssEditorFld2
          .setText(ChatServer.cssEditorFld2.getText() + "#" + name + "\n");
      id++;
      String inputLine;
      while ((inputLine = br.readLine()) != null) {
        for (PrintWriter listPrintWriter : listPrintWriters) {
          if (inputLine.contains("Disconnect")) {
            String str = inputLine.substring(10);
            int id2 = Integer.parseInt(str);
            try {
              listPrintWriters.remove(id2);
              names.remove(id2);
              inputLine = br.readLine();
              ChatServer.cssEditorFld2.clear();
              for (int i = 0; i < names.size(); i++) {
                ChatServer.cssEditorFld2
                    .setText(ChatServer.cssEditorFld2.getText() + "#"
                        + names.get(i) + "\n");
              }
            } catch (ConcurrentModificationException ec) {
            }
          } else {
            listPrintWriter.println(inputLine);
          }
        }
        ChatServer.cssEditorFld
            .setText(ChatServer.cssEditorFld.getText() + inputLine + "\n");
        ChatServer.cssEditorFld
            .selectPositionCaret(ChatServer.cssEditorFld.getLength());
        ChatServer.cssEditorFld.deselect();
      }
    } catch (IOException ex) {
    }
  }
}