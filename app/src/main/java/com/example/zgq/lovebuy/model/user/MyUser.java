package com.example.zgq.lovebuy.model.user;

import cn.bmob.v3.BmobUser;

/**
 * Created by 37902 on 2016/2/21.
 */
public class MyUser extends BmobUser {
    private String associateEmail;
    private String associateEmailPsd;
    private String passwordBackup;

    public String getPasswordBackup() {
        return passwordBackup;
    }

    public void setPasswordBackup(String passwordBackup) {
        this.passwordBackup = passwordBackup;
    }

    public MyUser(){}
    public MyUser(String associateEmail, String associateEmailPsd) {
        this.associateEmail = associateEmail;
        this.associateEmailPsd = associateEmailPsd;
    }

    public String getAssociateEmail() {
        return associateEmail;
    }

    public void setAssociateEmail(String associateEmail) {
        this.associateEmail = associateEmail;
    }

    public String getAssociateEmailPsd() {
        return associateEmailPsd;
    }

    public void setAssociateEmailPsd(String associateEmailPsd) {
        this.associateEmailPsd = associateEmailPsd;
    }
}
