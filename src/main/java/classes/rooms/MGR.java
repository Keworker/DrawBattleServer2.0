package classes.rooms;

import classes.User;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class MGR extends Room {
    final String LC = "*lost connected*";
    String txts[][] = {{LC, LC, LC, LC, LC, LC}, {LC, LC, LC, LC, LC, LC}, {LC, LC, LC, LC, LC, LC}};
    String imgs[][] = {{LC, LC, LC, LC, LC, LC}, {LC, LC, LC, LC, LC, LC}, {LC, LC, LC, LC, LC, LC}};
    int offset;
    String text1, img2, text2, img1;

    public MGR(long id) {
        super(id);
        offset = 1;
        maxSize = 2;
    }

    @Override
    public boolean updateGameStage() {
        if (gameStage < 1) {
            if (gameStage % 2 == 0) {
                for (int i = 0; i < members.size(); i++) {
                    members.get(i).getPrintWriter().println("request/text");
                    members.get(i).getPrintWriter().flush();
                    Scanner in = new Scanner(members.get(i).getIn());
                    System.out.println(gameStage);
                    txts[gameStage / 2][(i + offset) % maxSize] = LC + "Random text";
                    for (int j = 0; j < 15; j++) {
                        try {
                            if (members.get(i).getIn().available() > 0) {
                                String s[] = in.nextLine().split("/");
                                if (s[0].matches("text")) {
                                    System.out.println(s[1] + " " + s[1].length());
                                    txts[gameStage / 2][(i + offset) % maxSize] = s[1];
                                    if (i == 0) {
                                        text1 = s[1];
                                    }
                                    else {
                                        text2 = s[1];
                                    }
                                    break;
                                }
                            }
                            sleep(1000);
                        }
                        catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //К строке прибавляется рандомная история из 20-30 заранее забитых
                }
                members.get(0).getPrintWriter().println("text/" + text2);
                members.get(0).getPrintWriter().flush();
                members.get(1).getPrintWriter().println("text/" + text1);
                members.get(0).getPrintWriter().flush();
//                for (int i = 0; i < members.size(); i++) {
////                    members.get(i).getPrintWriter().println("text/" + txts[0][prevId(i)]);
//                    members.get(i).getPrintWriter().println("text/" + text1);
//                    members.get(i).getPrintWriter().flush();
//                }
            }
            else {
                for (int i = 0; i < members.size(); i++) {
                    members.get(i).getPrintWriter().println("request/image");
                    members.get(i).getPrintWriter().flush();
                    Scanner in = new Scanner(members.get(i).getIn());
                    for (int j = 0; j < 15; j++) {
                        try {
                            if (members.get(i).getIn().available() > 0) {
                                String s[] = in.nextLine().split("/");
                                if (s[0].matches("image")) {
                                    System.out.println(s[1]+ " " +s[1].length());
                                    imgs[gameStage / 2][(i + offset) % maxSize] = s[1];
                                    if (i == 0) {
                                        img1 = s[1];
                                    }
                                    else {
                                        img2 = s[1];
                                    }
                                    break;
                                }
                            }
                            sleep(1000);
                        }
                        catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                members.get(0).getPrintWriter().println("image/" + img2);
                members.get(0).getPrintWriter().flush();
                members.get(1).getPrintWriter().println("image/" + img1);
                members.get(0).getPrintWriter().flush();
//                for (int i = 0; i < members.size(); i++) {
////                    members.get(i).getPrintWriter().println(members.get(i).getId() + ": some image got");
//                    members.get(i).getPrintWriter().println("image/" + img2);
//                    members.get(i).getPrintWriter().flush();
//                }
            }
        }
        else {
            members.get(0).getPrintWriter().println("t1/" + text1);
            members.get(0).getPrintWriter().println("t2/" + text2);
            members.get(0).getPrintWriter().println("i1/" + img1);
            members.get(0).getPrintWriter().println("i2/" + img2);
            members.get(0).getPrintWriter().flush();
            members.get(1).getPrintWriter().println("t1/" + text1);
            members.get(1).getPrintWriter().println("t2/" + text2);
            members.get(1).getPrintWriter().println("i1/" + img1);
            members.get(1).getPrintWriter().println("i2/" + img2);
            members.get(1).getPrintWriter().flush();
//            while (members.size() > 0) {
//                for (User u : members) {
//                    try {
//                        if (u.getIn().available() > 0) {
//                            Scanner in = new Scanner(u.getIn());
//                            if (in.nextLine() == "game/exit") {
//                                for (int i = 0; i < members.size(); i++) {
//                                    if (members.get(i).getId() == u.getId()) {
//                                        members.remove(i);
//                                        u.getPrintWriter().println("exit/200/goodbye");
//                                        u.getPrintWriter().flush();
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
            //удалить комнату с сервера
            return false;
        }
        for (int i = 0; i < members.size(); i++) {
            members.get(i).getPrintWriter().println("request/next");
            members.get(i).getPrintWriter().flush();
        }
        offset++;
        gameStage++;
        new GameTimer().start();
        return true;
    }

    int prevId(int id) {
        if (id == 0) {
            return maxSize - 1;
        }
        if (id == 1) {
            return 0;
        }
        return id - 1;
    }
}
