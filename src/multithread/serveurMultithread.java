package multithread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class serveurMultithread {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		        try {
		            ServerSocket serverSocket = new ServerSocket(5000);

		            // Use a thread pool to handle multiple clients simultaneously
		            Executor executor = Executors.newFixedThreadPool(10);

		            while (true) {
		                Socket clientSocket = serverSocket.accept();
		                Runnable clientHandler = new ClientHandler(clientSocket);
		                executor.execute(clientHandler);
		                
//		                Scanner sc = null;
//						String userInput = sc.nextLine();
//		                if(userInput.equals("exit")){
//		                    serverSocket.close();
//		                    ((ExecutorService) executor).shutdown();
//		                    System.out.println("Server disconnected successfully");
//		                    break;
//		                }
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		}

		class ClientHandler implements Runnable {

		    private Socket clientSocket;
		    private BufferedReader in;
		    private PrintWriter out;
		    private Scanner sc = new Scanner(System.in);

		    public ClientHandler(Socket clientSocket) {
		        this.clientSocket = clientSocket;
		    }

		    @Override
		    public void run() {
		        try {
		            out = new PrintWriter(clientSocket.getOutputStream());
		            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		            Thread envoi = new Thread(new Runnable() {
		                String msg;

		                @Override
		                public void run() {
		                    while (true) {
		                        msg = sc.nextLine();
		                        out.println(msg);
		                        out.flush();
		                    }
		                }
		            });
		            envoi.start();

		            Thread recevoir = new Thread(new Runnable() {
		                String msg;

		                @Override
		                public void run() {
		                    try {
		                        msg = in.readLine();
		                        //tant que le client est connecté
		                        while (msg != null) {
		                            System.out.println("Client : " + msg);
		                            msg = in.readLine();
		                        }
		                        //sortir de la boucle si le client a déconnecté
		                        System.out.println("Client déconnecté");
		                        //fermer le flux et la session socket
		                        out.close();
		                        in.close();
		                        clientSocket.close();
		                    } catch (IOException e) {
		                        e.printStackTrace();
		                    }
		                }
		            });
		            recevoir.start();

		        } catch (IOException e) {
		            e.printStackTrace();
		        }

	}

}
