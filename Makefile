BUILD_DIR = bin
SRC_DIR = src

JAVAC = javac
JAVA = java
CLASS_PATH_FLAG = -cp
TARGET_DIR_FLAG = -d

ALL_SRC = $(shell find $(SRC_DIR) -name '*.java')

.PHONY: compile editor

compile:
	$(JAVAC) $(CLASS_PATH_FLAG) $(SRC_DIR) $(TARGET_DIR_FLAG) $(BUILD_DIR) $(ALL_SRC)

editor: compile
	$(JAVA) $(CLASS_PATH_FLAG) $(BUILD_DIR)


