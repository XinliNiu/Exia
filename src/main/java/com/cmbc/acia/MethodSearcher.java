package com.cmbc.acia;

import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.internal.resources.WorkspaceRoot;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.core.ExternalPackageFragmentRoot;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;

/**
 * Created by niuxinli on 17/10/31.
 */
public class MethodSearcher {
    public static void main(String[] args) {

        //IFolder folder = new Folder("/Users/niuxinli/Documents/workspace/memkv");
        IJavaElement pro = JavaCore.create("/Users/niuxinli/Documents/workspace/memkv");
        IJavaProject p = (IJavaProject) pro;

        //IPath path = new Path("/");
       // IPackageFragmentRoot pack = null; //new PackageFragmentRoot(path,null);
       // IJavaSearchScope sco = SearchEngine.createJavaSearchScope(new IJavaElement[] {pack});
    }
}
