import java.sql.*;

public class verify_database {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "888";
        
        System.out.println("=== 数据库状态验证 ===");
        
        try {
            // 首先连接MySQL服务器（不指定数据库）
            System.out.println("1. 连接MySQL服务器...");
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("✅ MySQL连接成功!");
            
            // 检查数据库列表
            System.out.println("2. 检查现有数据库...");
            Statement stmt = conn.createStatement();
            ResultSet databases = stmt.executeQuery("SHOW DATABASES");
            System.out.println("现有数据库:");
            boolean campusHubExists = false;
            while (databases.next()) {
                String dbName = databases.getString(1);
                System.out.println("  - " + dbName);
                if ("campus_hub".equals(dbName)) {
                    campusHubExists = true;
                }
            }
            
            if (!campusHubExists) {
                System.out.println("❌ campus_hub 数据库不存在!");
                System.out.println("需要创建数据库。");
                return;
            }
            
            // 连接到campus_hub数据库
            System.out.println("3. 连接到campus_hub数据库...");
            String dbUrl = "jdbc:mysql://localhost:3306/campus_hub?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
            Connection dbConn = DriverManager.getConnection(dbUrl, username, password);
            System.out.println("✅ 连接到campus_hub数据库成功!");
            
            // 检查表列表
            System.out.println("4. 检查数据库中的表...");
            Statement dbStmt = dbConn.createStatement();
            ResultSet tables = dbStmt.executeQuery("SHOW TABLES");
            System.out.println("campus_hub数据库中的表:");
            boolean userTableExists = false;
            while (tables.next()) {
                String tableName = tables.getString(1);
                System.out.println("  - " + tableName);
                if ("campus_hub_user".equals(tableName)) {
                    userTableExists = true;
                }
            }
            
            if (!userTableExists) {
                System.out.println("❌ campus_hub_user 表不存在!");
                System.out.println("需要创建表结构。");
                return;
            }
            
            // 检查表结构
            System.out.println("5. 检查campus_hub_user表结构...");
            ResultSet tableDesc = dbStmt.executeQuery("DESCRIBE campus_hub_user");
            System.out.println("campus_hub_user表结构:");
            while (tableDesc.next()) {
                System.out.printf("  %s %s%n", tableDesc.getString("Field"), tableDesc.getString("Type"));
            }
            
            // 检查现有数据
            System.out.println("6. 检查现有用户数据...");
            ResultSet userCount = dbStmt.executeQuery("SELECT COUNT(*) FROM campus_hub_user");
            if (userCount.next()) {
                int count = userCount.getInt(1);
                System.out.println("当前用户数量: " + count);
                
                if (count > 0) {
                    System.out.println("现有用户列表:");
                    ResultSet users = dbStmt.executeQuery("SELECT username, email, nickname FROM campus_hub_user");
                    while (users.next()) {
                        System.out.printf("  - 用户名: %s, 邮箱: %s, 昵称: %s%n",
                            users.getString("username"),
                            users.getString("email"),
                            users.getString("nickname"));
                    }
                }
            }
            
            System.out.println("✅ 数据库验证完成!");
            conn.close();
            dbConn.close();
            
        } catch (SQLException e) {
            System.out.println("❌ 数据库操作失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}