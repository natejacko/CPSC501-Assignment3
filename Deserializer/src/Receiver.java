import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
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
            while(true)
            {
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
                runInspection("deserialized.txt", obj, true);
                System.out.println("Looping... Type ^C to quit");
            }
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    // Modified driver code given for assignment 2: http://pages.cpsc.ucalgary.ca/~hudsonj/CPSC501F19/A2Code/
    private static void runInspection(String filename, Object obj, boolean recursive) 
    {
        try 
        {
            PrintStream old = System.out;
            File file = new File(filename);
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos);
            System.setOut(ps);
            new Inspector().inspect(obj, recursive);
            ps.flush();
            fos.flush();
            ps.close();
            fos.close();
            System.setOut(old);
            // Additionally print file to console
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String line = null;
            while((line = in.readLine()) != null)
            {
                System.out.println(line);
            }
            in.close();
        } 
        catch (IOException ioe) 
        {
            System.err.println("Unable to open file: " + filename);
        } 
        catch (Exception e) 
        {
            System.err.println("Unable to completely run test: " + obj);
            e.printStackTrace();
        }
    }
}
