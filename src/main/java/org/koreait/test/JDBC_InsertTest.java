package org.koreait.test;

import java.sql.*;

public class JDBC_InsertTest {

    public static void main(String[] args) {
        Connection conn = null;

        try {
            // 연결..
            Class.forName("org.mariadb.jdbc.Driver");
            // String url = "jdbc:mariadb://localhost:3306/ArticleManager";
            String url = "jdbc:mariadb://localhost:3306/ArticleManager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";
            conn = DriverManager.getConnection(url, "root", "");
            System.out.println("연결 성공!");

            // 데이터 삽입..
            String sql = "INSERT INTO article\n" +
                    "SET regDate = NOW(),\n" +
                    "    updateDate = NOW(),\n" +
                    "    title = CONCAT('제목', SUBSTRING(RAND() * 1000 FROM 1 FOR 2)),\n" +
                    "    `body` = CONCAT('내용', SUBSTRING(RAND() * 1000 FROM 1 FOR 2));";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            int affectedRows = pstmt.executeUpdate(); // 적용된 열의 수
            pstmt.close();

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
