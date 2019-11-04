import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.HashSet;

// Modified code from assignment 2
public class Inspector 
{
    // Using HashSet to track which objects have been inspected
    private HashSet<Object> inspectedObjects = new HashSet<Object>();
    
    public void inspect(Object obj, boolean recursive) 
    {    		
    	if (obj != null)
    	{
            Class c = obj.getClass();
            System.out.println("Begin Inspection:\n");
            inspectClass(c, obj, recursive, 0);
    	}
    }

    public void inspectClass(Class c, Object obj, boolean recursive, int depth) 
    {
		// Type checking
		if (obj == null || !c.isAssignableFrom(obj.getClass()))
		{
			printWithTabs("ERROR: obj null or obj class not assignable to c in call to inspectClass", depth);
			return;
		}
        inspectedObjects.add(obj);
		
		printWithTabs("CLASS INSPECTION", depth);
    	
    	if (c.isArray())
    	{
    		inspectArray(c, obj, recursive, depth, 0);
    		return;
    	}
    	
    	// TODO Not sure if this is even needed, try to test and if needed, check what needs to be returned
    	if (c.isPrimitive())
    	{
    		printWithTabs(obj.toString(), depth);
    		return;
    	}
    	
    	printWithTabs("Class Name: " + c.getName(), depth);
    	
    	// Not listed as part of the assignment, but may be useful
		int mod = c.getModifiers();
		printWithTabs(" Modifiers: " + Modifier.toString(mod), depth);
    	
    	inspectSuperClass(c, obj, recursive, depth);
    	
    	inspectInterfaces(c, obj, recursive, depth);
    	
    	inspectConstructors(c, obj, depth);
    	
    	inspectMethods(c, obj, depth);
    	
    	inspectFields(c, obj, recursive, depth);
    	
    }
    
    public void inspectSuperClass(Class c, Object obj, boolean recursive, int depth)
    {
		// Type checking
		if (obj == null || !c.isAssignableFrom(obj.getClass()))
		{
			printWithTabs("ERROR: obj null or obj class not assignable to c in call to inspectSuperClass", depth);
			return;
		}
		
		printWithTabs("SUPER-CLASS INSPECTION ( " + c.getName() + " )", depth);
		
		// Check Object class
		if (c.equals(Object.class))
		{
			printWithTabs("Object class is the super-class of itself", depth);
			return;
		}
		
		Class superClass = c.getSuperclass();
		
		// Handle null super-classes separately (interfaces, primitives, etc.)
		if (superClass != null)
		{
	    	printWithTabs("Super-class Name: " + superClass.getName(), depth);
	    	
	    	inspectClass(superClass, obj, recursive, depth + 1);
		}
		else
		{
			printWithTabs("None", depth);
		}
    }
    
    public void inspectInterfaces(Class c, Object obj, boolean recursive, int depth)
    {
		// Type checking
		if (obj == null || !c.isAssignableFrom(obj.getClass()))
		{
			printWithTabs("ERROR: obj null or obj class not assignable to c in call to inspectInterfaces", depth);
			return;
		}
		
		printWithTabs("INTERFACE INSPECTION ( " + c.getName() + " )", depth);
		
		Class[] inters = c.getInterfaces();
		
		if (inters.length == 0)
		{
			printWithTabs("None", depth);
		}
		else
		{
	    	for (Class i : inters)
	    	{
	    		printWithTabs("Implements: " + i.getName(), depth);
	    		
	    		inspectClass(i, obj, recursive, depth + 1);
	    	}
		}
    }
    
    public void inspectConstructors(Class c, Object obj, int depth)
    {
		// Type checking
		if (obj == null || !c.isAssignableFrom(obj.getClass()))
		{
			printWithTabs("ERROR: obj null or obj class not assignable to c in call to inspectConstructors", depth);
			return;
		}
		
		printWithTabs("CONSTRUCTOR INSPECTION ( " + c.getName() + " )", depth);
		
		Constructor[] cons = c.getDeclaredConstructors();
		
		if (cons.length == 0)
		{
			printWithTabs("None", depth);
		}
		else
		{
	    	for (Constructor con : cons)
	    	{
	    		printWithTabs("Constructor name: " + con.getName(), depth);
	    		
	    		for (Parameter p : con.getParameters())
	    		{
	    			printWithTabs(" Parameter type: " +p.getType().getName(), depth);
	    		}
	    		
	    		int mod = con.getModifiers();
	    		printWithTabs(" Modifiers: " + Modifier.toString(mod), depth);
	    	}
		}
    }
    
    public void inspectMethods(Class c, Object obj, int depth)
    {
		// Type checking
		if (obj == null || !c.isAssignableFrom(obj.getClass()))
		{
			printWithTabs("ERROR: obj null or obj class not assignable to c in call to inspectMethods", depth);
			return;
		}
		
		printWithTabs("METHOD INSPECTION ( " + c.getName() + " )", depth);
		
		Method[] methods = c.getDeclaredMethods();
		
		if (methods.length == 0)
		{
			printWithTabs("None", depth);
		}
		else
		{
	    	for (Method m : methods)
	    	{
	    		printWithTabs("Method name: " + m.getName(), depth);
	    		
	    		for (Class e : m.getExceptionTypes())
	    		{
	    			printWithTabs(" Throws: " + e.getName(), depth);
	    		}
	    		
	    		for (Class p : m.getParameterTypes())
	    		{
	    			printWithTabs(" Parameter type: " + p.getName(), depth);
	    		}
	    		
	    		printWithTabs(" Return type: " + m.getReturnType().getName(), depth);
	    		
	    		int mod = m.getModifiers();
	    		printWithTabs(" Modifiers: " + Modifier.toString(mod), depth);
	    	}
		}
    }
    
    public void inspectFields(Class c, Object obj, boolean recursive, int depth)
    {
		// Type checking
		if (obj == null || !c.isAssignableFrom(obj.getClass()))
		{
			printWithTabs("ERROR: obj null or obj class not assignable to c in call to inspectFields", depth);
			return;
		}
		
		printWithTabs("FIELD INSPECTION ( " + c.getName() + " )", depth);
		
		Field[] fs = c.getDeclaredFields();
		
		if (fs.length == 0)
		{
			printWithTabs("None", depth);
		}
		else
		{
	    	for (Field f: fs)
	    	{
	    		printWithTabs("Field name: " + f.getName(), depth);
	    		
	    		Class fieldType = f.getType();
	    		printWithTabs(" Type: " + fieldType.getName(), depth);
	    		
	    		int mod = f.getModifiers();
	    		printWithTabs(" Modifiers: " + Modifier.toString(mod), depth);
	    		
	    		f.setAccessible(true);
	    		
	    		Object fieldObj = null;
	    		try
				{
					fieldObj = f.get(obj);
				} 
	    		catch (IllegalArgumentException | IllegalAccessException e)
				{
					e.printStackTrace();
				}
	    		
	    		if (fieldType.isPrimitive())
	    		{
	    			printWithTabs(" Value: " + fieldObj.toString(), depth);
	    		}
	    		else if (fieldObj == null)
	    		{
	    			printWithTabs(" Value: null", depth);
	    		}
	    		else if (fieldType.isArray())
	    		{
					inspectArray(fieldType, fieldObj, recursive, depth, 1);
	    		}
	    		else
	    		{
	    			if (recursive)
	    			{
	    				printWithTabs(" Value:", depth);
	    				
	    				// If recursing, don't recurse into already inspected Objects as to avoid circular references
	    				if (inspectedObjects.contains(fieldObj))
    			        {
	    				    printWithTabs(" Object already inspected", depth);
    			            return;
			            }
	    				
	    				inspectClass(fieldType, fieldObj, recursive, depth + 1);
	    			}
	    			else
	    			{
	    				printWithTabs(" Reference value: " + fieldType.getName() + "@" + Integer.toHexString(System.identityHashCode(fieldObj)) , depth);
	    			}
	    		}
	    	}
		}
    }
    
	public void inspectArray(Class c, Object obj, boolean recursive, int depth, int arrDepth)
	{
		// Type checking
		if (obj == null || !c.isArray() || !obj.getClass().isArray() || !c.isAssignableFrom(obj.getClass()))
		{
			printWithTabs("ERROR: obj null or class not an array or obj class not assignable to c in call to inspectArray", depth);
			return;
		}
		
		printWithTabsAndSpaces("ARRAY INSPECTION ( " + c.getName() + " )", depth, arrDepth);
		
		// Component type
		Class compType = c.getComponentType();
		printWithTabsAndSpaces("Component type: " + compType.getName(), depth, arrDepth);
		
		// Length
		int length = Array.getLength(obj);
		printWithTabsAndSpaces("Length: " + String.valueOf(length), depth, arrDepth);
		
		// Array Contents
		if (length > 0)
		{
			printWithTabsAndSpaces("Array contents:", depth, arrDepth);
		}
		
		for (int i = 0; i < length; i++)
		{
			Object arrayObj = Array.get(obj, i);
			if (arrayObj == null)
			{
				printWithTabsAndSpaces(i + ": null", depth, arrDepth);
			}
			else if (compType.isPrimitive())
			{
				printWithTabsAndSpaces(i + ": " + arrayObj.toString(), depth, arrDepth);
			}
			else if (compType.isArray())
			{
				printWithTabsAndSpaces(i + ": Array", depth, arrDepth);
				
				inspectArray(arrayObj.getClass(), arrayObj, recursive, depth, arrDepth + 1);
			}
			else
			{
    			if (recursive)
    			{
    				printWithTabsAndSpaces(i + ":" , depth, arrDepth);
    				
                    // If recursing, don't recurse into already inspected Objects as to avoid circular references
    		        if (inspectedObjects.contains(arrayObj))
    		        {
    		            printWithTabsAndSpaces("Object already inspected", depth, arrDepth);
    		            return;
    		        }
    				
    				inspectClass(arrayObj.getClass(), arrayObj, recursive, depth + 1);
    			}
    			else
    			{
    				printWithTabsAndSpaces(i + ": " + arrayObj.getClass().getName() + "@" + arrayObj.hashCode() , depth, arrDepth);
    			}
			}
		}
	}

    private void printWithTabs(String output, int tabs)
    {
    	printWithTabsAndSpaces(output, tabs, 0);
    }
    
    private void printWithTabsAndSpaces(String output, int tabs, int spaces)
    {
    	while(tabs-- > 0)
    	{
    		System.out.print('\t');
    	}
    	while(spaces-- > 0)
    	{
    		System.out.print(' ');
    	}
    	System.out.println(output);
    }
}
