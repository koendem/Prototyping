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
                "policyholder;instanceOf('Party')",
                "kind1",
                "kind1.'insured object'.value",
                "kind2.'insured person';defaultName.firstName",
                "kind2.all",
                "'Business Extension'.all.'Limit of Liability'",
                "topLevelAgreement.children(Coverage).Premium",
                "topLevelAgreement.descendants(Coverage).Premium",
                "'insured person';defaultName",
                "topLevelAgreement.'insured person';defaultName.firstName.startDate",
                "request"
        };


        for(int i=0; i< tests.length; i++){
            Reader stringReader = new StringReader(tests[i]);

            ExprParser parser = new ExprParser(stringReader);
            SimpleNode root = parser.navPathExpression();

            System.out.println("Printing AST for : "+tests[i]);
            root.dump(" ");
        }
    }
}
