package com.silvermoongroup;

import java.io.Reader;
import java.io.StringReader;

import com.silvermoongroup.navexpressionparser.ExprParser;
import com.silvermoongroup.navexpressionparser.ParseException;
import com.silvermoongroup.navexpressionparser.SimpleNode;


public class NavExrParserTestApp
{
    public static void main( String[] args ) throws ParseException {
        String[] tests = {"\"This is a literal\"",
                          "kind1",
                       //   "kind1.'insured object'.value",
                          "kind2.'insured person';defaultName.firstName"};

        for(int i=0; i< tests.length; i++){
            Reader stringReader = new StringReader(tests[i]);

            ExprParser parser = new ExprParser(stringReader);
            SimpleNode root = parser.navPathExpression();

            System.out.println("Printing AST for : "+tests[i]);
            root.dump(" ");
        }



    }
}
