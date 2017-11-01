package com.cmbc.acia;

import com.iostate.exia.api.AstFunction;
import com.iostate.exia.ast.AstFind;
import com.iostate.exia.ast.AstUtils;
import com.iostate.exia.ast.FindUpper;
import com.iostate.exia.ast.visitors.GenericSelector;
import com.iostate.exia.core.FileWalker;
import com.iostate.exia.samples.LogCorrecter;
import com.iostate.exia.samples.MyTest;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

import java.io.File;
import java.util.Map;

/**
 * Created by niuxinli on 17/10/30.
 */
public class RefVisit  implements AstFunction {
    public static void main(String[] args) {
        //IFile file = new
        //ICompilationUnit cu = JavaCore.createCompilationUnitFrom("/Users/niuxinli/Documents/workspace/memkv");
        FileWalker.launch(new String[]{"/Users/niuxinli/jdttest"},new RefVisit());
    }

    @Override
    public boolean doAndModify(final CompilationUnit cu, File file) {
        final TypeDeclaration type = AstUtils.tryGetConcreteType(cu);
        if (type == null) return READONLY;

        GenericSelector<MethodInvocation> logSel = new GenericSelector<MethodInvocation>() {
            @Override
            public boolean visit(MethodInvocation mi) {

               // IMethodBinding binding = mi.resolveMethodBinding();
                //if(binding != null) {
                //if(mi.toString().contains("setIfAbsent")) {

                    System.out.println("all:"+mi);
                    System.out.println("name:"+mi.getName());
                    System.out.println("expression:"+mi.getExpression());
                    if(mi.getExpression() != null) {
                        Map t = mi.getExpression().properties();
                        if (t != null) {
                            System.out.println(t);
                        }
                    }
                    //System.out.println(mi.resolveMethodBinding());
               // }
                    //System.out.println(mi);
                       // System.out.println(binding);

                //}
                return true;
            }
        };

        if (type.getMethods().length == 0) {
            return READONLY;
        }
        for (MethodDeclaration mi : type.getMethods()) {
           mi.accept(logSel);
        }

       return READONLY;
    }

    private void correctMI(MethodInvocation mi) {
        StringLiteral sl = mi.getAST().newStringLiteral();
        sl.setLiteralValue("Error occurred: ");
        mi.arguments().add(0, sl);
    }

    private boolean isOnCaught(MethodInvocation mi, TypeDeclaration type) {
        SimpleName loggerName = (SimpleName) mi.getExpression();
        Expression arg = (Expression) mi.arguments().get(0);
        if (arg instanceof SimpleName) {
            String ename = ((SimpleName) arg).getIdentifier();
            CatchClause cc = FindUpper.catchClause(mi);
            if (cc != null && cc.getException().getName().getIdentifier().equals(ename)) {
                FieldDeclaration loggerField = AstFind.findFieldByName(loggerName.getIdentifier(), type);
                if (loggerField != null && loggerField.getType().toString().equals("Logger")) {
                    return true;
                }
            }
        }

        return false;
    }
}