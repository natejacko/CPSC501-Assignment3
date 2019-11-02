import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;

public class SerializerTesting
{
    private final String serializePath = "../serialize.xml";
    private final String deserializePath = "../deserialize.xml";
    
    @Test
    public void testStringSerialize()
    {
        serializeThenDeserialize(new String("abc"));
        assert(compareFiles());
    }
    
    @Test
    public void testTestClassSerialize()
    {
        serializeThenDeserialize(new TestClass());
        assert(compareFiles());
    }
    
    @Test
    public void testTestClassChildSerialize()
    {
        serializeThenDeserialize(new TestClassChild());
        assert(compareFiles());
    }
    
    // 1) Serialize and write to XML
    // 2) Deserialize the serialized doc
    // 3) Serialize the deserialized object and write to XML
    // This completes a whole cycle and should output two files which are identical
    private void serializeThenDeserialize(Object obj)
    {
        Document doc = new Serializer().serialize(obj);
        writeXML(doc, serializePath);
        
        Object deserializedObj = new Deserializer().deserialize(doc);
        doc = new Serializer().serialize(deserializedObj);
        writeXML(doc, deserializePath);
    }

    // Compare contents of two files
    private boolean compareFiles()
    {
        try
        {
            byte[] f1 = Files.readAllBytes(Paths.get(serializePath));
            byte[] f2 = Files.readAllBytes(Paths.get(deserializePath));
            return Arrays.equals(f1, f2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    // Write an XML doc in specified location
    private void writeXML(Document doc, String filePath)
    {
        try
        {
            XMLOutputter outputter = new XMLOutputter();
            outputter.setFormat(Format.getPrettyFormat());
            outputter.output(doc, new FileWriter(filePath));
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
