package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) {
        try {
            ArrayList<User> users = new ArrayList<>();
            ServerSocket serverSocket = new ServerSocket(8178);
            System.out.println("Сервер запущен");
            while (true){
                Socket socket = serverSocket.accept(); // Ожидаем подключения клиента
                User currentUser = new User(socket);
                users.add(currentUser);
                System.out.println("Клиент подключился");
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            currentUser.getOut().writeUTF("Введите имя: ");// Спрашиваете имя клиента
                            String userName = currentUser.getIn().readUTF(); // Записываете его имя (куда?)
                            currentUser.setUserName(userName);
                            currentUser.getOut().writeUTF("Добро пожаловать на сервер");// Приветсвуете клиента на сервере

                            while (true){
                                String request = currentUser.getIn().readUTF(); //Ожидаем сообщение от клиента
                                System.out.println(userName+": "+request);
                                if(request.equals("/onlineUsers")){
                                    String usersName = "";
                                    for (User user: users) usersName += user.getUserName()+", ";
                                    currentUser.getOut().writeUTF(usersName);
                                }else
                                    for (User user: users) {
                                        if(currentUser.getSocket().equals(user.getSocket())) continue;
                                        user.getOut().writeUTF(userName+": "+request);
                                    }
                            }
                        } catch (IOException e) {
                            users.remove(currentUser);
                            System.out.println(currentUser.getUserName()+" покинул чат");
                            try {
                                for (User user: users)
                                    user.getOut().writeUTF(currentUser.getUserName()+" покинул чат");
                            }catch (IOException ex){
                                ex.printStackTrace();
                            }
                        }
                    }
                });
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
