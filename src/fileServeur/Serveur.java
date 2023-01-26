package fileServeur;

import java.io.*;
import java.net.*;

public class Serveur {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5555);
        while (true) {
            Socket socket = serverSocket.accept();
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            String fileName = readLine(in);
            File file = new File(fileName);

            if (file.exists() && !file.isDirectory()) {
                writeLine(out, "EXISTS " + file.length());
                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                int read;
                while ((read = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                fis.close();
            } else {
                writeLine(out, "ERROR");
            }
            out.flush();
            out.close();
            socket.close();
        }
    }

    public static String readLine(InputStream in) throws IOException {
        int ch;
        StringBuilder sb = new StringBuilder();
        while ((ch = in.read()) != '\n') {
            sb.append((char) ch);
        }
        return sb.toString();
    }

    public static void writeLine(OutputStream out, String str) throws IOException {
        out.write((str + "\n").getBytes());
    }
}
