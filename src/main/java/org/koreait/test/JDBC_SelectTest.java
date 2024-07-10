package org.koreait.test;

import org.koreait.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBC_SelectTest {

    public static void main(String[] args) {
        Connection conn = null;
        List<Article> articles = new ArrayList<>();
        ResultSet rs = null;

        try {
            // 연결..
            Class.forName("org.mariadb.jdbc.Driver");
            // String url = "jdbc:mariadb://localhost:3306/ArticleManager";
            String url = "jdbc:mariadb://localhost:3306/ArticleManager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";
            conn = DriverManager.getConnection(url, "root", "");
            System.out.println("연결 성공!");

            String sql = "SELECT id, regDate, updateDate, title, `body`\n" +
                    "FROM article;";
            //Statement 생성 후 실행할 쿼리정보 등록
            PreparedStatement pstmt = conn.prepareStatement(sql);
            //결과를 담을 ResultSet 생성 후 결과 담기
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Article rsArticle = new Article();

                rsArticle.setId(rs.getInt("id"));
                rsArticle.setRegDate(rs.getString("regDate"));
                rsArticle.setUpdateDate(rs.getString("updateDate"));
                rsArticle.setTitle(rs.getString("title"));
                rsArticle.setBody(rs.getString("body"));

                articles.add(rsArticle);
            }

            if (articles.size() == 0) {
                System.out.println("게시글 없음");

            }
            else {
                System.out.println(" 번호 /    제목    /        작성 날짜        /        수정 날짜        /    내용");
                for (Article article : articles) {
                    System.out.printf(" %3d /%8s / %21s / %21s/ %s    \n", article.getId(), article.getTitle(), article.getRegDate(), article.getUpdateDate(), article.getBody());
                }


            }
            articles.clear();

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
