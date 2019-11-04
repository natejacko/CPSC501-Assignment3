import java.util.ArrayList;
import java.util.Scanner;

public class ObjectCreator
{   
    public static Object useObjectCreator()
    {
        printObjectOptions();
        return getObject(getObjectOption());
    }
    
    private static void printObjectOptions()
    {
        System.out.println("Select one of the following objects to create:");
        System.out.println("(1) Object with primitive fields");
        System.out.println("(2) Object with reference to another object");
        System.out.println("(3) Two objects with circular reference to one another");
        System.out.println("(4) Object with array of a ints or doubles");
        System.out.println("(5) Object with array of references");
        System.out.println("(6) Object with java.util.ArrayList of references");
        System.out.println("(7) Null object");
        System.out.print("Option (1, 2, 3, 4, 5, 6, 7): ");
    }
    
    private static char getObjectOption()
    {
        Scanner scan = new Scanner(System.in);
        while(true)
        {
            char option = scan.nextLine().charAt(0);
            switch(option)
            {
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    return option;
                default:
                    System.out.println("Invalid option!");
                    printObjectOptions();
            }
        }
    }
    
    private static Object getObject(char option)
    {
        Object obj = null;
        switch(option)
        {
            case '1':
                obj = createPrimitivesObject();
                break;
            case '2':
                obj = createReferenceObject();
                break;
            case '3':
                obj = createCircularReferenceObject();
                break;
            case '4':
                obj = createArrayOfPrimitivesObject();
                break;
            case '5':
                obj = createArrayOfReferencesObject();
                break;
            case '6':
                obj = createArrayListOfReferencesObject();
                break;
        }
        return obj;
    }
    
    private static boolean getBoolean()
    {
        Scanner scan = new Scanner(System.in);
        boolean bool = false;
        while(true)
        {
            System.out.print("Boolean value (true/false): ");
            String option = scan.nextLine();
            if (option.equals(""))
            {
                break;
            }
            else if (option.equals("true") || option.equals("false"))
            {
                bool = Boolean.parseBoolean(option);
                break;
            }
            System.out.println("Invalid boolean.");
        }
        return bool;
    }
    
    private static int getInt()
    {
        Scanner scan = new Scanner(System.in);
        int i = 0;
        while(true)
        {
            System.out.print("Int value: ");
            String option = scan.nextLine();
            if (option.equals(""))
            {
                break;
            }
            try
            {
                i = Integer.parseInt(option);
                break;
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid int.");
            }
        }
        return i;
    }
    
    private static float getFloat()
    {
        Scanner scan = new Scanner(System.in);
        float f = 0;
        while(true)
        {
            System.out.print("Float value: ");
            String option = scan.nextLine();
            if (option.equals(""))
            {
                break;
            }
            try
            {
                f = Float.parseFloat(option);
                break;
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid float.");
            }
        }
        return f;
    }
    
    private static double getDouble()
    {
        Scanner scan = new Scanner(System.in);
        double d = 0;
        while(true)
        {
            System.out.print("Double value: ");
            String option = scan.nextLine();
            if (option.equals(""))
            {
                break;
            }
            try
            {
                d = Double.parseDouble(option);
                break;
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid double.");
            }
        }
        return d;
    }
    
    private static char getChar()
    {
        Scanner scan = new Scanner(System.in);
        char c = 'a';
        while(true)
        {
            System.out.print("Char value (default will be 'a'): ");
            String option = scan.nextLine();
            if (option.equals(""))
            {
                break;
            }
            else if (option.length() == 1)
            {
                c = option.charAt(0);
                break;
            }
            System.out.println("Invalid char.");
        }
        return c;
    }
    
    private static int getLength()
    {
        Scanner scan = new Scanner(System.in);
        int length = 0;
        while(true)
        {
            if (scan.hasNextInt())
            {
                length = scan.nextInt();
                if (length > 0)
                {
                    break;
                }
            }
            scan.nextLine();
            System.out.print("Invalid length.\nLength: ");
        }
        return length;
    }
    
    private static Object createPrimitivesObject()
    {
        System.out.println("\nTo create an object with primitive fields, please specify the value for each of the following primitives.");
        System.out.println("Press <enter> to leave as default value.");
        
        Primitives primObj = new Primitives();
        primObj.bool = getBoolean();
        primObj.i = getInt();
        primObj.f = getFloat();
        primObj.d = getDouble();
        primObj.c = getChar();
        return primObj;
    }
    
    private static Object createReferenceObject()
    {
        System.out.println("\nTo create an object with reference to another object, please create a new object");
        printObjectOptions();
        Reference obj = new Reference();
        obj.referenceObject = getObject(getObjectOption());
        return obj;
    }
    
    private static Object createCircularReferenceObject()
    {
        System.out.println("\nTo create two objects with circular reference to one another, please specfiy the id of object one");
        CircularReference obj1 = new CircularReference();
        obj1.id = getInt();
        System.out.println("Specify the id of object two");
        CircularReference obj2 = new CircularReference();
        obj2.id = getInt();
        obj1.partner = obj2;
        obj2.partner = obj1;
        return obj1;
    }
    
    private static Object createArrayOfPrimitivesObject()
    {
        System.out.println("\nTo create an object with an array of primitives, please specify a primitive type.");
        Scanner scan = new Scanner(System.in);
        String option = "";
        boolean invalidOption = true;
        while (invalidOption)
        {
            System.out.println("Types: int, double");
            System.out.print("Type: ");
            switch(option = scan.nextLine())
            {
                case "int":
                case "double":
                    invalidOption = false;
                    break;
               default:
                   System.out.println("Invalid option.");
            }
        }
        
        System.out.println("Please specify an array size");
        System.out.print("Length: ");
        int length = getLength();
        
        Object arrayPrimObj = null;
        switch(option)
        {
            case "int":
                arrayPrimObj = new ArrayOfInts();
                ((ArrayOfInts) arrayPrimObj).array = new int[length];
                break;
            case "double":
                arrayPrimObj = new ArrayOfDoubles();
                ((ArrayOfDoubles) arrayPrimObj).array = new double[length];
                break;
        }
        
        for (int i = 0; i < length; i++)
        {
            System.out.println("\nCreate a new primitive for index " + i);
            System.out.println("Press <enter> to leave as default value.");
            switch(option)
            {
                case "int":
                    ((ArrayOfInts) arrayPrimObj).array[i] = getInt();
                    break;
                case "double":
                    ((ArrayOfDoubles) arrayPrimObj).array[i] = getDouble();
                    break;
            }
        }
        
        return arrayPrimObj;
    }
    
    private static Object createArrayOfReferencesObject()
    {
        System.out.println("\nTo create an object with an array of references, please specify an array size");
        System.out.print("Length: ");
        Scanner scan = new Scanner(System.in);
        int length = getLength();
        
        ArrayOfReferences arrayRefObj = new ArrayOfReferences();
        arrayRefObj.references = new Object[length];
        
        for (int i = 0; i < length; i++)
        {
            System.out.println("\nCreate a new object for index " + i);
            printObjectOptions();
            arrayRefObj.references[i] = getObject(getObjectOption());
        }
        
        return arrayRefObj;
    }
    
    private static Object createArrayListOfReferencesObject()
    {
        System.out.println("\nTo create an object with java.util.ArrayList of references, continually create new objects.");
        ArrayListOfReferences arraylistRefObj = new ArrayListOfReferences();
        arraylistRefObj.references = new ArrayList<Object>();
        Scanner scan = new Scanner(System.in);
        String option = "";
        while (true)
        {
            System.out.println("Type next to create a new object or <enter> to finish");
            option = scan.nextLine();
            if (option.equals("next"))
            {
                System.out.println("\nCreate a new object");
                printObjectOptions();
                arraylistRefObj.references.add(getObject(getObjectOption()));
                continue;
            }
            else if (option.equals(""))
            {
                break;
            }
            System.out.println("Invalid option.");
        }
        return arraylistRefObj;
    }
    

}
