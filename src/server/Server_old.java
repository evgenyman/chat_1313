package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;

public class Server {
    public static void main(String[] args) {
        try { // включили обработку Исключений
            ArrayList<Socket> userSocket = new ArrayList<>(); // Создали Коллекцию для хранения сокетов Клиентов
            ServerSocket serverSocket = new ServerSocket(8178); // Создали новый серверный сокет
            System.out.println("Сервер запущен");
            // Создаем бесконечный цикл ожидания подключения клиентов
            while (true){
                Socket socket = serverSocket.accept(); // Ожидаем подключение клиента
                userSocket.add(socket); // Как только Клиент подключился - добавили в коллекцию его Сокет
                System.out.println("Клиент подключился");
                // Создаем многозадачность
                Thread thread = new Thread(new Runnable() { // Создаем экземпляр Потока многозадачного режима
                    @Override
                    public void run() {
                        try {  // включили обработку Исключений
                            DataInputStream in = new DataInputStream(socket.getInputStream()); // Создали новый объект ВходящийДатаПоток
                            // DataOutputStream out = new DataOutputStream(socket.getOutputStream()); // Создали новый экземпляр ИсходящийДатаПоток
                            // ! Спращиваем имя клиента
                            DataOutputStream outQuestion = new DataOutputStream(socket.getOutputStream()); // Создали новый экземпляр ИсходящийДатаПоток
                            outQuestion.writeUTF("Введите Ваше имя: ");
                            // ! Записываем его имя (куда?)
                            String clientName = in.readUTF();
                            // ! Приветствуем клиента на сервере
                            outQuestion.writeUTF("Приветствуем Вас "+clientName);
                            while (true){
                                String request = in.readUTF(); // Создали переменную request и ожидаем, что пришлет нам Клиент (читаем в формате UTF) и записываем в request
                                // System.out.println("От клиента: "+request);
                                // out.writeUTF(request.toUpperCase()); // ! закомментировали ! Отправляем содержимое request в поток out, но преобразуя в БОЛЬШИЕ буквы
                                for (Socket socket1:userSocket) { // Перебор Коллекции по принципу "пока там что-то есть" - кладем из userSocket в socket1, выполняем инструкции ниже, затем возвращаемся и повторяем процедуру
                                    DataOutputStream out1 = new DataOutputStream(socket1.getOutputStream()); // Создали новый экземпляр ИсходящийДатаПоток
                                    out1.writeUTF(clientName+": "+request); // ! Сюда надо дописать имя клиента !
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                // // "Закрываем" описание Потока многозадачного режима

                /* Т.е. в итоге:
                * 1. мы запускаем сервер
                * 2. создаем бесконечный цикл ожидания подключения клиента
                * 3. в многозадачном режиме создаем потоки входа и выхода
                * 3.1. где бесконечно запускаем пррием и отправку сообщений
                * */

                thread.start(); // Стартовали экземпляр потока многозадачного режима
            }




        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
