import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SimpleHttpServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new FileHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started at http://localhost:8000/");
    }

    static class FileHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String filePath = "C:/Users/pc/Desktop/jfsd1" + exchange.getRequestURI().getPath();
            if (filePath.equals("C:/Users/pc/Desktop/jfsd1/")) {
                filePath = "C:/Users/pc/Desktop/jfsd1/index.html"; // Serve index.html by default
            }

            try {
                if (filePath.endsWith("/")) {
                    filePath += "index.html"; // Default to index.html for directories
                }

                byte[] response = Files.readAllBytes(Paths.get(filePath));
                exchange.sendResponseHeaders(200, response.length);
                OutputStream os = exchange.getResponseBody();
                os.write(response);
                os.close();
            } catch (IOException e) {
                String response = "404 (Not Found)";
                exchange.sendResponseHeaders(404, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }
}
