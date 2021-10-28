package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in); // Создаем новый Сканер (ин) для считывания с консоли
            Socket socket = new Socket("127.0.0.1",8178); // Создаем новый (клиентский0 сокет
            System.out.println("Успешно подключен");
            DataOutputStream out = new DataOutputStream(socket.getOutputStream()); // Создали новый объект ИсходящийДатаПоток
            DataInputStream in = new DataInputStream(socket.getInputStream());
            // Создаем многозадачный поток
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) { // Запускаем бесконечный цикл ожидания от Сервера сообщений
                            String response = in.readUTF(); // Создали переменную response куда будем записывать то, что присылает нам Сервер (читаем в формате UTF
                            System.out.println("Ответ от сервера: " + response);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
               thread.start(); // Стратуем многозадачный поток
            while (true){ // Запускаем бесконечное ожидание ввода сообщения с консоли и его отправку на Сервер
                String text = scanner.nextLine(); // Записываем в переменную text строку, что считали с консоли
                out.writeUTF(text); // Отправляем содержимое text в поток out
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
