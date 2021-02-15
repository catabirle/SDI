//This class contains implementation of all the functionalities provided to the client. 

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RmiImplementation extends UnicastRemoteObject implements RmiInterface, Serializable {

    protected RmiImplementation(String s) throws RemoteException {
        File storageDir = new File(s);
        storageDir.mkdir();
    }

    public void uploadFileToServer(byte[] mydata, String serverpath, int length) throws RemoteException {

        try {
            File serverpathfile = new File(serverpath);
            FileOutputStream out = new FileOutputStream(serverpathfile);
            byte[] data = mydata;

            out.write(data);
            out.flush();
            out.close();

        } catch (IOException e) {

            e.printStackTrace();
        }

        System.out.println("Done writing data...");

    }

    public byte[] downloadFileFromServer(String serverpath) throws RemoteException {

        byte[] mydata;

        File serverpathfile = new File(serverpath);
        mydata = new byte[(int) serverpathfile.length()];
        FileInputStream in;
        try {
            in = new FileInputStream(serverpathfile);
            try {
                in.read(mydata, 0, mydata.length);
            } catch (IOException e) {

                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {

                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        return mydata;

    }

    public String[] listFiles(String serverpath) throws RemoteException {
        File serverpathdir = new File(serverpath);
        return serverpathdir.list();

    }

    
    public List<String> readTextFileByLines(String serverpath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(serverpath));
        return lines;
    }
    
    public  void writeToTextFile(String fileName, String content) throws IOException {
        Files.write(Paths.get(fileName), content.getBytes(), StandardOpenOption.CREATE);
    }

    public boolean createDirectory(String serverpath) throws RemoteException {
        File serverpathdir = new File(serverpath);
        return serverpathdir.mkdir();

    }

    public boolean removeDirectoryOrFile(String serverpath) throws RemoteException {
        File serverpathdir = new File(serverpath);
        return serverpathdir.delete();

    }

    
}
