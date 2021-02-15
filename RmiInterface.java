//This class interfaces directly between the client and the server
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.*;
import java.rmi.registry.*;
import java.util.List;
public interface RmiInterface extends Remote {
	
	public void uploadFileToServer(byte[] mybyte, String serverpath, int length) throws RemoteException;
	public byte[] downloadFileFromServer(String servername) throws RemoteException;
	public String[] listFiles(String serverpath) throws RemoteException;
        public  List<String> readTextFileByLines(String serverpath) throws IOException;
        public  void writeToTextFile(String fileName, String content) throws IOException;
	public boolean createDirectory(String serverpath) throws RemoteException;
	public boolean removeDirectoryOrFile(String serverpath) throws RemoteException;

}
