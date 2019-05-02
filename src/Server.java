import java.io.*;
import java.net.*;
import java.util.HashMap;


public class Server {
    private int port;
    String currentUser = "";
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    String messageFromClient;
    DataOutputStream output = null;
    DataInputStream input = null;
    String command;

        public Server(int port){this.port = port;}

    /**
     * func: startServer()
     * @return void
     * desciption: this function is the heart of the server.
     * It initializes the sockets and waits for the client to start
     * once the client is running and they are connected the server begins to listen
     * for input from the client.
     * the input is split and the switch statement decides what command the user is doing
     * and then reacts accordingly.
     * Security with passwords not a concern they are stored in plain text file
     */
    public void startServer () {

                try {
                    serverSocket = new ServerSocket(port);
                } catch (IOException e) {
                    System.out.println(e);
                }


                try {
                    clientSocket = serverSocket.accept();
                    System.out.println("My chat room server. Version One.");
                } catch (IOException e) {
                    System.out.println(e);
                }


                while (true) {
                    try {
                        output = new DataOutputStream(clientSocket.getOutputStream());
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                    try {

                        input = new DataInputStream(clientSocket.getInputStream());
                        messageFromClient = input.readUTF();

                        String[] splitInput = messageFromClient.split(" ");

                        command = splitInput[0];


                        switch(command.toLowerCase()) {
                            case "login":
                                if (currentUser.equals("")){
                                    if (splitInput.length != 3) {
                                        output.writeUTF("Server: invalid number of arguments");
                                    } else {
                                        String username = splitInput[1];
                                        if (login(username) == true) {
                                            currentUser = username;
                                            System.out.println(currentUser + " login");
                                            output.writeUTF("Server: successful login");
                                        } else {
                                            output.writeUTF("Server: invalid User name or Password");
                                        }
                                    }
                                }else {
                                    output.writeUTF("Server: " + currentUser + " is already logged in. Logout and try again.");
                                }

                                break;
                            case "newuser":
                                if (currentUser.equals("")) {
                                    if (splitInput.length != 3) {
                                        output.writeUTF("Server: invalid number of arguments");
                                    } else {
                                        output.writeUTF(newuser(splitInput[1], splitInput[2]));
                                        System.out.println("New user added");
                                    }
                                }else {
                                    output.writeUTF("Server: " + currentUser + " is already logged in. Logout and try again");
                                }
                                break;
                            case "send":
                                StringBuilder sendMessage = new StringBuilder();
                                if (!"".equals(currentUser)) {
                                    for (int i = 1; i < splitInput.length; i++){
                                        sendMessage.append(splitInput[i] + " ");
                                    }
                                    output.writeUTF(currentUser + ": " + sendMessage );
                                    System.out.println(currentUser + ": " + sendMessage);
                                } else {
                                    output.writeUTF("Server: Denied. please login in first");
                                }
                                break;
                            case "logout":
                                System.out.println(currentUser + " logout");
                                output.writeUTF("Server: " + currentUser + " left");
                                currentUser = "";
                                break;
                            default:
                                output.writeUTF("Unrecognized command. Please try again");
                        }

                    } catch (Exception e) {
                        System.out.println(e);
                    }

                }
            }

    /**
     * @args none
     *
     * @return HashMap of <username, password>
     *     this method first reads from the input file and then adds users from that
     *     into the map.
     *     TODO: User should be its own class.
     *
     */
    private static HashMap<String, String> users() {
            HashMap<String, String> usersMap = new HashMap<>();
            String fileName = "resources/users.txt";
            String line ;
            String format;

            try {

                FileReader fileReader = new FileReader(fileName);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                while((line = bufferedReader.readLine()) != null) {
                    format = line.replaceAll(",", " " );
                    String[] split = format.split(" ");
                    usersMap.put(split[0], split[1]);

                }

                bufferedReader.close();
                fileReader.close();
            }
            catch(FileNotFoundException ex) {
                System.out.println("Unable to open file: " + fileName);
            }
            catch(IOException ex) {
                System.out.println("Error reading file: " + fileName);

            }

            return usersMap;
        }

    /**
     *
     * @param username
     * @return a boolean that determines if the username is in the map
     * This is self explanatory
     */
    private static boolean login(String username) {
        HashMap<String,String> usersMap = users();
        return usersMap.containsKey(username);
    }

    /**
     *
     * @param username
     * @param password
     * @return Message that either acknowledges success or failure for username
     * if successful then new user is added to the text file which loads the map
     */
        private static String newuser(String username, String password) {
            HashMap<String,String> userMap = users();
            String outputToClient = "";
            boolean usernameAvailable = true;


            for(String name : userMap.keySet()) {

                if (username.equalsIgnoreCase(name)) {
                    outputToClient = "Username already taken try again";
                    usernameAvailable = false;
                } else if (username.length() >= 32) {
                    outputToClient = "username is too long";
                    usernameAvailable = false;
                } else if (password.length() > 8 || password.length() < 4) {
                    outputToClient = "password needs to be between 4 and 8 characters";
                    usernameAvailable = false;
                }
            }

            if(usernameAvailable) {
                FileWriter fileWriter = null;
                BufferedWriter bufferedWriter = null;
                try {
                    String fileName = "resources/users.txt";
                    fileWriter = new FileWriter(fileName, true);
                    bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write("\n" + username + " " + password);


                } catch (IOException ex) {
                    System.out.println(ex);
                } finally {
                    try {
                        bufferedWriter.close();
                        fileWriter.close();
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                }
                outputToClient = "Successfully added new user. Please login to continue.";
            }
            return outputToClient;
        }

    }

