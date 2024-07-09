package org.koreait;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class JDBC_UpdateTest {

    public static void main(String[] args) {
        Connection conn = null;

        try {
            // 연결..
            Class.forName("org.mariadb.jdbc.Driver");
            // String url = "jdbc:mariadb://localhost:3306/ArticleManager";
            String url = "jdbc:mariadb://localhost:3306/ArticleManager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";
            conn = DriverManager.getConnection(url, "root", "");
            System.out.println("연결 성공!");

            Scanner scanner = new Scanner(System.in);
            System.out.print("수정할 게시물의 id : ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("새 게시물의 제목 : ");
            String title = scanner.nextLine();
            System.out.print("새 게시물의 내용 : ");
            String body = scanner.nextLine();

            System.out.println("title = " + title);
            System.out.println("body = " + body);

            // 데이터 수정..
            // 실행할 쿼리
            String sql = "UPDATE article SET" +
                    " `title` = '" + title + "', `body` = '" + body + "', updateDate = NOW() WHERE id = " + id + ";";

            System.out.println("수정 sql : " + sql);

            PreparedStatement pstmt = conn.prepareStatement(sql);
            int affectedRows = pstmt.executeUpdate(); // 적용된 열의 수

            if(affectedRows == 0) {
//                System.out.println("영향X");
                System.out.printf("%d번 게시물은 없습니다.\n", id);
            }
            else {
                System.out.printf("%d번 게시물이 수정되었습니다.\n", id);
//                System.out.println("영향O");
//                System.out.println("affectedRows = " + affectedRows);
            }

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
