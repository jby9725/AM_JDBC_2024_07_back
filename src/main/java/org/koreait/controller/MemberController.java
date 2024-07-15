package org.koreait.controller;

import org.koreait.Container;
import org.koreait.MemberService;
import org.koreait.util.DBUtil;
import org.koreait.util.SecSql;

import java.sql.Connection;
import java.util.Map;

public class MemberController {
    private Connection conn;

    private MemberService memberService;

    public MemberController(Connection conn) {
        this.conn = conn;
        this.memberService = new MemberService();
    }

    public void doJoin() {
        System.out.println("== 회원가입 ==");

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
    }
}
