// Name:STC.java
// Author: David Sinclair      Date: 29 Aug 2012
//
// Very basic Symbol Table implementation
//
package com.silvermoongroup.testlanguage.utils;

public class STC extends Object
{
    public String type;
    public String value;

    public STC(String itype, String ivalue)
    {
	type = itype;
	value = ivalue;
    }
}