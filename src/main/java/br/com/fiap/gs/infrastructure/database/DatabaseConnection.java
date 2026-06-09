package br.com.fiap.gs.infrastructure.database;

import br.com.fiap.gs.domain.exception.TerraformingException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton para conexão com Oracle Cloud.
 * Credenciais configuradas em src/main/resources/db.properties.
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private boolean usingMemory = false;

    private DatabaseConnection() {
        Properties props = loadProperties();
        String url  = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.password");

        try {
            Class.forName("oracle.jdbc.OracleDriver");
            this.connection = DriverManager.getConnection(url, user, pass);
            System.out.println("  [DB] Conexao Oracle estabelecida com sucesso.");
        } catch (Exception e) {
            System.out.println("  [DB] Oracle nao disponivel (" + e.getMessage() + ")");
            System.out.println("  [DB] Usando banco H2 em memoria como fallback.");
            connectH2();
            usingMemory = true;
        }
    }

    private void connectH2() {
        try {
            Class.forName("org.h2.Driver");
            this.connection = DriverManager.getConnection("jdbc:h2:mem:terrapath;DB_CLOSE_DELAY=-1", "sa", "");
            createH2Schema();
        } catch (Exception ex) {
            throw new TerraformingException("DB_H2", "Falha no fallback H2: " + ex.getMessage(), ex);
        }
    }

    private void createH2Schema() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS TB_STAGE (
                    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name         VARCHAR(100)  NOT NULL,
                    type         VARCHAR(50)   NOT NULL,
                    description  VARCHAR(500),
                    risk_factor  DOUBLE        DEFAULT 0,
                    completed    INT           DEFAULT 0,
                    created_at   TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
                    updated_at   TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
                    completed_at TIMESTAMP
                )
                """;
        try (java.sql.Statement st = connection.createStatement()) {
            st.execute(sql);
        }
    }

    public boolean isUsingMemory() { return usingMemory; }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream in = getClass().getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new TerraformingException("DB_000",
                        "Arquivo db.properties nao encontrado em src/main/resources/");
            }
            props.load(in);
        } catch (IOException e) {
            throw new TerraformingException("DB_000", "Erro ao ler db.properties: " + e.getMessage(), e);
        }
        return props;
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                instance = new DatabaseConnection();
                return instance.connection;
            }
        } catch (SQLException e) {
            throw new TerraformingException("DB_003", "Erro ao verificar conexao: " + e.getMessage(), e);
        }
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("  [DB] Conexao encerrada.");
            }
        } catch (SQLException e) {
            System.err.println("  [DB] Erro ao fechar conexao: " + e.getMessage());
        }
    }
}
