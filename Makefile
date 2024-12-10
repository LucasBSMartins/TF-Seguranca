# Variáveis
SRC_DIR = src
BUILD_DIR = build
PACKAGE = criptografia
PACKAGE2 = login
PACKAGE3 = criptografia/chaves
PACKAGE4 = criptografia/certificados
PACKAGE5 = criptografia/assinatura
PACKAGE6 = criptografia/repositorios
MAIN_CLASS = Main
LIB_DIR = lib
BC_JAR = $(LIB_DIR)/bcpkix-jdk18on-1.79.jar;$(LIB_DIR)/bcprov-jdk18on-1.79.jar;$(LIB_DIR)/bcutil-jdk18on-1.79.jar;$(LIB_DIR)/pdfbox-app-3.0.3.jar

# Alvo padrão: compilar e rodar
all: compile run

# Compilar arquivos Java
compile:
	@echo "Compilando os arquivos Java..."
	@mkdir $(BUILD_DIR) 2>nul || echo "Diretório de build já existe."
	javac -cp $(BC_JAR) -d $(BUILD_DIR) $(SRC_DIR)/*.java $(SRC_DIR)/$(PACKAGE2)/*.java $(SRC_DIR)/$(PACKAGE3)/*.java $(SRC_DIR)/$(PACKAGE4)/*.java $(SRC_DIR)/$(PACKAGE5)/*.java $(SRC_DIR)/$(PACKAGE6)/*.java

# Executar o programa
run:
	@echo "Rodando o programa..."
	java -cp "build;lib/bcprov-jdk18on-1.79.jar;lib/bcpkix-jdk18on-1.79.jar;lib/bcutil-jdk18on-1.79.jar;lib/pdfbox-app-3.0.3.jar" $(SRC_DIR)/$(MAIN_CLASS)

# Limpar arquivos de build
clean:
	@echo "Limpando o diretório de build..."
	@if exist $(BUILD_DIR) rmdir /s /q $(BUILD_DIR)

# Recompilar do zero
rebuild: clean all
