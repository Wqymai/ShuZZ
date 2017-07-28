package com.wuqiyan.shuzz.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wuqiyan on 17/7/28.
 */

@Entity
public class UserModel {
    private String nickname;
    private String gender;
    private String province;
    private String city;
    private String figureurl_qq_1;
    private String figureurl_qq_2;

    @Generated(hash = 988840714)
    public UserModel(String nickname, String gender, String province, String city,
            String figureurl_qq_1, String figureurl_qq_2) {
        this.nickname = nickname;
        this.gender = gender;
        this.province = province;
        this.city = city;
        this.figureurl_qq_1 = figureurl_qq_1;
        this.figureurl_qq_2 = figureurl_qq_2;
    }

    @Generated(hash = 782181818)
    public UserModel() {
    }

    public String getFigureurl_qq_1() {
        return figureurl_qq_1;
    }

    public void setFigureurl_qq_1(String figureurl_qq_1) {
        this.figureurl_qq_1 = figureurl_qq_1;
    }

    public String getFigureurl_qq_2() {
        return figureurl_qq_2;
    }

    public void setFigureurl_qq_2(String figureurl_qq_2) {
        this.figureurl_qq_2 = figureurl_qq_2;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
