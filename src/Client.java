import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
        public String localhost = "";
        public int port = 0;
        Socket client = null;
        DataInputStream input = null;
        DataOutputStream output = null;
        String inputFromUser ;
        String messageFromServer = "Did not receive input";

        public Client (String localhost, int port) {
            this.localhost = localhost;
            this.port = port;
        }

    /**
     * Method: startClient
     * @return void
     * Description: This is the client. It connects to the specified port (server) and then its main job is to
     * use the java scanner class and collect input from user.
     * That input is relayed to and handled by the server.
     * The client then collects information passed back by the server
     */
    public void startClient () {
            try {
                client = new Socket(localhost, port);
            } catch (IOException e) {
                System.out.println(e);
            }

            System.out.println("My chat room client. Version One.");

            while (true) {

                try {
                    input = new DataInputStream(client.getInputStream());

                } catch (IOException e) {
                    System.out.println(e);
                }


                try {
                    output = new DataOutputStream(client.getOutputStream());
                } catch (IOException e) {
                    System.out.println(e);
                }


                Scanner scanner = new Scanner(System.in);
                inputFromUser = scanner.nextLine();
                try {
                    output.writeUTF(inputFromUser);
                } catch (IOException ex) {
                    System.out.println(ex);
                }


                try {
                    messageFromServer = input.readUTF();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
                System.out.println(messageFromServer);

            }
        }
}
