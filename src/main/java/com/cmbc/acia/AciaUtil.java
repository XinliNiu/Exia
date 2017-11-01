package com.cmbc.acia;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import java.util.List;

/**
 * Created by niuxinli on 17/10/30.
 */
public class AciaUtil {
    public static String getFullNameFromMethodDeclaration(MethodDeclaration method) {

        CompilationUnit c = (CompilationUnit) method.getParent().getParent();
        String fullName = c.getPackage().getName().toString();
        fullName = fullName + "--" + method.getReturnType2() + "--" + method.getName() + "--" + "(";

        List p = method.parameters();
        if(p.size() == 0) {
            fullName = fullName + ")";
        }
        for(int i = 0; i < p.size()-1; i++) {
            Object node = p.get(i);
            if(node instanceof SingleVariableDeclaration) {
                SingleVariableDeclaration v = (SingleVariableDeclaration) node;
                //System.out.print(v.getType()+",");
                fullName = fullName + v.getType() + ",";
            }
        }
        if(p.size() != 0) {
            Object node = p.get(p.size()-1);
            if(node instanceof SingleVariableDeclaration) {
                SingleVariableDeclaration v = (SingleVariableDeclaration) node;
                fullName = fullName + v.getType() + ")";
            }
        }
        return fullName;
    }
}
