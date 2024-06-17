import game.KeyInput;
import game.MessageType;
import gameObjects.TankConstructorShell;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

    private static ServerSocket serverSocket;

    private static CopyOnWriteArrayList<Socket> clients;
    private static ConcurrentHashMap<Socket, ObjectOutputStream> clientOos;

    public static void main(String[] args) {
        clients = new CopyOnWriteArrayList<>();
        clientOos = new ConcurrentHashMap<>();

        try {
            serverSocket = new ServerSocket(8000);

            while (true) {
                Socket client = serverSocket.accept();

                clientOos.put(client, new ObjectOutputStream(client.getOutputStream()));
                clients.add(client);
                new Thread(() -> updateClient(client)).start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void updateClient(Socket client) {
        try {
            ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
            messageClientsExcept(client, MessageType.FETCH_TANK);

            while (client.isConnected()) {
//                System.out.println("Server receiving data from:\t" +client);
                MessageType messageType = (MessageType) ois.readObject();
                System.out.println(messageType);
                switch (messageType){
                    case TANK_INPUT:
                        KeyInput keyInput = (KeyInput) ois.readObject();
                        System.out.println(keyInput);
                        messageClientsExcept(client, messageType, keyInput);
                        break;
                    case NEW_TANK:
                        TankConstructorShell tankShell = (TankConstructorShell) ois.readObject();
                        messageClientsExcept(client, messageType, tankShell);
                        break;
                    case UPDATE_COLOR:
                        TankConstructorShell tankShell2 = (TankConstructorShell) ois.readObject();
                        messageClientsExcept(client, messageType, tankShell2);
                        break;
                }


            }

            clients.remove(client);
            client.close();
            clientOos.remove(client);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void messageClientsExcept(Socket excludedClient, MessageType messageType) throws IOException {
        messageClientsExcept(excludedClient, messageType, null);
    }

    private static void messageClientsExcept(Socket excludedClient, MessageType messageType, Object data) throws IOException {
        for (Socket socket : clients) {
            if(socket != excludedClient){
                ObjectOutputStream oos = clientOos.get(socket);
                oos.reset();
                oos.writeObject(messageType);
                if(data != null)
                    oos.writeObject(data);
                oos.flush();
            }
        }
    }
}
