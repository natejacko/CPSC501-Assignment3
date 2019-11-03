import java.io.ObjectOutputStream;
import java.net.Socket;

public class Sender
{   
    public static void sendObject(Object obj, String hostname, int port)
    {
        try
        {
            Socket socket = new Socket(hostname, port);
            System.out.println("Socket connected at " + hostname + ":" + port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Connected to output stream");
            out.writeObject(obj);
            System.out.println("Object written to output stream");  
            socket.close();
            System.out.println("Socket closed");
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
