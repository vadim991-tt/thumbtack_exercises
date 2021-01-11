package net.thumbtack.school.asynch;


import com.google.gson.Gson;
import net.thumbtack.school.asynch.dto.DtoRequest;
import net.thumbtack.school.asynch.dto.DtoResponse;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;


/**
 * Asynchronous http server based on NIO selectors.
 */
public class SelectorsServerDemo {


    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost", 5050));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT, null);

        while (true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                if (key.isAcceptable()) {
                    SocketChannel clientChannel = serverChannel.accept();
                    clientChannel.configureBlocking(false);
                    clientChannel.register(selector, SelectionKey.OP_READ);
                    log("Connection Accepted: " + clientChannel.getLocalAddress() + "\n");

                } else if (key.isReadable()) {
                    // Read part
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(9064);
                    int n = client.read(buffer);
                    if (n == -1) {
                        client.close();
                    }
                    String result = new String(buffer.array()).trim();
                    log("Message received: " + result);

                    // Parsing json and calculating result
                    String response = null;
                    if (result.contains("{")) {
                        String request = result.substring(result.indexOf("{"));
                        response = getJsonSum(request);
                    }
                    String httpResp = response == null ? createHttpResponse("Hello world") : createHttpResponse(response);
                    key.attach(httpResp);
                    key.interestOps(SelectionKey.OP_WRITE);


                } else if (key.isWritable()) {
                    SocketChannel client = (SocketChannel) key.channel();
                    Object T = key.attachment();
                    ByteBuffer writeBuffer = StandardCharsets.UTF_8.encode(T.toString());
                    client.write(writeBuffer);
                    if (!writeBuffer.hasRemaining()) {
                        writeBuffer.compact();
                        key.interestOps(SelectionKey.OP_READ);
                    }
                    client.close();
                }
                iterator.remove();
            }
        }
    }


    private static String getJsonSum(String request) throws IOException {
        Gson gson = new Gson();
        DtoRequest dtoRequest = gson.fromJson(request, DtoRequest.class);
        DtoResponse dtoResp = new DtoResponse(dtoRequest.getX() + dtoRequest.getY());
        return gson.toJson(dtoResp);
    }

    private static void log(String str) {
        System.out.println(str);
    }

    private static String createHttpResponse(String result) {
        return "HTTP/1.1 200 OK\n" +
                "Content-Length: 50\n" +
                "Content-Type: text/html\n" +
                "Connection: Closed\n\n" +
                "<html>\n" +
                "<body>\n" +
                "<h1> " + result + " </h1>\n" +
                "</body>\n" +
                "</html>";
    }
}
