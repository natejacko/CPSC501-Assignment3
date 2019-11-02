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
        deserializeInstances(instances, elements);
        
        // Assign fields in instances
        deserializeFields(instances, elements);
        
        return instances.get("0");
    }
    
    private void deserializeInstances(HashMap<String, Object> instances, List<Element> elements)
    {
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
    }
    
    private void deserializeFields(HashMap<String, Object> instances, List<Element> elements)
    {
        for (Element e : elements)
        {
           Object obj = instances.get(e.getAttributeValue("id"));
           
           if (obj == null)
           {
               continue;
           }
           
           Class objClass = obj.getClass();
           if (Modifier.isTransient(objClass.getModifiers()))
           {
               continue;
           }
           
           List<Element> children = e.getChildren();
           // TODO
           // If traversal up superclass to set inherited fields, may need to utilize declaringclass attribute
           // instead of just using the object class
           if (objClass.isArray())
           {
               for (int i = 0; i < Array.getLength(obj); i++)
               {
                   Element c = children.get(i);
                   Array.set(obj, i, getValueFromElement(c, obj, objClass.getComponentType(), instances));
               }
           }
           else
           {
               for (Element c : children)
               {
                   String fieldName = c.getAttributeValue("name");
                   try
                   {
                       Field f = objClass.getDeclaredField(fieldName);
                       if (Modifier.isTransient(f.getModifiers()))
                       {
                           continue;
                       }
                       if (!Modifier.isPublic(f.getModifiers()))
                       {
                           f.setAccessible(true);
                       }
                       Element fieldElement = c.getChildren().get(0);
                       f.set(obj, getValueFromElement(fieldElement, obj, f.getType(), instances));
                   } 
                   catch (Exception ex)
                   {
                       System.out.println("Exception encountered whilst deserializing " + fieldName + " in " + objClass.getName());
                       ex.printStackTrace();
                   }
               }
           }
        }
    }
    
    private Object getValueFromElement(Element e, Object obj, Class c, HashMap<String, Object> instances)
    {
        String value = e.getText();
        if (e.getName().equals("value"))
        {
            if (c.equals(boolean.class))
            {
                return Boolean.valueOf(value);
            }
            else if (c.equals(byte.class))
            {
                return Byte.valueOf(value);
            }
            else if (c.equals(short.class))
            {
                return Short.valueOf(value);
            }
            else if (c.equals(int.class))
            {
                return Integer.valueOf(value);
            }
            else if (c.equals(long.class))
            {
                return Long.valueOf(value);
            }
            else if (c.equals(float.class))
            {
                return Float.valueOf(value);
            }
            else if (c.equals(double.class))
            {
                return Double.valueOf(value);
            }
            else if (c.equals(char.class))
            {
                return value.charAt(0);
            }
            else
            {
                // This shouldn't ever be hit since primitives aren't nullable. Just putting in to compile
                return null;
            }
        }
        else
        {
            return instances.get(value);
        }
    }
}
