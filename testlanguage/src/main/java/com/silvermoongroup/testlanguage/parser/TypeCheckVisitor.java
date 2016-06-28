// Name:TypeCheckVisitor.java
// Author: David Sinclair      Date: 29 Aug 2012
//
// Visitor for basic type checking expressions in an abstract syntax tree in the ExprLang language
//
package com.silvermoongroup.testlanguage.parser;

import com.silvermoongroup.testlanguage.ASTAdd_op;
import com.silvermoongroup.testlanguage.ASTBool_op;
import com.silvermoongroup.testlanguage.ASTDecl;
import com.silvermoongroup.testlanguage.ASTExp;
import com.silvermoongroup.testlanguage.ASTMult_op;
import com.silvermoongroup.testlanguage.ASTNot_op;
import com.silvermoongroup.testlanguage.ASTStms;
import com.silvermoongroup.testlanguage.ASTidentifier;
import com.silvermoongroup.testlanguage.ASTnumber;
import com.silvermoongroup.testlanguage.ASTprogram;
import com.silvermoongroup.testlanguage.ExprLangVisitor;
import com.silvermoongroup.testlanguage.SimpleNode;
import com.silvermoongroup.testlanguage.utils.DataType;
import com.silvermoongroup.testlanguage.utils.STC;


import java.util.*;

public class TypeCheckVisitor implements ExprLangVisitor
{

	public Object visit(SimpleNode node, Object data) {
		throw new RuntimeException("Visit SimpleNode");
	}

	public Object visit(ASTprogram node, Object data)
    {
	node.jjtGetChild(0).jjtAccept(this, data);
	return DataType.Program;
    }

    public Object visit(ASTDecl node, Object data)
    {
	return DataType.Declaration;
    }

    public Object visit(ASTStms node, Object data)
    {
	PrintVisitor pv = new PrintVisitor();

	if ((DataType)node.jjtGetChild(0).jjtAccept(this, data) == DataType.TypeUnknown)
	    {
		System.out.print("Type error: ");
		node.jjtGetChild(0).jjtAccept(pv, null);
		System.out.println();
	    }

	return (node.jjtGetChild(1).jjtAccept(this, data));
    }

    public Object visit(ASTAdd_op node, Object data)
    {
	if (((DataType)node.jjtGetChild(0).jjtAccept(this, data) == DataType.TypeInteger)
            && ((DataType)node.jjtGetChild(1).jjtAccept(this, data) == DataType.TypeInteger))
	    return DataType.TypeInteger;
	else
	    return DataType.TypeUnknown;
    }

    public Object visit(ASTBool_op node, Object data)
    {
	if (((DataType)node.jjtGetChild(0).jjtAccept(this, data) == DataType.TypeBoolean)
            && ((DataType)node.jjtGetChild(1).jjtAccept(this, data) == DataType.TypeBoolean))
	    return DataType.TypeBoolean;
	else
	    return DataType.TypeUnknown;
    }

    public Object visit(ASTMult_op node, Object data)
    {
	if (((DataType)node.jjtGetChild(0).jjtAccept(this, data) == DataType.TypeInteger)
            && ((DataType)node.jjtGetChild(1).jjtAccept(this, data) == DataType.TypeInteger))
	    return DataType.TypeInteger;
	else
	    return DataType.TypeUnknown;
    }

    public Object visit(ASTNot_op node, Object data)
    {
	if ((DataType)node.jjtGetChild(0).jjtAccept(this, data) != DataType.TypeBoolean)
	    return DataType.TypeUnknown;
	else
	    return DataType.TypeBoolean;
    }

    public Object visit(ASTExp node, Object data)
    {
	return(node.jjtGetChild(0).jjtAccept(this, data));
    }

    public Object visit(ASTidentifier node, Object data)
    {
	Hashtable ST = (Hashtable) data;
	STC hashTableEntry;

        hashTableEntry = (STC)ST.get(node.jjtGetValue());
	if (hashTableEntry.type == "Int")
	    {
		return DataType.TypeInteger;
	    }
	else if (hashTableEntry.type == "Bool")
	    {
		return DataType.TypeBoolean;
	    }
	else
	    {
		return DataType.TypeUnknown;
	    }
    }

    public Object visit(ASTnumber node, Object data)
    {
	return DataType.TypeInteger;
    }
}