package com.iostate.exia.classic.sg.visitors;


import com.iostate.exia.ast.FindUpper;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;


/*
 * WARNING: It deletes the whole Statement that contains your target MethodInvocation, be aware when you use it. 
 * 
 * Deletes the upper statement: find parent, if parent is not Statement, find ancestor
 */
public class InvocationStatementDeletor extends ASTVisitor {
	private String methodName;
	
	public InvocationStatementDeletor(String name) {
		super(true);
		methodName = name;
	}
	
	@Override
	public boolean visit(MethodInvocation node) {
		if (node.getName().getIdentifier().equals(methodName)) {
			Statement upper = FindUpper.statement(node);
			if (upper != null) {
				upper.delete();
			} else {
				throw new RuntimeException("Not supported condition!");
			}
		}

		return true;
	}
}
