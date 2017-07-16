Exia
====

Proved in 2 million lines of code: a tooling framework for automatic analysis and modification on large codebases.

Supports Java 6/7.

### Usage

Build: gradle shadowJar

Run: `java -jar build/libs/exia-2.0-all.jar [class-name] [project-paths...]`

For example: `java -jar build/libs/exia-2.0-all.jar UnusedImportDeletor /home/sorra/projects/`

----

To learn how to write your own program, please see the package `com.iostate.exia.samples`.

There are samples showing you:
1. Detect the places you have forgotten to write `logger.isDebugEnabled()`
2. Fix a type of misuse of `logger.error()` API
3. Remove unused imports

----

There are two extension points: `FileFilter` & `AstFunction`.
Implement them and call `FileWalker` to run your own operations.  
(`com.iostate.exia.api.JavaSourceFileFilter` is ready for use)

The package `com.iostate.exia.ast` provides convenient AST utilities.

### What's new in 2.0?

- Refactored with Gradle and Kotlin.
- Will add more useful samples.
- Will add intelligence.
