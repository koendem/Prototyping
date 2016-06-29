package com.silvermoongroup;

import java.io.Reader;
import java.io.StringReader;

import com.silvermoongroup.navexpressionparser.ExprParser;
import com.silvermoongroup.navexpressionparser.ParseException;
import com.silvermoongroup.navexpressionparser.SimpleNode;

/**
 * Hello world!
 *
 */
public class NavExrParserTestApp
{
    public static void main( String[] args ) throws ParseException {
        String testLiteral = "\"This is a literal\"";
        String kindNavPath1 = "kind1";
        String kindNavPath2 = "kind1.'insured object'.value";

        Reader stringReader = new StringReader(testLiteral);

        ExprParser parser = new ExprParser(stringReader);
        SimpleNode root = parser.navPathExpression();

        System.out.println("Printing AST for : "+testLiteral);
        root.dump(" ");

    }
}
