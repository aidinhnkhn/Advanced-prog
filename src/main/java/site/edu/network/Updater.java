package site.edu.network;

import elements.chat.Chat;
import elements.chat.pm.Pm;
import elements.people.Professor;
import elements.people.Student;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import shared.messages.message.Message;
import shared.messages.message.MessageStatus;
import shared.messages.response.Response;
import shared.messages.response.ResponseStatus;
import shared.util.Config;
import shared.util.JsonCaster;
import site.edu.Main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class Updater implements Runnable {
    private Socket socket;
    private PrintWriter printWriter;
    private Scanner scanner;

    private String authToken;
    private static Logger log = LogManager.getLogger(Updater.class);

    public Updater(Socket socket) throws IOException {
        this.socket = socket;
        this.printWriter = new PrintWriter(socket.getOutputStream());
        this.scanner = new Scanner(socket.getInputStream());
    }

    private void init() throws IOException {

        while (true) {
            try {
                String input = scanner.nextLine();
                while (true) {
                    String nextLine = scanner.nextLine();
                    if (nextLine.equals("over")) break;
                    input += nextLine;
                }

                Response response = Response.fromJson(input);

                analyse(response);
                while (Main.mainClient.getUser() == null) {
                    System.out.println("updater is sleeping");
                    Thread.sleep(2000);
                }
                Thread.sleep(500);
                sendUpdateRequest();
            } catch (NoSuchElementException e) {
                try {
                    this.socket = new Socket(InetAddress.getLocalHost(), Config.getConfig().getProperty(Integer.class, "serverPort"));
                    this.printWriter = new PrintWriter(socket.getOutputStream());
                    this.scanner = new Scanner(socket.getInputStream());
                    System.out.println("connected");
                    Main.mainClient.getServerController().setServerOnline(true);

                } catch (ConnectException e2) {
                    Main.mainClient.getServerController().setServerOnline(false);
                    System.out.println("disconnected");
                }
            } catch (InterruptedException e) {
                log.info("updater thread interrupted");
            }
        }
    }

    private void sendUpdateRequest() {
        if (Main.mainClient.getUser() == null) return;
        Message message = new Message(MessageStatus.Update, authToken);
        message.addData("id", Main.mainClient.getUser().getId());
        sendMessage(Message.toJson(message));
    }

    private void analyse(Response response) {
        if (response.getStatus() == ResponseStatus.AuThToken) {
            this.authToken = (String) (response.getData("authToken"));
            log.info("authToken set");
        } else if (response.getStatus() == ResponseStatus.Update)
            update(response);

    }

    private void setProfessor(Response response) {
        Professor professor = JsonCaster.professorCaster((String) response.getData("professor"));
        Main.mainClient.setUser(professor);
    }

    private void setStudent(Response response) {
        Student student = JsonCaster.studentCaster((String) response.getData("student"));
        Main.mainClient.setUser(student);
    }

    private void update(Response response) {
        ArrayList<Chat> chats = JsonCaster.chatArrayListCaster((String) response.getData("chats"));
        if (Main.mainClient.getUser().getId().charAt(0) == 's')
            setStudent(response);
        else
            setProfessor(response);
        chats.sort(Comparator.comparing(Chat::getDate));
        Collections.reverse(chats);
        for (Chat chat : chats){
            if (Main.mainClient.getUser().getId().equals(chat.getStudentId1()))
                chat.setName(chat.getStudentName2());
            else
                chat.setName(chat.getStudentName1());
        }
        Main.mainClient.setChats(chats);
        log.info("information Updated");
    }

    public void sendMessage(String message) {
        printWriter.println(message);
        printWriter.flush();
    }

    @Override
    public void run() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
