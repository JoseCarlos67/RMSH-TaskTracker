# 📝 Task-Manager CLI

O **Task-Manager** é uma aplicação de linha de comando (CLI) intuitiva para gerir as suas tarefas diárias diretamente do terminal. Desenvolvida em Java 21, oferece uma experiência rica com menu interativo, auto-complete e suporte total para **Linux (Fedora)** e **Windows**.

---

## ✨ Funcionalidades

* **Menu Interativo:** Seleção de status (`TODO`, `IN_PROGRESS`, `DONE`) utilizando as setas do teclado e a tecla Enter.
* **Persistência Inteligente:** As tarefas são guardadas automaticamente na pasta do utilizador (`~/.rmsh-taskmanager/tasks.json`), garantindo que não perde dados entre atualizações.
* **Auto-complete Nativo:** Suporte à tecla `Tab` para sugestão de comandos através da biblioteca JLine 3.
* **Filtros de Listagem:** Visualize todas as tarefas ou filtre apenas por um estado específico (ex: `list DONE`).

---

## 🚀 Como Instalar

### 🐧 Linux (Fedora/Distros baseadas em RPM)
Faça o download do arquivo  **Source code(tar.gz)** na aba **Releases** e instale via terminal:
```bash
sudo dnf install ./rmsh-taskmanager-1.0-3.noarch.rpm
```

### 🪟 Windows (.exe)
1. Vá até a aba **Releases** e faça o download do arquivo **Source code (zip)**.

2. Execute o instalador e siga os passos (será criado um atalho no Menu Iniciar e Desktop).

3. Abra o **Windows Terminal** (ou CMD) e digite: rmsh-taskmanager.

---

## 📖 Guia de ComandosO

O **Task-Manager** utiliza um sistema de comandos simples:

| Comando | Parâmetros | Descrição                                                      |
|--------|------------|----------------------------------------------------------------|
| `new` | - | Inicia o processo de criação de uma nova tarefa.               |
| `list` | `[status]` | Lista todas as tarefas (ou filtra por status).                 |
| `update` | `[field] [id]` | Atualiza a *description* ou o *status* (ex: `update status 1`). |
| `delete` | `[id]` | Remove permanentemente a tarefa com o ID especificado.         |
| `clear` | - | Limpa o terminal.                                              |
| `help` | - | Exibe o guia de comandos.                                      |
| `exit` | - | Fecha a aplicação.                              |

---

# 🛠️ Tecnologias e Dependências

- **Java 21**: Linguagem base.
- **JLine 3**: Manipulação de terminal, modo *raw* para menus e auto-complete.
- **Jackson Databind**: Serialização de objetos para ficheiros JSON.
- **GitHub Actions**: Pipeline de CI/CD para geração automática de instaladores `.exe` e `.rpm`.

---

# 🧑‍💻 Compilação Manual (Build)

Se preferir compilar o projeto por conta própria:

## 📥 Clone o repositório

```bash
git clone https://github.com/JoseCarlos67/RMSH-TaskTracker.git
```

## ⚙️ Compile com Maven
```bash
mvn clean package
```
## ▶️ Execute o ficheiro JAR
```bash
java -jar target/RMSH-TaskTracker-1.0-SNAPSHOT.jar
```
📄 Licença

Distribuído sob a licença MIT. Veja o ficheiro LICENSE para mais detalhes.
