package org.koreait;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App_back {

    Scanner scanner = new Scanner(System.in);
    String cmd = "";
    int sys_status = 0;

    String url = "jdbc:mariadb://localhost:3306/ArticleManager";
    String userName = "root";
    String password = "";
    Connection conn = null;
    PreparedStatement pstmt = null;

    public void run() {

        System.out.println("== ArticleManager 프로그램 실행 ==");


        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        try {
            conn = DriverManager.getConnection(url, userName, password);
            if (conn != null) {
                System.out.println("Connected to database");
            }
        } catch (SQLException e) {
            System.out.println("DB 접속 실패");
            e.printStackTrace();
//            throw new RuntimeException(e);
        }

        while (sys_status == 0) {
            System.out.print("명령어 > ");
            cmd = scanner.nextLine().trim();

            int artionResult = doAction(conn, scanner, cmd);

            if (artionResult == -1) {
                sys_status = -1;
                System.out.println("프로그램 종료");
                continue;
            }

        }

    }

    public int doAction(Connection conn, Scanner scanner, String cmd) {
        if (cmd.equals("exit")) {
            return -1;
        }

        if (cmd.equals("article write")) {
            System.out.print("제목 : ");
            String title = scanner.nextLine();
            System.out.print("내용 : ");
            String body = scanner.nextLine();

            // 데이터 삽입..
            String sql = "INSERT INTO article\n" + "SET regDate = NOW(),\n" + "    updateDate = NOW(),\n" + "    title = '" + title + "',\n" + "    `body` = '" + body + "';";

            // System.out.println(sql);

            try {
                pstmt = conn.prepareStatement(sql);
                int affectedRows = pstmt.executeUpdate(); // 적용된 열의 수
                pstmt.close();
                System.out.println("data affected: " + affectedRows);
            } catch (SQLException e) {
                System.out.println("에러 : " + e);
            }

        } else if (cmd.equals("article list")) {
            // 조회...
            List<Article> articleList = getArticleList(conn);

            if (articleList.size() == 0) {
                System.out.println("게시글 없음");
//                    continue;
            }
            System.out.println(" 번호 /    제목    /     내용 /        작성 날짜        /        수정 날짜        / ");
            for (Article article : articleList) {
                System.out.printf(" %3d /%8s /%10s / %21s / %21s    \n", article.getId(), article.getTitle(), article.getBody(), article.getRegDate(), article.getUpdateDate());
            }
            articleList.clear();

        } else if (cmd.equals("article detail")) {
            System.out.print("자세히 볼 게시물의 id : ");
            int id = scanner.nextInt();
            scanner.nextLine();

            // 조회...
            List<Article> articleList = getArticleList(conn, id);

            if (articleList.size() == 0) {
                System.out.println("게시글 없음");
//                    continue;
            }
            System.out.println("== 검색 결과 ==");
            System.out.println("번호 : " + articleList.get(0).getId());
            System.out.println("작성 날짜 : " + articleList.get(0).getRegDate());
            System.out.println("수정 날짜 : " + articleList.get(0).getUpdateDate());
            System.out.println("제목 : " + articleList.get(0).getTitle());
            System.out.println("내용 : " + articleList.get(0).getBody());

            articleList.clear();
        } else if (cmd.equals("article modify")) {

            System.out.print("수정할 게시물의 id : ");
            int id = scanner.nextInt();
            scanner.nextLine();

            // 조회...
            List<Article> articleList = getArticleList(conn, id);

            if (articleList.size() == 0) {
                System.out.println("게시글 없음");
//                    continue;
            }
            System.out.println("== 검색 결과 ==");
            System.out.println(" 번호 /    제목    /     내용 /        작성 날짜        /        수정 날짜        / ");
            for (Article article : articleList) {
                System.out.printf(" %3d /%8s /%10s / %21s / %21s    \n", article.getId(), article.getTitle(), article.getBody(), article.getRegDate(), article.getUpdateDate());
            }
            articleList.clear();

            System.out.print("새 게시물의 제목 : ");
            String title = scanner.nextLine();
            System.out.print("새 게시물의 내용 : ");
            String body = scanner.nextLine();

            // 주의!
            String sql = "UPDATE article";
            sql += " SET updateDate = NOW()";
            if (title.length() > 0) sql += ", `title` = '" + title + "'";
            if (body.length() > 0) sql += ", `body` = '" + body + "'";
            sql += " WHERE id = " + id + ";";

            System.out.println("수정 sql : " + sql);

            try {

                pstmt = conn.prepareStatement(sql);
                int affectedRows = pstmt.executeUpdate();
                pstmt.close();
                System.out.println("Edit ) data affected: " + affectedRows);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else if (cmd.equals("article delete")) {

            System.out.print("삭제할 게시물의 id : ");
            int id = scanner.nextInt();
            scanner.nextLine();

            String sql = "DELETE FROM article WHERE id = " + id + ";";
            System.out.println("Delete) sql : " + sql);

            // Statement 생성 후 실행할 쿼리정보 등록
            try {
                pstmt = conn.prepareStatement(sql);
                pstmt = conn.prepareStatement(sql);
                int affectedRows = pstmt.executeUpdate(); // 적용된 열의 수
                pstmt.close();

//                    System.out.println("Delete) data affected: " + affectedRows);
                if (affectedRows == 0) {
                    System.out.printf("%d번 게시물은 없습니다.\n", id);
                } else {
                    System.out.printf("%d번 게시물이 삭제되었습니다.\n", id);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


        } else {
            System.out.println("잘못된 명령어");
        }

        return 0;
    }

    public List<Article> getArticleList(Connection conn) {
        List<Article> articles = new ArrayList<>();

        // 조회...
        // 실행할 쿼리
        String sql = "SELECT id, regDate, updateDate, title, `body`\n" + "FROM article;";
        //Statement 생성 후 실행할 쿼리정보 등록
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            //결과를 담을 ResultSet 생성 후 결과 담기
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Article rsArticle = new Article();

                rsArticle.setId(rs.getInt("id"));
                rsArticle.setRegDate(rs.getString("regDate"));
                rsArticle.setUpdateDate(rs.getString("updateDate"));
                rsArticle.setTitle(rs.getString("title"));
                rsArticle.setBody(rs.getString("body"));

                articles.add(rsArticle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return articles;
    }

    public List<Article> getArticleList(Connection conn, int id) {
        List<Article> articles = new ArrayList<>();

        // 조회...
        // 실행할 쿼리
        String sql = "SELECT id, regDate, updateDate, title, `body`\n" + "FROM article" + " WHERE id = " + id + ";\n";
        //Statement 생성 후 실행할 쿼리정보 등록
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            //결과를 담을 ResultSet 생성 후 결과 담기
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Article rsArticle = new Article();

                rsArticle.setId(rs.getInt("id"));
                rsArticle.setRegDate(rs.getString("regDate"));
                rsArticle.setUpdateDate(rs.getString("updateDate"));
                rsArticle.setTitle(rs.getString("title"));
                rsArticle.setBody(rs.getString("body"));

                articles.add(rsArticle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return articles;
    }
}





