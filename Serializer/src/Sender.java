import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
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
            outXMLFilePath = args[0];
            serverHostname = args[1];
        }
        if (args.length == 3)
        {
            outXMLFilePath = args[0];
            serverHostname = args[1];
            serverPort = Integer.parseInt(args[2]);
        }
       
        while(true)
        {
            System.out.println("The following program creates user specified objects, serializes them, and sends them over a network to a deserializer");
            System.out.println("Firstly, create an object as instructed.");
            System.out.println("----------------------------------------");
            Object obj = ObjectCreator.useObjectCreator();
            System.out.println("----------------------------------------");
            System.out.println("Serializing object (to stdout and in file " + outXMLFilePath + ")");
            System.out.println("----------------------------------------");
            Document doc = new Serializer().serialize(obj);
            try
            {
                XMLOutputter outputter = new XMLOutputter();
                outputter.setFormat(Format.getPrettyFormat());
                outputter.output(doc, new FileWriter(outXMLFilePath));
                // Additionally print file to console
                BufferedReader in = new BufferedReader(new FileReader(outXMLFilePath));
                String line = null;
                while((line = in.readLine()) != null)
                {
                    System.out.println(line);
                }
                in.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            System.out.println("----------------------------------------");
            System.out.println("Sending serialized object to " + serverHostname + " at port " + serverPort);
            System.out.println("----------------------------------------");
            sendObject(doc, serverHostname, serverPort);
            System.out.println("----------------------------------------");
            System.out.println("Looping... Type ^C to quit");
            System.out.println("----------------------------------------");
        }
    }
    
    private static void sendObject(Object obj, String hostname, int port)
    {
        try
        {
            Socket socket = new Socket(InetAddress.getByName(hostname), port);
            System.out.println("Socket connected at " + hostname + ":" + port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Connected to output stream");
            out.writeObject(obj);
            out.flush();
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
