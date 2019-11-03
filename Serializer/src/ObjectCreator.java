import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jdom2.Document;
import org.jdom2.output.Format;
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
        System.out.println("(3) Object with array of a primitive");
        System.out.println("(4) Object with array of references");
        System.out.println("(5) Object with java.util.ArrayList of references");
        System.out.println("(6) Null object");
        System.out.print("Option (1,2,3,4,5,6): ");
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
                obj = createArrayOfPrimitivesObject();
                break;
            case '4':
                obj = createArrayOfReferencesObject();
                break;
            case '5':
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
    
    private static byte getByte()
    {
        Scanner scan = new Scanner(System.in);
        byte b = 0;
        while(true)
        {
            System.out.print("Byte value (-128 to 127): ");
            String option = scan.nextLine();
            if (option.equals(""))
            {
                break;
            }
            try
            {
                b = Byte.parseByte(option);
                break;
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid byte.");
            }
        }
        return b;
    }
    
    private static short getShort()
    {
        Scanner scan = new Scanner(System.in);
        short s = 0;
        while(true)
        {
            System.out.print("Short value: ");
            String option = scan.nextLine();
            if (option.equals(""))
            {
                break;
            }
            try
            {
                s = Short.parseShort(option);
                break;
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid short.");
            }
        }
        return s;
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
    
    private static long getLong()
    {
        Scanner scan = new Scanner(System.in);
        long l = 0;
        while(true)
        {
            System.out.print("Long value: ");
            String option = scan.nextLine();
            if (option.equals(""))
            {
                break;
            }
            try
            {
                l = Long.parseLong(option);
                break;
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid long.");
            }
        }
        return l;
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
        System.out.println("To create an object with primitive fields, please specify the value for each of the following primitives.");
        System.out.println("Press <enter> to leave as default value.");
        
        Primitives primObj = new Primitives();
        primObj.bool = getBoolean();
        primObj.b = getByte();
        primObj.s = getShort();
        primObj.i = getInt();
        primObj.l = getLong();
        primObj.f = getFloat();
        primObj.d = getDouble();
        primObj.c = getChar();
        return primObj;
    }
    
    private static Object createReferenceObject()
    {
        System.out.println("To create an object with reference to another object, please create a new object");
        printObjectOptions();
        Reference obj = new Reference();
        obj.referenceObject = getObject(getObjectOption());
        return obj;
    }
    
    private static Object createArrayOfPrimitivesObject()
    {
        System.out.println("To create an object with an array of primitives, please specify a primitive type.");
        Scanner scan = new Scanner(System.in);
        String option = "";
        boolean invalidOption = true;
        while (invalidOption)
        {
            System.out.println("Types: boolean, byte, short, int, long, float, double, char");
            System.out.print("Type: ");
            switch(option = scan.nextLine())
            {
                case "boolean":
                case "byte":
                case "short":
                case "int":
                case "long":
                case "float":
                case "double":
                case "char":
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
            case "boolean":
                arrayPrimObj = new ArrayOfBooleans();
                ((ArrayOfBooleans) arrayPrimObj).array = new boolean[length];
                break;
            case "byte":
                arrayPrimObj = new ArrayOfBytes();
                ((ArrayOfBytes) arrayPrimObj).array = new byte[length];
                break;
            case "short":
                arrayPrimObj = new ArrayOfShorts();
                ((ArrayOfShorts) arrayPrimObj).array = new short[length];
                break;
            case "int":
                arrayPrimObj = new ArrayOfInts();
                ((ArrayOfInts) arrayPrimObj).array = new int[length];
                break;
            case "long":
                arrayPrimObj = new ArrayOfLongs();
                ((ArrayOfLongs) arrayPrimObj).array = new long[length];
                break;
            case "float":
                arrayPrimObj = new ArrayOfFloats();
                ((ArrayOfFloats) arrayPrimObj).array = new float[length];
                break;
            case "double":
                arrayPrimObj = new ArrayOfDoubles();
                ((ArrayOfDoubles) arrayPrimObj).array = new double[length];
                break;
            case "char":
                arrayPrimObj = new ArrayOfChars();
                ((ArrayOfChars) arrayPrimObj).array = new char[length];
                break;
        }
        
        for (int i = 0; i < length; i++)
        {
            System.out.println("Create a new primitive for index " + i);
            System.out.println("Press <enter> to leave as default value.");
            switch(option)
            {
                case "boolean":
                    ((ArrayOfBooleans) arrayPrimObj).array[i] = getBoolean();
                    break;
                case "byte":
                    ((ArrayOfBytes) arrayPrimObj).array[i] = getByte();
                    break;
                case "short":
                    ((ArrayOfShorts) arrayPrimObj).array[i] = getShort();
                    break;
                case "int":
                    ((ArrayOfInts) arrayPrimObj).array[i] = getInt();
                    break;
                case "long":
                    ((ArrayOfLongs) arrayPrimObj).array[i] = getLong();
                    break;
                case "float":
                    ((ArrayOfFloats) arrayPrimObj).array[i] = getFloat();
                    break;
                case "double":
                    ((ArrayOfDoubles) arrayPrimObj).array[i] = getDouble();
                    break;
                case "char":
                    ((ArrayOfChars) arrayPrimObj).array[i] = getChar();
                    break;
            }
        }
        
        return arrayPrimObj;
    }
    
    private static Object createArrayOfReferencesObject()
    {
        System.out.println("To create an object with an array of references, please specify an array size");
        System.out.print("Length: ");
        Scanner scan = new Scanner(System.in);
        int length = getLength();
        
        ArrayOfReferences arrayRefObj = new ArrayOfReferences();
        arrayRefObj.references = new Object[length];
        
        for (int i = 0; i < length; i++)
        {
            System.out.println("Create a new object for index " + i);
            printObjectOptions();
            arrayRefObj.references[i] = getObject(getObjectOption());
        }
        
        return arrayRefObj;
    }
    
    private static Object createArrayListOfReferencesObject()
    {
        System.out.println("To create an object with java.util.ArrayList of references, continually create new objects.");
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
                System.out.println("Create a new object");
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
