/**
 * Name: Alexander Garcia
 * Paw: adgbq2
 * Date: 5/1/19
 * Description: This program utilizes an MVC type pattern with Server and Client
 * TO RUN: the Server must call its main first followed by the Client
 * Messages are to be typed into the client console
 * both server and client have output to the console.
 */
public class ServerMain {
    public static void main(String[] args) {
        Server server = new Server(14931);
        server.startServer();
    }
}
