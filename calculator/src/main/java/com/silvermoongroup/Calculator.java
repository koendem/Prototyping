package com.silvermoongroup;

import com.silvermoongroup.calculatorgrammar.Arithmetic;
import com.silvermoongroup.calculatorgrammar.ParseException;

/**
 * Hello world!
 *
 */
public class Calculator
{
    public static void main( String[] args ) throws ParseException {
        System.out.println( "Hello this is the calculator : enter an arithmetic expression. press Ctrl Z to exit" );
        Arithmetic parser = new Arithmetic(System.in);
        while (true)
        {
            parser.parseOneLine();
        }
    }
}
