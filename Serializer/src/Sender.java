import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Sender
{   
    public static void main(String args[])
    {
        // Cmd line parsing
        String outXMLFilePath = "serialized.xml";
        String serverHostname = "localhost";
        int serverPort = 4000;
        if (args.length == 0)
        {
            System.out.println("No command line arguments provided. Type Sender -help to see available arguments");
        }
        else if (args.length == 1 && args[0].equals("-help"))
        {
            System.out.println("Usage: Sender");
            System.out.println("Usage: Sender -help");
            System.out.println("Usage: Sender <outXMLFilePath>");
            System.out.println("Usage: Sender <outXMLFilePath> <serverHostname>");
            System.out.println("Usage: Sender <outXMLFilePath> <serverHostname> <serverPort>");
            System.out.println("Defaults: outXMLFilePath=serialized.xml, serverHostname=localhost, serverPort=4000");
        }
        else if (args.length == 1)
        {
            outXMLFilePath = args[0];
        }
        if (args.length == 2)
        {
            serverHostname = args[1];
        }
        if (args.length == 3)
        {
            Integer.parseInt(args[2]);
        }
       
        System.out.println("The following program creates user specified objects, serializes them, and sends them over a network to a deserializer");
        System.out.println("Firstly, create an object as instructed.");
        System.out.println("----------------------------------------");
        Object obj = ObjectCreator.useObjectCreator();
        System.out.println("----------------------------------------");
        System.out.println("Serializing object. XML located at " + outXMLFilePath);
        Document doc = new Serializer().serialize(obj);
        try
        {
            XMLOutputter outputter = new XMLOutputter();
            outputter.setFormat(Format.getPrettyFormat());
            outputter.output(doc, new FileWriter(outXMLFilePath));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("----------------------------------------");
        System.out.println("Sending serialized object");
        System.out.println("----------------------------------------");
        sendObject(doc, serverHostname, serverPort);
        System.out.println("----------------------------------------");
    }
    
    private static void sendObject(Object obj, String hostname, int port)
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
