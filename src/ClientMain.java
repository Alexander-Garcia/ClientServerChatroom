/**
 * This is the client main class
 * It will instantiate a client object with the localhost string and
 * port number equal to 1 + last four of student id
 */
public class ClientMain {
    public static void main(String[] args) {
        Client client = new Client("localhost",14931);
        client.startClient();
    }
}
