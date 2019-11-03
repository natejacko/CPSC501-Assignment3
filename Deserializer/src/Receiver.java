import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Receiver
{
    private static int port = 4000;
    
    public static void main(String[] args)
    {
        if (args.length == 1)
        {
            port = Integer.parseInt(args[0]);
        }
        
        try
        {
            System.out.println("To close server, type ^c");
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server socket created on port " + port);
            Socket socket = serverSocket.accept();
            System.out.println("Receieved connection");
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to input stream");
            Document doc = (Document) in.readObject();
            System.out.println("Object read from input stream");  
            
            // TODO deserialize and visualize
            
            Object obj = new Deserializer().deserialize(doc);
            
            // Complete the cycle and re-serialize the deserialized object to compare 
            doc = new Serializer().serialize(obj);
            new XMLOutputter().output(doc, System.out);
            XMLOutputter outputter = new XMLOutputter();
            outputter.setFormat(Format.getPrettyFormat());
            outputter.output(doc, new FileWriter("../out.xml"));
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
