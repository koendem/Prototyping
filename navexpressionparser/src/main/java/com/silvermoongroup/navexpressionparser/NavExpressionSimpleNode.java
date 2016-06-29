package com.silvermoongroup.navexpressionparser;

/**
 * Created by koen on 29.06.16.
 */
public abstract class NavExpressionSimpleNode extends SimpleNode {


    public NavExpressionSimpleNode(int i) {
        super(i);
    }

    public NavExpressionSimpleNode(ExprParser p, int i) {
        super(p, i);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ExprParserTreeConstants.jjtNodeName[id]);
        if (jjtGetValue()!= null){
            sb.append(" -- value: ");
            sb.append(jjtGetValue());
        }
        return sb.toString();
    }
}
