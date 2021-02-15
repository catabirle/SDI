//This class connects to the server and accepts commands from the user.

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class RMIClient implements Serializable {

    public static void main(String[] args) {

        String environment;
        String hostname;
        int portnumber;
        String clientpath;
        String serverpath;
        String upload = "upload";
        String download = "download";
        String dir = "dir";
        String mkdir = "mkdir";
        String read = "read";
        String write = "write";
        String rmdir = "rmdir";
        String rm = "rm";
        String shutdown = "exit";

        try {

            environment = System.getenv("SERVER_PORT");
            System.out.println(environment);

            hostname = environment.split(":")[0];

            portnumber = Integer.parseInt(environment.split(":")[1]);
            System.out.println("seeking connection on:" + environment);

            Registry myreg = LocateRegistry.getRegistry(hostname, portnumber);
            RmiInterface inter = (RmiInterface) myreg.lookup("remoteObject");

            //to upload a file
            if (upload.equals(args[0])) {
                clientpath = args[1];
                serverpath = args[2];

                File clientpathfile = new File(clientpath);
                byte[] mydata = new byte[(int) clientpathfile.length()];
                FileInputStream in = new FileInputStream(clientpathfile);
                System.out.println("uploading to server...");
                in.read(mydata, 0, mydata.length);
                inter.uploadFileToServer(mydata, serverpath, (int) clientpathfile.length());

                in.close();
            }
            //to download a file
            if (download.equals(args[0])) {
                serverpath = args[1];
                clientpath = args[2];

                byte[] mydata = inter.downloadFileFromServer(serverpath);
                System.out.println("downloading...");
                File clientpathfile = new File(clientpath);
                FileOutputStream out = new FileOutputStream(clientpathfile);
                out.write(mydata);
                out.flush();
                out.close();
            }

            //to list all the files in a directory
            if (dir.equals(args[0])) {
                serverpath = args[1];
                String[] filelist = inter.listFiles(serverpath);
                for (String i : filelist) {
                    System.out.println(i);
                }
            }
            //to read file
            if (read.equals(args[0])) {
                try {
                    File file = new File(args[1]);
                    Scanner myReader = new Scanner(file);
                    while (myReader.hasNextLine()) {
                        String data = myReader.nextLine();
                        System.out.println(data);
                    }
                    myReader.close();
                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }

            //to Write file
            if (write.equals(args[0])) {
                try {
                    FileWriter myWriter = new FileWriter(args[1]);
                            
                    myWriter.write(args[2]);
                         
                    myWriter.close();
                    System.out.println("Successfully wrote to the file.");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }

            //to create a new directory
            if (mkdir.equals(args[0])) {
                serverpath = args[1];
                boolean bool = inter.createDirectory(serverpath);
                System.out.println("directory created :" + bool);
            }

            //to remove/delete a directory
            if (rmdir.equals(args[0]) || rm.equals(args[0])) {
                serverpath = args[1];
                boolean bool = inter.removeDirectoryOrFile(serverpath);
                System.out.println("directory deleted :" + bool);
            }
            //to shutdown the client

            if (shutdown.equals(args[0])) {
                System.exit(0);
                System.out.println("Client has shutdown. Close the console");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error with connection or command. Check your hostname or command");
        }
    }

}
