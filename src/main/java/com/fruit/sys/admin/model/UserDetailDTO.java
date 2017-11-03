package com.fruit.sys.admin.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hao.yuan  on 15/7/30.
 */
public class UserDetailDTO implements Serializable
{
    private int userId;

    private String userName;

    private String phone;

    private double userPower;

    private int score;

    private boolean buyGroup;

    private Date addTime;

    private String email;

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public double getUserPower()
    {
        return userPower;
    }

    public void setUserPower(double userPower)
    {
        this.userPower = userPower;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public boolean isBuyGroup()
    {
        return buyGroup;
    }

    public void setBuyGroup(boolean buyGroup)
    {
        this.buyGroup = buyGroup;
    }

    public Date getAddTime()
    {
        return addTime;
    }

    public void setAddTime(Date addTime)
    {
        this.addTime = addTime;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("UserDetailDTO{");
        sb.append("userId=").append(userId);
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", userPower=").append(userPower);
        sb.append(", score=").append(score);
        sb.append(", buyGroup=").append(buyGroup);
        sb.append(", addTime=").append(addTime);
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
