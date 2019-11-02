import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Deserializer
{

    public static void main(String[] args)
    {
        try
        {
            String fileName = "../file.xml";
            if (args.length > 0)
            {
                fileName = args[0];
            }
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new File(fileName));
            Object obj = new Deserializer().deserialize(doc);
            
            // Complete the cycle and re-serialize the deserialized object to compare 
            doc = new Serializer().serialize(obj);
            new XMLOutputter().output(doc, System.out);
            XMLOutputter outputter = new XMLOutputter();
            outputter.setFormat(Format.getPrettyFormat());
            outputter.output(doc, new FileWriter("../out.xml"));
        }
        catch (IOException | JDOMException e)
        {
            e.printStackTrace();
        }
    }
    
    public Object deserialize(Document doc)
    {
        List<Element> elements = doc.getRootElement().getChildren();
        HashMap<String, Object> instances = new HashMap<String, Object>();
        
        // Create instances
        for (Element e : elements)
        {
            Object obj = null;
            
            // Check if object isn't null (checking if <null /> tag doesn't exist)
            if (e.getChild("null") == null)
            {
                String className = e.getAttributeValue("class");
                try
                {
                    Class objClass = Class.forName(className);
                    if (objClass.isArray())
                    {
                        obj = Array.newInstance(objClass.getComponentType(), Integer.valueOf(e.getAttributeValue("length")));
                    }
                    else
                    {
                        Constructor con = objClass.getDeclaredConstructor(null);
                        if (!Modifier.isPublic(con.getModifiers()))
                        {
                            con.setAccessible(true);
                        }
                        obj = con.newInstance(null);
                    }
                } 
                catch (ClassNotFoundException ex)
                {
                    System.out.println("Class " + className + " cannot be deserialized as it could not be found"); 
                    ex.printStackTrace();                    
                } catch (NoSuchMethodException ex)
                {
                    System.out.println("No-arg constructor could be found. " + className + " cannot be deserialized.");
                    ex.printStackTrace();
                } catch (Exception ex)
                {
                    System.out.println("Exception encountered whilst deserializing " + className);
                    ex.printStackTrace();
                } 
            }
            
            instances.put(e.getAttributeValue("id"), obj);
        }
        
        // Assign fields in instances
        for (Element e : elements)
        {
           Object obj = instances.get(e.getAttributeValue("id"));
           
           if (obj == null)
           {
               continue;
           }
           
           Class objClass = obj.getClass();
           List<Element> children = e.getChildren();
           if (objClass.isArray())
           {
               for (int i = 0; i < Array.getLength(obj); i++)
               {
                   Element c = children.get(i);
                   String value = c.getText();
                   if (c.getName().equals("value"))
                   {
                       // Check field type and parse string as type to set
                       Class componentType = objClass.getComponentType();
                       if (componentType.equals(boolean.class))
                       {
                           Array.set(obj, i, Boolean.valueOf(value));
                       }
                       else if (componentType.equals(byte.class))
                       {
                           Array.set(obj, i, Byte.valueOf(value));
                       }
                       else if (componentType.equals(short.class))
                       {
                           Array.set(obj, i, Short.valueOf(value));
                       }
                       else if (componentType.equals(int.class))
                       {
                           Array.set(obj, i, Integer.valueOf(value));
                       }
                       else if (componentType.equals(long.class))
                       {
                           Array.set(obj, i, Long.valueOf(value));
                       }
                       else if (componentType.equals(float.class))
                       {
                           Array.set(obj, i, Float.valueOf(value));
                       }
                       else if (componentType.equals(double.class))
                       {
                           Array.set(obj, i, Double.valueOf(value));
                       }
                       else if (componentType.equals(char.class))
                       {
                           Array.set(obj, i, value.charAt(0));
                       }
                   }
                   else
                   {
                       Array.set(obj, i, instances.get(value));
                   }
               }
           }
           else
           {
               for (Element c : children)
               {
                   String fieldName = c.getAttributeValue("name");
                   try
                   {
                       // TODO
                       // If traversal up superclass to set inherited fields, may need to utilize declaringclass attribute
                       // instead of just using the object class
                       Field f = objClass.getDeclaredField(fieldName);
                       if (!Modifier.isPublic(f.getModifiers()))
                       {
                           f.setAccessible(true);
                       }
                       Element fieldElement = c.getChildren().get(0);
                       String value = fieldElement.getText();
                       if (fieldElement.getName().equals("value"))
                       {
                           // Check field type and parse string as type to set
                           Class fieldType = f.getType();
                           if (fieldType.equals(boolean.class))
                           {
                               f.set(obj, Boolean.valueOf(value));
                           }
                           else if (fieldType.equals(byte.class))
                           {
                               f.set(obj, Byte.valueOf(value));
                           }
                           else if (fieldType.equals(short.class))
                           {
                               f.set(obj, Short.valueOf(value));
                           }
                           else if (fieldType.equals(int.class))
                           {
                               f.set(obj, Integer.valueOf(value));
                           }
                           else if (fieldType.equals(long.class))
                           {
                               f.set(obj, Long.valueOf(value));
                           }
                           else if (fieldType.equals(float.class))
                           {
                               f.set(obj, Float.valueOf(value));
                           }
                           else if (fieldType.equals(double.class))
                           {
                               f.set(obj, Double.valueOf(value));
                           }
                           else if (fieldType.equals(char.class))
                           {
                               f.set(obj, value.charAt(0));
                           }
                       }
                       else
                       {
                           f.set(obj, instances.get(value));
                       }
                   } 
                   catch (Exception ex)
                   {
                       System.out.println("Exception encountered whilst deserializing " + fieldName + " in " + objClass.getName());
                       ex.printStackTrace();
                   }
               }
           }
        }
        
        return instances.get("0");
    }
}
