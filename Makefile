BUILD_DIR = bin
SRC_DIR = src

JAVAC = javac
JAVA = java
CLASS_PATH_FLAG = -cp
TARGET_DIR_FLAG = -d

TEXT_EDITOR_CLASS = utils.editor.TextEditor
SERVER_CLASS = com.Server.Server
CLIENT_CLASS = com.Client.Client

RM = rm -rf

ALL_SRC = $(shell find $(SRC_DIR) -name '*.java')

.PHONY: compile editor server client clean

compile:
	$(JAVAC) $(CLASS_PATH_FLAG) $(SRC_DIR) $(TARGET_DIR_FLAG) $(BUILD_DIR) $(ALL_SRC)

editor: compile
	$(JAVA) $(CLASS_PATH_FLAG) $(BUILD_DIR) $(TEXT_EDITOR_CLASS)

server: compile
	$(JAVA) $(CLASS_PATH_FLAG) $(BUILD_DIR) $(SERVER_CLASS)

client: compile
	$(JAVA) $(CLASS_PATH_FLAG) $(BUILD_DIR) $(CLIENT_CLASS)

clean:
	$(RM) $(BUILD_DIR)


