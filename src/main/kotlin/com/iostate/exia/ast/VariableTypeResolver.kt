package com.iostate.exia.ast

import org.eclipse.jdt.core.dom.*

class VariableTypeResolver(varSymbol: String, private val minScope: ASTNode) {
  private val symbol: String = varSymbol

  private var typeLevel = true

  /**
   * The found result
   */
  private var declName: SimpleName? = null

  private val visitor = object : ASTVisitor() {
    override fun visit(sn: SimpleName): Boolean {
      if (found()) {
        return false
      }
      if (sn.identifier == symbol && sn.parent is VariableDeclaration) {
        declName = sn
        return false
      }
      return true
    }
  }

  /**
   * Starts resolving with the requested symbol
   * @param varSymbolNode the variable symbol node to resolve (node must be in the AST)
   */
  constructor(varSymbolNode: SimpleName) : this(varSymbolNode.identifier, varSymbolNode)

  fun disableTypeLevel(): VariableTypeResolver {
    typeLevel = false
    return this
  }

  /**
   * Node's parent is instance of [VariableDeclarationFragment] or [SingleVariableDeclaration]
   * @return the SimpleName node of declaration
   */
  fun resolveDeclSimpleName(): SimpleName? {
    if (!found()) {
      resolve()
    }
    return declName
  }

  fun resolveType(): Type {
    val declSN = resolveDeclSimpleName() ?: throw RuntimeException("Cannot resolve decl for: " + symbol)
    val maybeFrag = declSN.parent
    val varDecl: ASTNode
    if (maybeFrag is VariableDeclarationFragment) {
      varDecl = maybeFrag.getParent()
    } else if (maybeFrag is SingleVariableDeclaration) {
      varDecl = maybeFrag
    } else {
      throw RuntimeException(maybeFrag.toString())
    }
    val props = varDecl.structuralPropertiesForType() as List<StructuralPropertyDescriptor>
    return props.filter({ p -> p.isChildProperty() && p.getId().equals("type") })
        .map { p ->
          varDecl.getStructuralProperty(p) as Type
        }.first()
  }

  fun resolveTypeQname(): String {
    val type = resolveType()
    return AstFind.qnameOfTypeRef(type.toString().trim { it <= ' ' }, FindUpper.cu(type))
  }

  private fun resolve() {
    if (found()) {
      return
    }

    applyLocal(minScope)

    if (found()) {
      return
    }

    if (typeLevel) {
      val typeScope = FindUpper.abstractTypeScope(minScope)
      applyInFields(typeScope, false)

      if (found()) {
        return
      }

      for (superClass in superClasses(typeScope)) {
        if (found()) {
          return
        }
        applyInFields(superClass, true)
      }
    }
  }

  private fun found(): Boolean {
    return declName != null
  }

  private fun applyLocal(node: ASTNode) {
    var node = node
    if (node is Block) {
      node.accept(visitor)
    }
    while (!found()) {
      val outer = FindUpper.scoper(node, Block::class.java) ?: break
      node = outer
      node.accept(visitor)
    }
    if (!found()) {
      val md = FindUpper.methodScope(node)
      if (md != null) {
        val parameters = md.parameters()
        for (parameter in parameters) {
          (parameter as SingleVariableDeclaration).accept(visitor)
        }
      }
    }
  }

  private fun applyScope(scope: ASTNode?) {
    if (scope == null) {
      throw NullPointerException()
    }
    scope.accept(visitor)
  }

  private fun applyInFields(typeScope: AbstractTypeDeclaration, isSuper: Boolean) {
    for (bd in typeScope.bodyDeclarations()) {
      if (bd is FieldDeclaration) {
        if (isSuper && AstFind.hasModifierKeyword(bd.modifiers(),
            Modifier.ModifierKeyword.PRIVATE_KEYWORD)) {//TODO handle package-private
          // private fields in super classes are invisible
          continue
        }
        applyScope(bd as ASTNode)
      }
    }
  }

  private fun superClasses(atd: AbstractTypeDeclaration): List<TypeDeclaration> {
    if (atd is TypeDeclaration) {
      return AstFind.superClasses(atd)
    } else {
      return emptyList()
    }
  }
}
