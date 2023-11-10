package tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

// Command-line tool for generating the Abstract-Syntax-Tree for Java Lox.
public class GenerateAst 
{
    public static void main(String[] args) throws IOException
    {
        if (args.length != 1)
        {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];

        defineAst(outputDir, "Expr", 
            Arrays.asList(
                "Binary : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal : Object value",
                "Unary : Token operator, Expr right"
        ));
    }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException
    {
        String path = outputDir + "/" + baseName + ".java"; // Hope that Java autotranslates this to Windows PATH format.
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package Lox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName);
        writer.println("{");

        // Insert the AST classes.
        for (String type : types)
        {
            String[] splitType = type.split(":");
            String className = splitType[0].trim();
            String fields = splitType[1].trim();
            defineType(writer, baseName, className, fields);
        }

        writer.println("}");
        writer.close();
    }
    
    private static void defineType(PrintWriter writer, String baseName, String className, String fieldList)
    {
        writer.println("    static class " + className + " extends " + baseName);
        writer.println("    {");

        // Constructor print:
        writer.println("        " + className + "(" + fieldList + ")");
        writer.println("        {");

        // Stores the parameters in the fields.
        String[] fields = fieldList.split(", ");
        for (String field : fields)
        {
            String name = field.split(" ")[1];
            writer.println("            this." + name + " = " + name + ";");
        }
        writer.println("        }"); // Encapsulates the parameter storing.

        // Fields.
        writer.println();
        for(String field : fields)
        {
            writer.println("        final " + field + ";");
        }

        writer.println("    }"); // Encapsulate the constructor.
        writer.println();
    }
}
