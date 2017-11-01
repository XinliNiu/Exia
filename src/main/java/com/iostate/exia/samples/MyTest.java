package com.iostate.exia.samples;

import com.cmbc.acia.AciaUtil;
import com.cmbc.acia.MethodInfo;
import com.iostate.exia.api.AstFunction;
import com.iostate.exia.ast.AstUtils;
import com.iostate.exia.core.FileWalker;
import org.eclipse.jdt.core.dom.*;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by niuxinli on 17/9/15.
 */
public class MyTest  implements AstFunction{
    private static Map<String,MethodDeclaration> methodMap = new ConcurrentHashMap<String,MethodDeclaration>();
    public static void main(String[] args) {


        FileWalker.launch(new String[]{"/Users/niuxinli/jdttest"},new MyTest());
    }
    @Override
    public boolean doAndModify(CompilationUnit cu, File file) {
        final TypeDeclaration type = AstUtils.tryGetConcreteType(cu);

        MethodDeclaration[] methods = type.getMethods();
        for(MethodDeclaration method : methods) {
            String fullName = AciaUtil.getFullNameFromMethodDeclaration(method);
            System.out.println(fullName);
            methodMap.put(fullName,method);
        }

        return true;
    }
}
