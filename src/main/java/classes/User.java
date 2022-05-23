package classes;

import classes.rooms.Room;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class User {
    private long id;
    private short gameStage;
    private InputStream in;
    private OutputStream out;
    private PrintWriter printWriter;
    private Socket socket;
    private String name;
    private boolean isInit;
    private Room room;

    public User(long id) {
        this.id = id;
        //Ник взять из бд
        this.gameStage = 0;
        this.isInit = false;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            printWriter = new PrintWriter(out);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public InputStream getIn() {
        return in;
    }

    public OutputStream getOut() {
        return out;
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public long getId() {
        return id;
    }

    public short getGameStage() {
        return gameStage;
    }

    @Override
    public String toString() {
        return "User " + name +  " {" + "\n\t\tid = " + id + ", \n\t\tgameStage = " + gameStage +
                ", \n\t\tin = " + in.toString() + ", \n\t\tout = " + out.toString() +
                ", \n\t\tprintWriter = " + printWriter.toString() + "\n\t}";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isInit() {
        return isInit;
    }

    public void setInit() {
        isInit = true;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
