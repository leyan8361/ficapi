package com.fic.service.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *   @Author Xie
 *   @Date 2018/11/21
 *   @Discription:
**/
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 支付密码
     */
    private String payPassword;

    /**
     * 用户头像
     */
    private String himageUrl;

    /**
     * 用户推荐码
     */
    private String userInviteCode;

    /**
     * 用户推荐人的推荐码
     */
    private String tUserInviteCode;

    /**
     * 推荐用户id
     */
    private Integer tUserId;

    /**
     * 用户移动电话
     */
    private String mobile;

    /**
     * 用户固定电话
     */
    private String phone;

    /**
     * 用户邮编
     */
    private Integer zip;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户签名信息
     */
    private String remark;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 创建人
     */
    private Integer createdBy;

    /**
     * 更新人
     */
    private Integer updatedBy;

    /**
     * 预留字段_1
     */
    private String userRe1;

    /**
     * 预留字段_2
     */
    private String userRe2;

    /**
     * 预留字段_3
     */
    private String userRe3;

    /**
     * 预留字段_4
     */
    private String userRe4;

    /**
     * 预留字段_6
     */
    private String userRe5;

    /**
     * 预留字段_7
     */
    private String userRe6;

    /**
     * 预留字段_7
     */
    private String userRe7;

    /**
     * 预留字段_8
     */
    private String userRe8;

    /**
     * 预留字段_9
     */
    private String userRe9;

    /**
     * 预留字段_10
     */
    private String userRe10;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return 用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName 
	 *            用户名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return 昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * @param nickName 
	 *            昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * @return 用户密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password 
	 *            用户密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return 用户头像
     */
    public String getHimageUrl() {
        return himageUrl;
    }

    /**
     * @param himageUrl 
	 *            用户头像
     */
    public void setHimageUrl(String himageUrl) {
        this.himageUrl = himageUrl;
    }

    /**
     * @return 用户推荐码
     */
    public String getUserInviteCode() {
        return userInviteCode;
    }

    /**
     * @param userInviteCode 
	 *            用户推荐码
     */
    public void setUserInviteCode(String userInviteCode) {
        this.userInviteCode = userInviteCode;
    }

    /**
     * @return 用户推荐人的推荐码
     */
    public String getTuserInviteCode() {
        return tUserInviteCode;
    }

    /**
     * @param tUserInviteCode
	 *            用户推荐人的推荐码
     */
    public void setTuserInviteCode(String tUserInviteCode) {
        this.tUserInviteCode = tUserInviteCode;
    }

    /**
     * @return 推荐用户id
     */
    public Integer getTuserId() {
        return tUserId;
    }

    /**
     * @param tUserId
	 *            推荐用户id
     */
    public void setTuserId(Integer tUserId) {
        this.tUserId = tUserId;
    }

    /**
     * @return 用户移动电话
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile 
	 *            用户移动电话
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return 用户固定电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone 
	 *            用户固定电话
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return 用户邮编
     */
    public Integer getZip() {
        return zip;
    }

    /**
     * @param zip 
	 *            用户邮编
     */
    public void setZip(Integer zip) {
        this.zip = zip;
    }

    /**
     * @return 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email 
	 *            邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return 用户签名信息
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark 
	 *            用户签名信息
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return 更新时间
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime 
	 *            更新时间
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * @return 创建时间
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime 
	 *            创建时间
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return 创建人
     */
    public Integer getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy 
	 *            创建人
     */
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return 更新人
     */
    public Integer getUpdatedBy() {
        return updatedBy;
    }

    /**
     * @param updatedBy 
	 *            更新人
     */
    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * @return 预留字段_1
     */
    public String getUserRe1() {
        return userRe1;
    }

    /**
     * @param userRe1 
	 *            预留字段_1
     */
    public void setUserRe1(String userRe1) {
        this.userRe1 = userRe1;
    }

    /**
     * @return 预留字段_2
     */
    public String getUserRe2() {
        return userRe2;
    }

    /**
     * @param userRe2 
	 *            预留字段_2
     */
    public void setUserRe2(String userRe2) {
        this.userRe2 = userRe2;
    }

    /**
     * @return 预留字段_3
     */
    public String getUserRe3() {
        return userRe3;
    }

    /**
     * @param userRe3 
	 *            预留字段_3
     */
    public void setUserRe3(String userRe3) {
        this.userRe3 = userRe3;
    }

    /**
     * @return 预留字段_4
     */
    public String getUserRe4() {
        return userRe4;
    }

    /**
     * @param userRe4 
	 *            预留字段_4
     */
    public void setUserRe4(String userRe4) {
        this.userRe4 = userRe4;
    }

    /**
     * @return 预留字段_6
     */
    public String getUserRe5() {
        return userRe5;
    }

    /**
     * @param userRe5 
	 *            预留字段_6
     */
    public void setUserRe5(String userRe5) {
        this.userRe5 = userRe5;
    }

    /**
     * @return 预留字段_7
     */
    public String getUserRe6() {
        return userRe6;
    }

    /**
     * @param userRe6 
	 *            预留字段_7
     */
    public void setUserRe6(String userRe6) {
        this.userRe6 = userRe6;
    }

    /**
     * @return 预留字段_7
     */
    public String getUserRe7() {
        return userRe7;
    }

    /**
     * @param userRe7 
	 *            预留字段_7
     */
    public void setUserRe7(String userRe7) {
        this.userRe7 = userRe7;
    }

    /**
     * @return 预留字段_8
     */
    public String getUserRe8() {
        return userRe8;
    }

    /**
     * @param userRe8 
	 *            预留字段_8
     */
    public void setUserRe8(String userRe8) {
        this.userRe8 = userRe8;
    }

    /**
     * @return 预留字段_9
     */
    public String getUserRe9() {
        return userRe9;
    }

    /**
     * @param userRe9 
	 *            预留字段_9
     */
    public void setUserRe9(String userRe9) {
        this.userRe9 = userRe9;
    }

    /**
     * @return 预留字段_10
     */
    public String getUserRe10() {
        return userRe10;
    }

    /**
     * @param userRe10 
	 *            预留字段_10
     */
    public void setUserRe10(String userRe10) {
        this.userRe10 = userRe10;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }
}