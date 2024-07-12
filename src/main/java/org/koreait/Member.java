package org.koreait;

import java.util.Map;

public class Member {
    private int id;
    private String regDate;
    private String userId;
    private String password;
    private String nickname;

    Member(int id, String regDate, String userId, String password, String nickname) {
        this.id = id;
        this.regDate = regDate;
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
    }


    public Member(Map<String, Object> articleMap) {
        this.id = (int) articleMap.get("id");
        this.regDate = (String) articleMap.get("regDate");
        this.userId = (String) articleMap.get("userId");
        this.password = (String) articleMap.get("password");
        this.nickname = (String) articleMap.get("nickname");
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}