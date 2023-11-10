# Makefile for building a single directory of Java source files. It requires
# a DIR variable to be set.
# printf "%8s %-60s %s\n" javac $< "$(JAVA_OPTIONS)"

BUILD_DIR := build

SOURCES := $(wildcard $(DIR)/Lox/*.java)
CLASSES := $(addprefix $(BUILD_DIR)/, $(SOURCES:.java=.class))

JAVA_OPTIONS := -Werror

default: $(CLASSES)
	@: # Don't show "Nothing to be done" output.

# Compile a single .java file to .class.
$(BUILD_DIR)/$(DIR)/%.class: $(DIR)/%.java
	@echo am in java.make and is going to run: javac -cp $(DIR) -d $(BUILD_DIR)/$(DIR) $(JAVA_OPTIONS) -implicit:none $<
	@ if not exist ".\$(BUILD_DIR)\$(DIR)" mkdir .\$(BUILD_DIR)\$(DIR)
	@ javac -cp $(DIR) -d $(BUILD_DIR)\$(DIR) $(JAVA_OPTIONS) -implicit:none $<

.PHONY: default