package classes.rooms;

import classes.User;

import java.util.ArrayList;

public abstract class Room {
    protected long id;
    protected ArrayList<User> members;
    protected short maxSize;
    protected short gameStage;
    protected boolean open = true;

    public Room(long id) {
        this.id = id;
        this.members = new ArrayList<>();
        this.gameStage = 0;
    }

    public abstract boolean updateGameStage();

    public boolean addUser(long id) {
        if (open && members.size() < maxSize) {
            members.add(new User(id));
            return true;
        }
        return false;
    }

    public boolean addUser(User user) {
        if (members.size() < maxSize) {
            members.add(user);
            user.setRoom(this);
            return true;
        }
        return false;
    }

    public boolean kickUser(long id) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getId() == id) {
                members.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean run() {
        if (members.size() == maxSize) {
            String team = "";
            for (User user : members) {
                team += user.getName() + ",";
            }
            new GameTimer().start();
            return true;
        }
        return false;
    }

    public boolean canRun() {return members.size() == maxSize;}

    public long getId() {
        return id;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public short getGameStage() {
        return gameStage;
    }

    @Override
    public String toString() {
        String s = "Room {" + "\n\tid = " + id + ", \n\tmembers:";
        for (int i = 0; i < members.size(); i++) {
            s += "\n\t" + members.get(i).toString();
        }
        return  s + ",\n\tmaxSize = " + maxSize + ",\n\tgameStage = " + gameStage + "\n}";
    }

    public class GameTimer extends Thread {
        @Override
        public void run() {
            try {
                //sleep(90000);
                sleep(90100);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            updateGameStage();
        }
    }
}
