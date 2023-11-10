BUILD_DIR := build

default: jlox

# Remove all build outputs and intermediate files.
clean:
	@ ECHO Cleaning!
	@ rd /s /q "$(BUILD_DIR)""

# Compile and run the AST generator.
generate_ast:
	@ echo Am in GenerateAst
	@ $(MAKE) -f utils/java.make DIR=java PACKAGE=tool
	@ echo Am now here
	@ java -cp .build\java Lox

# Compile the Java interpreter .java files to .class files.
jlox:
	@ echo am in jlox
	@ $(MAKE) -f utils/java.make DIR=java PACKAGE=lox

run_generate_ast = @ java -cp build/gen/$(1) \
			com.craftinginterpreters.tool.GenerateAst \
			gen/$(1)/com/craftinginterpreters/lox

.PHONY: jlox clean default