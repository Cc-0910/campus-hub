import java.sql.*;
import java.util.Scanner;

public class test_db_connection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/campus_hub?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "888";
        
        System.out.println("=== 数据库连接测试 ===");
        
        try {
            // 检查数据库是否存在
            System.out.println("1. 检查数据库连接...");
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("✅ 数据库连接成功!");
            
            // 检查表是否存在
            System.out.println("2. 检查用户表是否存在...");
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables("campus_hub", null, "campus_hub_user", null);
            
            if (tables.next()) {
                System.out.println("✅ campus_hub_user 表已存在");
                
                // 检查表中的数据
                System.out.println("3. 检查现有用户数据...");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM campus_hub_user");
                
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("当前数据库中有 " + count + " 个用户");
                    
                    if (count > 0) {
                        System.out.println("4. 显示现有用户信息:");
                        rs = stmt.executeQuery("SELECT id, username, email, nickname, create_time FROM campus_hub_user");
                        while (rs.next()) {
                            System.out.printf("ID: %d, 用户名: %s, 邮箱: %s, 昵称: %s%n",
                                rs.getLong("id"),
                                rs.getString("username"),
                                rs.getString("email"),
                                rs.getString("nickname"));
                        }
                    } else {
                        System.out.println("数据库表为空，可以进行注册测试");
                    }
                }
            } else {
                System.out.println("❌ campus_hub_user 表不存在，需要初始化数据库");
                System.out.println("请执行 create_database.sql 脚本来创建表结构");
            }
            
            conn.close();
            
        } catch (SQLException e) {
            System.out.println("❌ 数据库连接失败: " + e.getMessage());
            
            if (e.getMessage().contains("Unknown database")) {
                System.out.println("💡 提示: campus_hub 数据库不存在，请先创建数据库");
            } else if (e.getMessage().contains("Access denied")) {
                System.out.println("💡 提示: 数据库访问被拒绝，请检查用户名和密码");
            } else if (e.getMessage().contains("Connection refused")) {
                System.out.println("💡 提示: 无法连接到数据库，请检查MySQL服务是否启动");
            }
        }
    }
}