package com.cmbc.acia;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import java.util.List;
/**
 * Created by niuxinli on 17/10/1.
 */
public class MethodInfo {
    ClassInfo clazz;
    MethodDeclaration method;
    String fullName ;
    String body;
    List<String> refs;
}
