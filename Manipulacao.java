
import java.sql.*;
import java.util.Scanner;

public class Manipulacao {
    private static final String URL = "jdbc:mysql://localhost:3306/locadoradvd";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    public static void incluirProfissao(Scanner scanner) {

        System.out.print("Descrição da Profissão: ");
        String descricao = scanner.nextLine();

        String insertProfissao = "INSERT INTO Profissao (Descricao) VALUES (?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(insertProfissao)) {
            statement.setString(1, descricao);
            statement.executeUpdate();
            System.out.println("Profissão incluída com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void alterarProfissao(Scanner scanner) {
        System.out.print("Informe o código ou parte da descrição da profissão: ");
        String descricaoProfissao = scanner.nextLine();

        String selectProfissao = "SELECT ProfissaoId, Descricao FROM Profissao WHERE Descricao LIKE ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(selectProfissao)) {
            statement.setString(1, "%" + descricaoProfissao + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int profissaoId = resultSet.getInt("ProfissaoId");
                String descricao = resultSet.getString("Descricao");
                System.out.println("[" + profissaoId + "] " + descricao);
            }

            System.out.print("Informe o código da profissão que deseja alterar: ");
            int profissaoId = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer

            System.out.print("Nova descrição da profissão: ");
            String novaDescricao = scanner.nextLine();

            String updateProfissao = "UPDATE Profissao SET Descricao = ? WHERE ProfissaoId = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateProfissao)) {
                updateStatement.setString(1, novaDescricao);
                updateStatement.setInt(2, profissaoId);
                updateStatement.executeUpdate();
                System.out.println("Profissão alterada com sucesso.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void excluirProfissao(Scanner scanner) {
        System.out.print("Informe o código ou parte da descrição da profissão: ");
        String descricaoProfissao = scanner.nextLine();

        String selectProfissao = "SELECT ProfissaoId, Descricao FROM Profissao WHERE Descricao LIKE ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(selectProfissao)) {
            statement.setString(1, "%" + descricaoProfissao + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int profissaoId = resultSet.getInt("ProfissaoId");
                String descricao = resultSet.getString("Descricao");
                System.out.println("[" + profissaoId + "] " + descricao);
            }

            System.out.print("Informe o código da profissão que deseja excluir: ");
            int profissaoId = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer

            String deleteProfissao = "DELETE FROM Profissao WHERE ProfissaoId = ?";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteProfissao)) {
                deleteStatement.setInt(1, profissaoId);
                deleteStatement.executeUpdate();
                System.out.println("Profissão excluída com sucesso.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void listarProfissoes() {
        String selectProfissoes = "SELECT ProfissaoId, Descricao FROM Profissao ORDER BY Descricao";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectProfissoes)) {

            System.out.println("Profissões:");

            while (resultSet.next()) {
                int profissaoId = resultSet.getInt("ProfissaoId");
                String descricao = resultSet.getString("Descricao");
                System.out.println("[" + profissaoId + "] " + descricao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void incluirPessoa(Scanner scanner) {
        System.out.print("Nome da Pessoa: ");
        String nome = scanner.nextLine();

        listarProfissoes();

        System.out.print("Informe o código da profissão da pessoa: ");
        int profissaoId = scanner.nextInt();
        scanner.nextLine(); 

        String insertPessoa = "INSERT INTO Pessoa (Nome, ProfissaoId) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(insertPessoa,
                        Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, nome);
            statement.setInt(2, profissaoId);
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int pessoaId = generatedKeys.getInt(1);
                incluirTelefones(scanner, pessoaId);
                System.out.println("Pessoa incluída com sucesso.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void incluirTelefones(Scanner scanner, int pessoaId) {
        System.out.print("Deseja incluir telefone(s) para a pessoa? (S/N): ");
        String resposta = scanner.nextLine();

        if (resposta.equalsIgnoreCase("S")) {
            while (true) {
                System.out.print("Número do telefone: ");
                String numero = scanner.nextLine();

                String insertTelefone = "INSERT INTO Telefone (Numero, PessoaId) VALUES (?, ?)";
                try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                        PreparedStatement statement = connection.prepareStatement(insertTelefone)) {
                    statement.setString(1, numero);
                    statement.setInt(2, pessoaId);
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                System.out.print("Deseja incluir mais um telefone? (S/N): ");
                resposta = scanner.nextLine();
                if (!resposta.equalsIgnoreCase("S")) {
                    break;
                }
            }
        }
    }

    public static void alterarPessoa(Scanner scanner) {
        System.out.print("Informe o código ou parte do nome da pessoa: ");
        String nomePessoa = scanner.nextLine();

        String selectPessoa = "SELECT PessoaId, Nome FROM Pessoa WHERE Nome LIKE ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(selectPessoa)) {
            statement.setString(1, "%" + nomePessoa + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int pessoaId = resultSet.getInt("PessoaId");
                String nome = resultSet.getString("Nome");
                System.out.println("[" + pessoaId + "] " + nome);
            }

            System.out.print("Informe o código da pessoa que deseja alterar: ");
            int pessoaId = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer

            System.out.print("Novo nome da pessoa: ");
            String novoNome = scanner.nextLine();

            String updatePessoa = "UPDATE Pessoa SET Nome = ? WHERE PessoaId = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updatePessoa)) {
                updateStatement.setString(1, novoNome);
                updateStatement.setInt(2, pessoaId);
                updateStatement.executeUpdate();
                System.out.println("Pessoa alterada com sucesso.");

                excluirTelefonesPessoa(scanner, pessoaId);
                incluirTelefones(scanner, pessoaId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void excluirTelefonesPessoa(Scanner scanner, int pessoaId) {
        String selectTelefones = "SELECT TelefoneId, Numero FROM Telefone WHERE PessoaId = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(selectTelefones)) {
            statement.setInt(1, pessoaId);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Telefones da Pessoa:");
            while (resultSet.next()) {
                int telefoneId = resultSet.getInt("TelefoneId");
                String numero = resultSet.getString("Numero");
                System.out.println("[" + telefoneId + "] " + numero);
            }

            System.out.print("Deseja excluir os telefones da pessoa? (S/N): ");
            String resposta = scanner.nextLine();

            if (resposta.equalsIgnoreCase("S")) {
                String deleteTelefones = "DELETE FROM Telefone WHERE PessoaId = ?";
                try (PreparedStatement deleteStatement = connection.prepareStatement(deleteTelefones)) {
                    deleteStatement.setInt(1, pessoaId);
                    deleteStatement.executeUpdate();
                    System.out.println("Telefones excluídos com sucesso.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void excluirPessoa(Scanner scanner) {
        System.out.print("Informe o código ou parte do nome da pessoa: ");
        String nomePessoa = scanner.nextLine();

        String selectPessoa = "SELECT PessoaId, Nome FROM Pessoa WHERE Nome LIKE ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(selectPessoa)) {
            statement.setString(1, "%" + nomePessoa + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int pessoaId = resultSet.getInt("PessoaId");
                String nome = resultSet.getString("Nome");
                System.out.println("[" + pessoaId + "] " + nome);
            }

            System.out.print("Informe o código da pessoa que deseja excluir: ");
            int pessoaId = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer

            String deletePessoa = "DELETE FROM Pessoa WHERE PessoaId = ?";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deletePessoa)) {
                deleteStatement.setInt(1, pessoaId);
                deleteStatement.executeUpdate();
                System.out.println("Pessoa excluída com sucesso.");

                excluirTelefonesPessoa(scanner, pessoaId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void listarPessoasTelefones() {
        String selectPessoasTelefones = "SELECT p.PessoaId, p.Nome, t.Numero FROM Pessoa p " +
                "LEFT JOIN Telefone t ON p.PessoaId = t.PessoaId ORDER BY p.Nome";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectPessoasTelefones)) {

            System.out.println("Pessoas/Telefones:");

            int pessoaIdAtual = -1;
            while (resultSet.next()) {
                int pessoaId = resultSet.getInt("PessoaId");
                String nome = resultSet.getString("Nome");
                String numero = resultSet.getString("Numero");

                if (pessoaId != pessoaIdAtual) {
                    System.out.println("[" + pessoaId + "] " + nome);
                    pessoaIdAtual = pessoaId;
                }

                if (numero != null) {
                    System.out.println("\tTelefone: " + numero);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void listarPessoasTelefonesProfissao() {
    String selectPessoasTelefones = "SELECT p.PessoaId, p.Nome, t.Numero, prof.Descricao AS Profissao " +
            "FROM Pessoa p " +
            "LEFT JOIN Telefone t ON p.PessoaId = t.PessoaId " +
            "LEFT JOIN Profissao prof ON p.ProfissaoId = prof.ProfissaoId " +
            "ORDER BY p.Nome";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(selectPessoasTelefones)) {

        System.out.println("Pessoas/Telefones/Profissões:");

        int pessoaIdAtual = -1;
        while (resultSet.next()) {
            int pessoaId = resultSet.getInt("PessoaId");
            String nome = resultSet.getString("Nome");
            String numero = resultSet.getString("Numero");
            String profissao = resultSet.getString("Profissao");

            if (pessoaId != pessoaIdAtual) {
                System.out.println("[" + pessoaId + "] " + nome);
                pessoaIdAtual = pessoaId;
            }

            if (numero != null) {
                System.out.println("\tTelefone: " + numero);
            }

            if (profissao != null) {
                System.out.println("\tProfissão: " + profissao);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


}
