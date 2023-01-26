package fileServeur;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5555);
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        String fileName = "example.txt";
        writeLine(out, fileName);
        String response = readLine(in);

        if (response.startsWith("EXISTS")) {
            long fileSize = Long.parseLong(response.substring(7));
            System.out.println("File size: " + fileSize);
            FileOutputStream fos = new FileOutputStream("received_" + fileName);
            byte[] buffer = new byte[4096];
            int read;
            while (fileSize > 0 && (read = in.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) != -1) {
                fos.write(buffer, 0, read);
                fileSize -= read;
            }
            fos.close();
        } else {
            System.out.println("Error: " + response);
        }
        in.close();
        out.close();
        socket.close();
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

