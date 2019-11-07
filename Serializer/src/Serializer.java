import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import org.jdom2.*;

// Serialization to XML code based off code Java Reflection in Action textbook by Ira R. Forman and Nate Forman
// Textbook found here: http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.116.5796&rep=rep1&type=pdf
public class Serializer 
{   
	public Document serialize(Object obj)
	{	
		return serializeObject(obj, new Document(new Element("serialized")), new IdentityHashMap<Object, Integer>());
	}
	
	private Document serializeObject(Object obj, Document doc, IdentityHashMap<Object, Integer> map)
	{
	    // Get the size of the map as ID, then add this obj to the map
		int objID = map.size(); 
		map.put(obj, objID);
		
		Element objElement = new Element("object");
        objElement.setAttribute("id", Integer.toString(objID));
		
		doc.getRootElement().addContent(objElement);
		
		if (obj != null)
		{
		    Class objClass = obj.getClass();
		    objElement.setAttribute("class", objClass.getName());
		        
    		if (objClass.isPrimitive())
    		{
    		    objElement.addContent(serializePrimitive(obj));
    		}
    		else if (objClass.isArray())
    		{
    		    objElement.setAttribute("length", Integer.toString(Array.getLength(obj)));
    		    for (Element e : serializeArray(obj, doc, map))
    		    {
    		        objElement.addContent(e);
    		    }
    		}
    		else
    		{
    		    for (Field f : objClass.getDeclaredFields())
    		    {
    		        // Only serialize non-static fields
    		        if (!Modifier.isStatic(f.getModifiers()))
    		        {
    		           f.setAccessible(true);
    		           Element fieldElement = new Element("field");
    		           fieldElement.setAttribute("name", f.getName());
    		           fieldElement.setAttribute("declaringclass", f.getDeclaringClass().getName());
    		           
    		           Class fieldType = f.getType();
    		           Object fieldObject = null;
    		           try
    		           {  
    		               fieldObject = f.get(obj);
    		           } 
    		           catch (IllegalArgumentException | IllegalAccessException e)
    		           {
    		               e.printStackTrace();
    		           }
    		           Element fieldContents = null;
    		           // Serialize transient. If respecting native Java serialization,
    		           // uncomment the following if statement and change the next if statement to else if
    		           /*
    		           if (Modifier.isTransient(f.getModifiers()))
    		           {
    		               fieldContents = new Element("transient");
    		           }
    		           */
    		           if (fieldType.isPrimitive())
    		           {
    		               fieldContents = serializePrimitive(fieldObject);
    		           }
    		           else
    		           {
		                   fieldContents = serializeReference(fieldObject, fieldType, doc, map);
    		           }
    		           
    		           fieldElement.addContent(fieldContents);
    		           objElement.addContent(fieldElement);
    		        }
    		    }
    		}
		}
		else
		{
		    objElement.addContent(new Element("null"));
		}
		
		return doc;
	}
	
	private Element serializeReference(Object fieldObject, Class fieldClass, Document doc, IdentityHashMap<Object, Integer> map)
	{  
	    Element referenceElement = new Element("reference");
	    
        if (map.containsKey(fieldObject))
	    {
	        referenceElement.setText(map.get(fieldObject).toString());
	    }
        else
        {
            referenceElement.setText(Integer.toString(map.size()));
            serializeObject(fieldObject, doc, map);
        }
	    
	    return referenceElement;
	}
	
	private Element[] serializeArray(Object array, Document doc, IdentityHashMap<Object, Integer> map)
	{
	    int length = Array.getLength(array);
	    Element[] arrayElements = new Element[length];
	    Class componentType = array.getClass().getComponentType();
	    
	    for (int i = 0; i < length; i++)
	    {
            Object arrayObject = Array.get(array, i);

            Element arrayElement = null;
            if (componentType.isPrimitive())
            {
                arrayElement = serializePrimitive(arrayObject);
            }
            else
            {
                arrayElement = serializeReference(arrayObject, componentType, doc, map);
            }
            
            arrayElements[i] = arrayElement;
	    }
	    return arrayElements;
	}
	
	private Element serializePrimitive(Object obj)
	{
        Element valueElement = new Element("value");
        try
        {    
            valueElement.setText(obj.toString());
        }
        catch (IllegalDataException e)
        {
            System.out.println("Primitive value could not be written to XML, writing invalid...");
            e.printStackTrace();
            valueElement.setText("invalid");
        }
        return valueElement;
	}
}
