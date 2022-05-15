BUILD_DIR = target
SRC_DIR = src
INSTALL_DIR = $(addsuffix /classes/, $(BUILD_DIR))

JAVAC = javac
JAVA = java
CLASS_PATH_FLAG = -cp
TARGET_DIR_FLAG = -d

TEXT_EDITOR_CLASS = utils.editor.TextEditor
SERVER_CLASS = com.Server.Server
CLIENT_CLASS = com.Client.Client

MVN = mvn

RM = rm -rf

ALL_SRC = $(shell find $(SRC_DIR) -name '*.java')

.PHONY: install editor server client clean

editor: ${BUILD_DIR}
	$(JAVA) $(CLASS_PATH_FLAG) $(INSTALL_DIR) $(TEXT_EDITOR_CLASS)

server: ${BUILD_DIR}
	$(JAVA) $(CLASS_PATH_FLAG) $(INSTALL_DIR) $(SERVER_CLASS)

client: ${BUILD_DIR}
	$(JAVA) $(CLASS_PATH_FLAG) $(INSTALL_DIR) $(CLIENT_CLASS)

${BUILD_DIR} : ${ALL_SRC}
	[ ! -d ${BUILD_DIR} ] && ${MVN} install || echo "${BUILD_DIR} already present."

install: ${ALL_SRC}
	${MVN} install

clean:
	$(RM) $(BUILD_DIR)


