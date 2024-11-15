import java.sql.*;
import java.util.Scanner;
import java.util.*;

public class Main {
    public static String nome;
    public static int idade;
    static Scanner scanner = new Scanner(System.in);

    static List<String> tabela = new ArrayList<>();
    public static void main(String[] args) {
        String nomeTabela;
        int numAtributos;
        boolean entrada = true;
        try {
            // Carrega o driver JDBC do MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Conecta ao banco de dados (substitua as informações de conexão conforme necessário)
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/teste", "root", "#Mathe0u");
            int op = 0;
            int saida;
            while (entrada) {
//                System.out.println("0 = sair");
//                saida = scanner.nextInt();
//                if (saida == 0) {
//                    break;
//                }

                do{
                    System.out.println("Opções:\n1. Adicionar dados\n2. Mostrar dados\n3. Apagar dados\n4. Apagar a tabela inteira\n5. Busca\n0. Sair");

                    if (scanner.hasNextInt()) {
                        op = scanner.nextInt();
                        scanner.nextLine(); // Consumir a nova linha após o próximoInt()

                        switch (op) {
                            case 1:
                                adicionarDados(conn);
                                break;
                            case 2:
                                mostrarDados(conn);
                                break;
                            case 3:
                                deletarDados(conn);
                                break;
                            case 0:
                                entrada = false;
                                break;
                            case 4:
                                apagarTabala(conn);
                                break;
                            case 5:
                                pesquisarDados(conn);
                                break;
                            default:
                                System.out.println("Opção inválida. Tente novamente.");
                        }
                    } else {
                        System.out.println("Entrada inválida. Digite um número inteiro.");
                        scanner.nextLine(); // Consumir a entrada inválida
                    }
                } while (entrada != false);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void adicionarDados(Connection conn) throws SQLException {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite seu nome: ");
        String nome = scanner.nextLine();

        System.out.print("Digite sua idade: ");
        int idade = scanner.nextInt();

        // Insere os dados na tabela 'teste'
        String sql = "INSERT INTO teste (NOME, IDADE) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setInt(2, idade);
            ps.executeUpdate();
        }
    }

    public static void mostrarDados(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM teste");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("ID");
                String nomeBanco = rs.getString("NOME");
                int idadeBanco = rs.getInt("IDADE");
                System.out.println("ID: " + id + ", Nome: " + nomeBanco + ", Idade: " + idadeBanco);
            }
        }
    }

    public static void deletarDados(Connection conn) throws SQLException {
        System.out.print("Digite o ID do registro que deseja excluir: ");
        int idExcluir = scanner.nextInt();

        // Verifica se o registro com o ID informado existe
        String verificaSql = "SELECT COUNT(*) FROM teste WHERE ID = ?";
        PreparedStatement verificaPs = conn.prepareStatement(verificaSql);
        verificaPs.setInt(1, idExcluir);
        ResultSet verificaRs = verificaPs.executeQuery();
        verificaRs.next(); // Move para o primeiro resultado (contagem de registros)
        int totalRegistros = verificaRs.getInt(1);

        if (totalRegistros > 0) {
            // O registro existe, então podemos excluí-lo
            String sql = "DELETE FROM teste WHERE ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idExcluir);
            ps.executeUpdate();
            System.out.println("Registro com ID " + idExcluir + " excluído com sucesso.");
        } else {
            System.out.println("O registro com ID " + idExcluir + " não existe na tabela.");
        }
    }
    public static void apagarTabala(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            // Desativa verificação de chaves estrangeiras
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
            // Executa o TRUNCATE na tabela (substitua "sua_tabela" pelo nome correto)
            stmt.execute("TRUNCATE TABLE teste");
            // Reativa verificação de chaves estrangeiras
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
            System.out.println("Tabela truncada com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void pesquisarDados(Connection conn ) {
        try {
            System.out.println("Informe o critério de busca (ID, nome ou idade):");
            String criterio = scanner.nextLine();
            String sql = "SELECT * FROM teste WHERE id = ? OR nome = ? OR idade = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, criterio);
            stmt.setString(2, criterio);
            stmt.setString(3, criterio);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                int idade = rs.getInt("idade");
                System.out.println("ID: " + id + ", Nome: " + nome + ", Idade: " + idade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
