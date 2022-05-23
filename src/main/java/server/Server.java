package server;

import classes.rooms.DTR;
import classes.rooms.MGR;
import classes.rooms.Room;
import classes.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    final String logStart = "\tLog-Debug:\n";
    public static final short MAIN_GAME = 0, DRAW_TOUR = 1;
    private static ServerSocket serverSocket;
    private ArrayList<Room> rooms;
    private ArrayList<User> users;
    private Scanner in;
    int id = 0;
    int rId = 0;

    public Server() {
        rooms = new ArrayList<Room>();
        users = new ArrayList<User>();
        try {
            serverSocket = new ServerSocket(5000);
            System.out.println("!start");
            while (true) {
                Socket socket = serverSocket.accept();
                in = new Scanner(socket.getInputStream());
                User user = new User(id); id++;
                user.setSocket(socket);
                ReaderThread readerThread = new ReaderThread(user);
                readerThread.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toRoom(User user, int mode) {
        for (Room room : rooms) {
            if (mode == 0 && room.getClass() == MGR.class) {
                if (room.addUser(user)) {
                    return;
                }
            }
            else if (mode == 1 && room.getClass() == DTR.class) {
                if (room.addUser(user)) {
                    return;
                }
            }
        }
        if (mode == 0) {
            MGR room = new MGR(rId);
            rId++;
            room.addUser(user);
            rooms.add(room);
        }
        else if (mode == 1) {
            DTR room = new DTR(rId);
            rId++;
            room.addUser(user);
            rooms.add(room);
        }
    }

    private class ReaderThread extends Thread {
        User user;

        public ReaderThread(User user) {
            this.user = user;
        }

        @Override
        public void run() {
            try {
                Scanner in = new Scanner(user.getSocket().getInputStream());
                main: while (true) {
                    if (in.hasNext()) {
                        String[] message = in.nextLine().split("/");
                        for (String ss : message) {
                            System.out.print(ss + "/");
                        }
                        System.out.print("\n");
                        try {
                            switch (message[0]) {
                                case "init": {
                                    if (!user.isInit()) {
                                        user.setName(message[2]);
                                        users.add(user);
                                        toRoom(user, Integer.parseInt(message[1]));
                                        user.getPrintWriter().println("init/200/" + user.getId() + "/");
                                        user.getPrintWriter().flush();
                                        String s = "";
                                        for (User u : user.getRoom().getMembers()) {
                                            s += u.getName() + ";";
                                            u.getPrintWriter().println("new/" + user.getName());
                                            u.getPrintWriter().flush();
                                        }
                                        user.getPrintWriter().println(s);
                                        user.getPrintWriter().flush();
                                        System.out.print(logStart);
                                        for (Room room : rooms) {
                                            System.out.println(room.toString());
                                        }
                                        user.setInit();
                                    }
                                    else {
                                        System.out.println("User: " + user.getId() + ". Bad request.");
                                        user.getPrintWriter().println("init/403/Already init");
                                        user.getPrintWriter().flush();
                                    }
                                    break;
                                }
                                case "run": {
                                    switch (message[1]) {
                                        case "try": {
                                            if (user.getRoom().canRun()) {
                                                String q = "";
                                                for (User u : user.getRoom().getMembers()) {
                                                    q += u.getName() + ",";
                                                }
                                                for (User u : user.getRoom().getMembers()) {
                                                    u.getPrintWriter().println("request/run/" + q);
                                                    u.getPrintWriter().flush();
                                                }
                                                user.getRoom().run();
                                                return;
                                                //Удалить всех юзеров с основново сервера
                                            }
                                            else {
                                                user.getPrintWriter().println("run/409/Wait");
                                                user.getPrintWriter().flush();
                                            }
                                            break;
                                        }
                                        case "finnish": {
                                            if (user.getRoom().run()) {
                                                return;
                                            }
                                            else {
                                                user.getPrintWriter().println("run/409/Wait");
                                                user.getPrintWriter().flush();
                                            }
                                            break;
                                        }
                                    }
                                    break;
                                }
                                case "exit": {
                                    if (user.getRoom().kickUser(user.getId())) {
                                        for (int i = 0; i < users.size(); i++) {
                                            if (users.get(i).getId() == user.getId()) {
                                                users.remove(i);
                                                user.getPrintWriter().println("exit/200/goodbye");
                                                user.getPrintWriter().flush();
                                                return;
                                            }
                                        }
                                    }
                                    else {
                                        System.out.println("User: " + user.getId() + ". Bad request.");
                                        user.getPrintWriter().println("exit/403/can not");
                                        user.getPrintWriter().flush();
                                    }
                                    break;
                                }
                                default: {
                                    System.out.println("User: " + user.getId() + ". Bad request.");
                                    user.getPrintWriter().println("Null/404/Not found");
                                    user.getPrintWriter().flush();
                                }
                            }
                        }
                        catch (ArrayIndexOutOfBoundsException exception) {
                            System.out.println("User: " + user.getId() + ". Bad request.");
                            user.getPrintWriter().println("Null/400/Wrong request");
                            user.getPrintWriter().flush();
                        }
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
