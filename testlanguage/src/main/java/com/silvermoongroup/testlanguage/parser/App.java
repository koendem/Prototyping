package com.silvermoongroup.testlanguage.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Enumeration;

import com.silvermoongroup.testlanguage.ExprLang;
import com.silvermoongroup.testlanguage.ParseException;
import com.silvermoongroup.testlanguage.SimpleNode;
import com.silvermoongroup.testlanguage.utils.STC;




/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) throws ParseException, FileNotFoundException
    {
        String temp;

        if (args.length < 1)
        {
            System.out.println("Please pass in the filename.");
            System.exit(1);
        }

        ExprLang parser = new ExprLang(new FileInputStream(args[0]));

        SimpleNode root = parser.program();

        System.out.println("Abstract Syntax Tree:");

        root.dump(" ");

        System.out.println();
        System.out.println("Symbol Table:");

        Enumeration t = parser.ST.keys();

        while (t.hasMoreElements())
        {
            temp = (String)t.nextElement();

            STC temp2 = (STC)parser.ST.get(temp);
            System.out.println(temp);
            if (temp2.type != null)
            {
                System.out.println(" type = " + temp2.type);
            }
            if (temp2.value != null)
            {
                System.out.println(" value = " + temp2.value);
            }
        }

        System.out.println();
        System.out.println("Program:");
        PrintVisitor pv = new PrintVisitor();
        root.jjtAccept(pv, null);

        System.out.println();
        System.out.println("Type Checking:");
        TypeCheckVisitor tc = new TypeCheckVisitor();
        root.jjtAccept(tc, parser.ST);
    }
}
