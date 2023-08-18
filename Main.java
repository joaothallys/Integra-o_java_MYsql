import java.sql.Connection;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        
        Connection conn = ConnectionFactory.getConnection();
        
        if (conn == null) {
            System.out.println("A conexão não ocorreu");
        } else {
            System.out.println("O banco está conectado");
        }

        Scanner scanner = new Scanner(System.in);
        int opcao = 0;

        while (opcao != 9) {
            exibirMenu();
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer

            switch (opcao) {
                case 1:
                    Manipulacao.incluirProfissao(scanner);
                    break;
                case 2:
                    Manipulacao.alterarProfissao(scanner);
                    break;
                case 3:
                    Manipulacao.excluirProfissao(scanner);
                    break;
                case 4:
                    Manipulacao.listarProfissoes();
                    break;
                case 5:
                    Manipulacao.incluirPessoa(scanner);
                    break;
                case 6:
                    Manipulacao.alterarPessoa(scanner);
                    break;
                case 7:
                    Manipulacao.excluirPessoa(scanner);
                    break;
                case 8:
                    Manipulacao.listarPessoasTelefones();
                    break;
                case 9:
                    Manipulacao.listarPessoasTelefonesProfissao();
                    break;
                case 10:
                    System.out.println("Fim do programa.");
                    break;
                
                default:
                    System.out.println("Opção inválida. Digite novamente.");
            }

            System.out.println();
        }

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("==== Menu de Opções ====");
        System.out.println("1. Incluir Profissão");
        System.out.println("2. Alterar Profissão");
        System.out.println("3. Excluir Profissão");
        System.out.println("4. Listar Profissões");
        System.out.println("5. Incluir Pessoa");
        System.out.println("6. Alterar Pessoa");
        System.out.println("7. Excluir Pessoa");
        System.out.println("8. Listar Pessoas/Telefones");
        System.out.println("9. Listar Pessoas/Telefones/Profissao");
        System.out.println("10. Fim");
        System.out.print("Escolha uma opção: ");
    }
}