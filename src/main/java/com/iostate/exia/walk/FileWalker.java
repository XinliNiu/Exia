package com.iostate.exia.walk;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.iostate.exia.io.FileUtil;
import com.iostate.exia.util.MyLogger;
import com.iostate.exia.core.CuBase;
import org.eclipse.jdt.core.dom.CompilationUnit;


public class FileWalker {
  
  private final MyLogger logger = MyLogger.getLogger(getClass());
  
  public static String[] projects;
  
  private static final ThreadLocal<File> currentFile = new ThreadLocal<File>();
  public static File getCurrentFile() {return currentFile.get();}
  
  /**
   * root folders to start scan from
   */
  private final String[] roots;
  
  private final FileFilter filter;
  
  private final AstFunction function;
  
  private final ConcurrentLinkedQueue<File> files = new ConcurrentLinkedQueue<File>(); 
  
  public static void launch(String[] roots, FileFilter filter, AstFunction function) {
    new FileWalker(roots, filter, function).walk();
  }
  
  public FileWalker(String[] roots, FileFilter filter, AstFunction function) {
    this.roots = roots;
    this.filter = filter;
    this.function = function;
    projects = roots;
  }

  public void walk() {
    long start = System.currentTimeMillis();
    for (String root : roots) {
      File rootDir = new File(root);
      if (!rootDir.isDirectory()) {
        throw new RuntimeException("Wrong with "+root);
      }
      goThrough(rootDir);
    }
    processAllFiles();
    
    long end = System.currentTimeMillis();
    System.out.println("Time cost: " + (end-start)/1000 + "s");
  }
  
  private void goThrough(File file) {
//    if (!file.exists()) {
//      logger.log("File not exist: " + file.getPath());
//      return;
//    }
    if (file.isDirectory()) {
      for (File child : file.listFiles()) {
        goThrough(child);
      }
    }
    else if (filter.accept(file)) {
      files.offer(file);
    }
  }
  
  private void processAllFiles() {
    int usableCores = Runtime.getRuntime().availableProcessors();
    usableCores = usableCores > 2 ? usableCores-1 : usableCores;
    ExecutorService es = Executors.newFixedThreadPool(usableCores);
    for (int i=0; i<usableCores; i++) {
      es.execute(new Runnable() {
        @Override
        public void run() {
          while (true) {
            File file = files.poll();
            if (file == null)
              break;
            processFile(file);
          }
          currentFile.remove();
        }
      });
    }

    es.shutdown();
    try {
      es.awaitTermination(10, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private void processFile(File file) {
    currentFile.set(file);
    CompilationUnit cu = CuBase.getCuByPath(file.getPath());
    boolean modified;
    try {
      modified = function.doAndModify(cu, file);
    } catch (RuntimeException e) {
      logger.log("Error at " + file.getPath());
      throw e;
    }
    if (modified) {
      logger.log("Write " + file.getPath());
      String content = CuBase.rewriteSource(file.getPath());
      FileUtil.write(file, content);
    }
    CuBase.unloadAnyFile(file.getPath());
  }
}
