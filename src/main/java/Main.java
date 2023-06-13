import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static final int PORT = 8989;
    private static final String PDF = "pdfs";

    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File(PDF));
        try (ServerSocket serverSocket = new ServerSocket(PORT);) {
            System.out.println("Сервер подключен к порту " + PORT);
            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream());
                ) {

                    String word = in.readLine();
                    String response = getGson(engine, word);
                    out.write(response);
                    System.out.println(response);
                }
            }
        } catch (IOException e) {
            System.out.println("Сервер не подключен");
            e.printStackTrace();
        }
    }

    private static String getGson(BooleanSearchEngine engine, String word) {
        System.out.printf("Запрос пользователя: %s\n", word);
        var gson = new GsonBuilder().setPrettyPrinting().create();
        var checkSearch = engine.search(word);
        var response = gson.toJson(checkSearch.isEmpty() ? "Слово не найдено" : checkSearch);
        return response;
    }
}