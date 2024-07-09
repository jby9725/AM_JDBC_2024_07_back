package org.koreait;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class JDBC_DeleteTest {

    public static void main(String[] args) {
        Connection conn = null;

        try {
            // 연결..
            Class.forName("org.mariadb.jdbc.Driver");
            // String url = "jdbc:mariadb://localhost:3306/ArticleManager";
            String url = "jdbc:mariadb://localhost:3306/ArticleManager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";
            conn = DriverManager.getConnection(url, "root", "");
            System.out.println("연결 성공!");

            // 데이터 삭제..
            Scanner scanner = new Scanner(System.in);
            System.out.print("삭제할 게시물의 id : ");
            int id = scanner.nextInt();
            scanner.nextLine();

            String sql = "DELETE FROM article WHERE id = " + id + ";";
//            System.out.println("Delete) sql : " + sql);

            // Statement 생성 후 실행할 쿼리정보 등록
            PreparedStatement pstmt = null;
            pstmt = conn.prepareStatement(sql);
            int affectedRows = pstmt.executeUpdate(); // 적용된 열의 수
            pstmt.close();

            System.out.println("Delete) data affected: " + affectedRows);
            if(affectedRows == 0) {
                System.out.printf("%d번 게시물은 없습니다.\n", id);
            }
            else {
                System.out.printf("%d번 게시물이 삭제되었습니다.\n", id);
            }




        } catch (ClassNotFoundException e) {
            System.out.println("드라이버 로딩 실패" + e);
        } catch (SQLException e) {
            System.out.println("에러 : " + e);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
