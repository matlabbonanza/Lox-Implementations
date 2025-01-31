BUILD_DIR := build

default: jlox

# Remove all build outputs and intermediate files.
clean:
	@ ECHO Cleaning!
	@ rm "$(BUILD_DIR)" -r

# Compile and run the AST generator.
generate_ast:
	@ $(MAKE) -f utils/java.make DIR=java PACKAGE=tool
	@ java -cp build\java tool.GenerateAst \
			java/Lox

# Compile the Java interpreter .java files to .class files.
jlox: generate_ast
	@ $(MAKE) -f utils/java.make DIR=java PACKAGE=lox

run_generate_ast = @ java -cp build/gen/$(1) \
			com.craftinginterpreters.tool.GenerateAst \
			gen/$(1)/com/craftinginterpreters/lox

.PHONY: jlox clean default