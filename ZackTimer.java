import java.util.Timer;
import java.util.TimerTask;
import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import java.io.File;// Import the File class
import java.io.FileNotFoundException;// Import this class to handle errors
import java.util.Scanner;//Import the Scanner class to read text files

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class ZackTimer {

    private final Timer timer = new Timer();
    private final int minutes;
    private final String fichero;
    private static ArrayList<String> listaURL = new ArrayList<String>();

    public ZackTimer(int minutes, String ficheroArg) {
        this.minutes = minutes;
        this.fichero = ficheroArg;
    }

    public void openBrowser(String url) {
        Desktop desktop = java.awt.Desktop.getDesktop();
        try {
            URI oURL = new URI(url);
            desktop.browse(oURL);
        } catch (URISyntaxException | IOException e) {
            logConsola(" Error al abrir la url " + url);
            e.printStackTrace();
        }
    }

    public void readFile(String fileUrl) {
        try {
            File myObj = new File(fileUrl);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                this.listaURL.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            logConsola(" Error al leer ");
            e.printStackTrace();
        }
    }

    public void logConsola(String mensaje) {
        System.out.println(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()) + mensaje);
    }

    public void start() {
        timer.schedule(new TimerTask() {
            public void run() {
                toDoAction();
                timer.cancel();
            }

            private void toDoAction() {
                logConsola(" Han transcurrido " + minutes + " minutos");
                logConsola(" Abriendo fichero...");
                readFile(fichero);
                logConsola(" Abriendo navegador...");
                for(String url : listaURL) {
                    logConsola(" Abriendo - " + url);
                    openBrowser(url);
                }

            }
        }, minutes * 60 * 1000);
    }

    public static void main(String[] args) {
        int minutosParam = 0;
        String ficheroParam ="";
        if (args[0] != null) {
          try {
            minutosParam = Integer.parseInt(args[0]);
          } catch (NumberFormatException e) {
            minutosParam = 1;
          }
        };
        ficheroParam = args[1];

        System.out.println(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()) + " Temporizador activado a " + minutosParam + " minutos ...");
        System.out.println(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()) + " Fichero :" + ficheroParam);

        ZackTimer zTimer = new ZackTimer(minutosParam, ficheroParam);
        zTimer.start();
    }
}
