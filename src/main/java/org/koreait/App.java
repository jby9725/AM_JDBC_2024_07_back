package org.koreait;

import org.koreait.controller.ArticleController;
import org.koreait.controller.Controller;
import org.koreait.controller.MemberController;
import org.koreait.util.DBUtil;
import org.koreait.util.SecSql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class App {

    String cmd = "";
    int sys_status = 0;

    String url = "jdbc:mariadb://localhost:3306/ArticleManager";
    String userName = "root";
    String password = "";
    Connection conn = null;
    PreparedStatement pstmt = null;
    Controller controller;

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

        controller = new Controller();

        while (sys_status == 0) {
            System.out.print("명령어 > ");
            cmd = Container.getScanner().nextLine().trim();

            int artionResult = controller.doAction(conn, cmd);
            // int artionResult = action(conn, cmd);

            if (artionResult == -1) {
                sys_status = -1;
                System.out.println("프로그램 종료");
                continue;
            }

        }

    }


    public int action(Connection conn, String cmd) {
        if (cmd.equals("exit")) {
            return -1;
        }
        ///////////////////////////////////////// Controller /////////////////////////////////

        MemberController memberController = new MemberController(conn);
        ArticleController articleController = new ArticleController();

        ///////////////////////////////////////// article /////////////////////////////////
        if (cmd.equals("article write")) {
            System.out.print("제목 : ");
            String title = Container.getScanner().nextLine();
            System.out.print("내용 : ");
            String body = Container.getScanner().nextLine();

            // 데이터 삽입..
            SecSql sql = new SecSql();
            sql.append("INSERT INTO article");
            sql.append("SET regDate = NOW(),");
            sql.append("updateDate = NOW(),");
            sql.append("title = ?,", title);
            sql.append("body = ?,", body);

            int id = DBUtil.insert(conn, sql);

            System.out.println(id + "번 글이 생성되었습니다.");

        } else if (cmd.equals("article list")) {
            // 조회...
            List<Article> articles = new ArrayList<>();
            SecSql sql = new SecSql();
            sql.append("SELECT *");
            sql.append("FROM article");
            sql.append("ORDER BY id DESC");

            List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);

            for (Map<String, Object> articleMap : articleListMap) {
                articles.add(new Article(articleMap));
            }

            if (articles.size() == 0) {
                System.out.println("게시글 없음");
                return 0;
            }

            System.out.println(" 번호 /    제목    /     내용 /        작성 날짜        /        수정 날짜        / ");
            for (Article article : articles) {
                System.out.printf(" %3d /%8s /%10s / %21s / %21s    \n", article.getId(), article.getTitle(), article.getBody(), article.getRegDate(), article.getUpdateDate());
            }
            articles.clear();


        } else if (cmd.equals("article detail")) {
            System.out.print("자세히 볼 게시물의 id : ");
            int id = Container.getScanner().nextInt();
            Container.getScanner().nextLine();

            // 조회...
            SecSql sql = new SecSql();
            sql.append("SELECT *");
            sql.append("FROM article");
            sql.append("WHERE id = ?", id);

            Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

            if (articleMap.isEmpty()) {
                System.out.println(id + "번 글은 없습니다.");
                return 0;
            }

            Article article = new Article(articleMap);

            System.out.println("== 검색 결과 ==");
            System.out.println("번호 : " + article.getId());
            System.out.println("작성날짜 : " + article.getRegDate());
            System.out.println("수정날짜 : " + article.getUpdateDate());
            System.out.println("제목 : " + article.getTitle());
            System.out.println("내용 : " + article.getBody());


        } else if (cmd.equals("article modify")) {

            System.out.print("수정할 게시물의 id : ");
            int id = Container.getScanner().nextInt();
            Container.getScanner().nextLine();

            // 조회...
            SecSql sql = new SecSql();
            sql.append("SELECT *");
            sql.append("FROM article");
            sql.append("WHERE id = ?", id);

            Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

            if (articleMap.isEmpty()) {
                System.out.println(id + "번 글은 없습니다.");
                return 0;
            }

            System.out.print("새 게시물의 제목 : ");
            String title = Container.getScanner().nextLine();
            System.out.print("새 게시물의 내용 : ");
            String body = Container.getScanner().nextLine();

            sql = new SecSql();
            sql.append("UPDATE article");
            sql.append("SET updateDate = NOW()");
            if (title.length() > 0)
                sql.append(",title = ?", title);
            if (body.length() > 0)
                sql.append(",`body` = ?", body);
            sql.append("WHERE id = ?", id);

            DBUtil.update(conn, sql);

            System.out.println(id + "번 글이 수정되었습니다.");

        } else if (cmd.equals("article delete")) {

            System.out.print("삭제할 게시물의 id : ");
            int id = Container.getScanner().nextInt();
            Container.getScanner().nextLine();

            SecSql sql = new SecSql();
            sql.append("SELECT *");
            sql.append("FROM article");
            sql.append("WHERE id = ?", id);

            Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

            if (articleMap.isEmpty()) {
                System.out.println(id + "번 글은 없습니다.");
                return 0;
            }

            sql = new SecSql();
            sql.append("DELETE FROM article");
            sql.append("WHERE id = ?", id);

            DBUtil.delete(conn, sql);

            System.out.println(id + "번 글이 삭제되었습니다.");

            //////////////////////////////// member //////////////////////////////////////////
        } else if (cmd.equals("member join")) {
            memberController.doJoin();
            /*
            *             System.out.println("== 회원가입 ==");

            String userId = null;
            String password = null;
            String pwdConfirm = null;
            String nickname = null;

            while (true) {
                System.out.print("사용자 아이디 : ");
                userId = Container.getScanner().nextLine().trim();

                if (userId.length() == 0 || userId.contains(" ")) {
                    System.out.println("아이디 재입력 필요");
                    continue;
                }

                // 아이디가 중복인지 체크....
                boolean is_id_unique = false;
                SecSql sql = new SecSql();
                sql.append("SELECT *");
                sql.append("FROM `member`");
                sql.append("WHERE `userId` = ?", userId);

                Map<String, Object> memberMap = DBUtil.selectRow(conn, sql);
                // System.out.println("memberMap = " + memberMap);

                if (memberMap.isEmpty())
                    is_id_unique = true;
                else is_id_unique = false;

                if (!is_id_unique) {
                    System.out.println("해당 아이디가 이미 있습니다.");
                    System.out.println("아이디를 다시 입력 받습니다.");
                    continue;
                }
                break;
            }

            while (true) {
                System.out.print("사용자 비밀번호 : ");
                password = Container.getScanner().nextLine().trim();

                if (password.length() == 0 || password.contains(" ")) {
                    System.out.println("비밀번호 재입력 필요");
                    continue;
                }

                boolean loginPwdCheck = true;

                while (true) {
                    System.out.print("사용자 비밀번호 확인 : ");
                    pwdConfirm = Container.getScanner().nextLine();
                    if (pwdConfirm.length() == 0 || pwdConfirm.contains(" ")) {
                        System.out.println("확인 비밀번호 재입력 필요");
                        continue;
                    }

                    if (!pwdConfirm.equals(password)) {
                        loginPwdCheck = false;
                    }
                    break;
                }
                if (loginPwdCheck) {
                    break;
                }

            }

            while (true) {
                System.out.print("사용자 닉네임 : ");
                nickname = Container.getScanner().nextLine();
                if (nickname.length() == 0 || nickname.contains(" ")) {
                    System.out.println("닉네임 재입력 필요");
                    continue;
                }
                break;
            }

            // 데이터 삽입..
            SecSql sql = new SecSql();
            sql.append("INSERT INTO `member` (`regDate`, `userId`, `password`, `nickname`) VALUES");
            sql.append("(NOW(),");
            sql.append(" ?,", userId);
            sql.append(" ?,", password);
            sql.append(" ?)", nickname);

            int id = DBUtil.insert(conn, sql);
            System.out.println(id + "번 멤버가 생성되었습니다.");
            * */

        } else {
            System.out.println("잘못된 명령어");
        }

        return 0;
    }

}





