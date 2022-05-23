package debug;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientDebug1 {
    public PrintWriter pW;
    public boolean flag = true;

    public ClientDebug1() {
        System.out.println("!start");
        try {
            String users[] = new String[6];
            Socket socket = new Socket("127.0.0.1", 5000);
            pW = new PrintWriter(socket.getOutputStream());
            pW.println("init/0/Nick");
            pW.flush();
            new Thread() {
                Scanner scanner = new Scanner(socket.getInputStream());
                @Override
                public void run() {
                    while (true) {
                        if (scanner.hasNext()) {
                            String s[] = scanner.nextLine().split("/");
                            for (String ss : s) {
                                System.out.print(ss + "/");
                            }
                            System.out.print("\n");
                            try {
                                switch (s[0]) {
                                    case "request": {
                                        switch (s[1]) {
                                            case "text": {
                                                pW.println("text/some text");
                                                pW.flush();
                                                break;
                                            }
                                            case "image": {
                                                pW.println("image/some image");
                                                pW.flush();
                                                break;
                                            }
                                            case "run": {
                                                pW.println("run/finnish");
                                                pW.flush();
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                    case "init": {
                                        continue;
                                    }
                                    case "run": {
                                        if (Integer.parseInt(s[1]) == 200) {
                                            String teams[] = s[2].split(",");
                                            for (int i = 0; i < 6; i++) {
                                                users[i] = teams[i];
                                            }
                                            //Переход активити в андроид приложении
                                        }
                                        break;
                                    }
                                    case "exit": {
                                        if (Integer.parseInt(s[1]) == 200) {
                                            flag = false;
                                            return;
                                        }
                                        break;
                                    }
                                }
                            }
                            catch (ArrayIndexOutOfBoundsException exception) {
                                exception.printStackTrace();
                            }
                        }
                    }
                }
            }.start();
            new ReqThread().start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ClientDebug1 cd1 = new ClientDebug1();
    }

    public class ReqThread extends Thread {
        Scanner in = new Scanner(System.in);

        @Override
        public void run() {
            while (flag) {
                if (in.hasNext()) {
                    switch (in.nextLine()) {
                        case "run-game": {
                            pW.println("run/try");
                            pW.flush();
                            break;
                        }
                        case "exit-menu": {
                            pW.println("exit/");
                            pW.flush();
                            break;
                        }
                        case "exit-game": {
                            pW.println("game/exit");
                            pW.flush();
                            break;
                        }
                    }
                }
            }
        }
    }
}
