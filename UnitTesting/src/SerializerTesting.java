import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;

import com.sun.org.apache.bcel.internal.generic.AALOAD;

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
    
    @Test
    public void testPrimitivesSerialize()
    {
        Primitives p = new Primitives();
        p.bool = true;
        p.c = 't';
        p.d = 46.8;
        p.f = 17.654f;
        p.i = 587;
        serializeThenDeserialize(p);
        assert(compareFiles());  
    }
    
    @Test
    public void testReferenceSerialize()
    {
        Reference r = new Reference();
        r.referenceObject = new Primitives();
        serializeThenDeserialize(r);
        assert(compareFiles());
    }
    
    @Test
    public void testCircularReferenceSerialize()
    {
        CircularReference r1 = new CircularReference();
        CircularReference r2 = new CircularReference();
        r1.id = 24;
        r1.partner = r2;
        r2.id = 46;
        r2.partner = r1;
        serializeThenDeserialize(r1);
        assert(compareFiles());
    }
    
    @Test
    public void testArrayOfReferencesSerialize()
    {
        ArrayOfReferences a = new ArrayOfReferences();
        a.references = new Primitives[2];
        a.references[0] = new Primitives();
        a.references[1] = new Primitives();
        serializeThenDeserialize(a);
        assert(compareFiles());
    }
    
    @Test
    public void testArrayOfIntsSerialize()
    {
        ArrayOfInts a = new ArrayOfInts();
        a.array = new int[2];
        a.array[0] = 10;
        a.array[1] = 20;
        serializeThenDeserialize(a);
        assert(compareFiles());
    }
    
    @Test
    public void testArrayOfDoublesSerialize()
    {
        ArrayOfDoubles a = new ArrayOfDoubles();
        a.array = new double[2];
        a.array[0] = 10.5;
        a.array[1] = 20.6;
        serializeThenDeserialize(a);
        assert(compareFiles());
    }
    
    @Test
    public void testArrayListOfRefrencesSerialize()
    {
        ArrayListOfReferences a = new ArrayListOfReferences();
        a.references = new ArrayList<Primitives>();
        a.references.add(new Primitives());
        a.references.add(new Primitives());
        serializeThenDeserialize(a);
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
