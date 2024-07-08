package org.koreait;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static String cmd = "";
    static int sys_status = 0;
    static List<Article> articles = new ArrayList<>();

    static String url = "jdbc:mariadb://localhost:3306/ArticleManager";
    static String userName = "root";
    static String password = "";
    static Connection conn = null;

    public static void main(String[] args) {

        try {
            Connection conn = DriverManager.getConnection(url, userName, password);
            if (conn != null) {
                System.out.println("Connected to database");
            }


            while (sys_status == 0) {
                System.out.print("명령어 > ");
                cmd = scanner.nextLine().trim();

                if (cmd.equals("exit")) {
                    sys_status = -1;
                    System.out.println("프로그램 종료");
                    continue;
                }
                if (cmd.equals("article write")) {
                    System.out.print("제목 : ");
                    String title = scanner.nextLine();
                    System.out.print("내용 : ");
                    String body = scanner.nextLine();

                    // 데이터 삽입..
                    String sql = "INSERT INTO article\n" +
                            "SET regDate = NOW(),\n" +
                            "    updateDate = NOW(),\n" +
                            "    title = " + title + ",\n" +
                            "    `body` = " + body + ";";

                    // System.out.println(sql);

                    try {
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        int affectedRows = pstmt.executeUpdate(); // 적용된 열의 수
                        pstmt.close();
                        System.out.println("data affected: " + affectedRows);
                    } catch (SQLException e) {
                        System.out.println("에러 : " + e);
                    }
                    // Article newArticle = new Article(++lastID, title, body);
                    // articles.add(newArticle);

                } else if (cmd.equals("article list")) {
                    // 조회...
                    // 실행할 쿼리
                    String sql = "SELECT id, regDate, updateDate, title, `body`\n" +
                            "FROM article;";
                    //Statement 생성 후 실행할 쿼리정보 등록
                    PreparedStatement pstmt = conn.prepareStatement(sql);
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

                    if (articles.size() == 0) {
                        System.out.println("게시글 없음");
                        continue;
                    }
                    System.out.println(" 번호 /    제목    /     내용 /        작성 날짜        /        수정 날짜        / ");
                    for (Article article : articles) {
                        System.out.printf(" %3d /%8s /%10s / %21s / %21s    \n", article.getId(), article.getTitle(), article.getBody(), article.getRegDate(), article.getUpdateDate());
                    }
                    articles.clear();

                } else if (cmd.equals("article edit")) {

                    System.out.print("수정할 게시물의 id : ");
                    int id = scanner.nextInt();

                    // 조회...
                    // 실행할 쿼리
                    String sql = "SELECT id, regDate, updateDate, title, `body`\n" +
                            "FROM article WHERE id = " + id + ";\n";
                    //Statement 생성 후 실행할 쿼리정보 등록
                    PreparedStatement pstmt = conn.prepareStatement(sql);
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

                    if (articles.size() == 0) {
                        System.out.println("게시글 없음");
                        continue;
                    }
                    System.out.println("== 검색 결과 ==");
                    System.out.println(" 번호 /    제목    /     내용 /        작성 날짜        /        수정 날짜        / ");
                    for (Article article : articles) {
                        System.out.printf(" %3d /%8s /%10s / %21s / %21s    \n", article.getId(), article.getTitle(), article.getBody(), article.getRegDate(), article.getUpdateDate());
                    }
                    articles.clear();

                    scanner.nextLine();
                    System.out.print("새 게시물의 제목 : ");
                    String title = scanner.nextLine();
                    System.out.print("새 게시물의 내용 : ");
                    String body = scanner.nextLine();

                    System.out.println("title = " + title);
                    System.out.println("body = " + body);

                    sql = "UPDATE article SET" +
                            " `title` = '" + title + "', `body` = '" + body + "', updateDate = NOW() WHERE id = " + id + ";";

                    System.out.println("수정 sql : " + sql);

                    pstmt = conn.prepareStatement(sql);
                    int affectedRows = pstmt.executeUpdate();
                    pstmt.close();

                    System.out.println("Edit ) data affected: " + affectedRows);

                } else if (cmd.equals("article delete")) {

                    System.out.print("삭제할 게시물의 id : ");
                    int id = scanner.nextInt();

                    String sql = "DELETE FROM article WHERE id = " + id + ";";
                    System.out.println("Delete) sql : " + sql);

                    // Statement 생성 후 실행할 쿼리정보 등록
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt = conn.prepareStatement(sql);
                    int affectedRows = pstmt.executeUpdate(); // 적용된 열의 수
                    pstmt.close();
                    System.out.println("Delete) data affected: " + affectedRows);

                } else {
                    System.out.println("잘못된 명령어");
                }
            }
        } catch (SQLException e) {
            System.out.println("DB 접속 실패");
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
    }
}