import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.jdom2.Document;

public class Receiver
{
    private static int port = 4000;
    
    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            System.out.println("No command line arguments provided. Type Reciever <serverPort> to specify a port other than 4000");
        }
        else if (args.length == 1)
        {
            port = Integer.parseInt(args[0]);
        }
        
        try
        {
            System.out.println("Creating server receiver");
            System.out.println("----------------------------------------");
            System.out.println("To close server, type ^c");
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server socket created on port " + port);
            Socket socket = serverSocket.accept();
            System.out.println("Receieved connection");
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to input stream");
            Document doc = (Document) in.readObject();
            System.out.println("Object read from input stream");
            System.out.println("----------------------------------------");
            System.out.println("Deserializing received object");
            System.out.println("----------------------------------------");
            Object obj = new Deserializer().deserialize(doc);
            System.out.println("----------------------------------------");
            System.out.println("Visualizing object (to stdout and in file deserialized.txt)");
            System.out.println("----------------------------------------");
            // TODO use a modified version of assignment 2 here
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
