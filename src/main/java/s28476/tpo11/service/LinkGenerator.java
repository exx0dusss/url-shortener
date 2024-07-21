package s28476.tpo11.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class LinkGenerator {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Random random = new Random();

    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;

    @Value("${server.servlet.context-path}")
    private String serverContext;


    public static String getRandomSequence() {
        StringBuilder link = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(CHARACTERS.length());
            link.append(CHARACTERS.charAt(index));
        }
        return link.toString();
    }

    public String getLink(String address, String id) {
        return "http://" + serverAddress + ":" + serverPort + serverContext + "/" + address + "/" + id;
    }

    public String getCHARACTERS() {
        return CHARACTERS;
    }
}

