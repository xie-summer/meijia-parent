package com.simi.po.model.user;

import java.math.BigDecimal;

public class Users {
 
	private Long id;

    private String mobile;
    
    private String thirdType;
    
    private String openid;
    
    private String provinceName;

    private String name;
    
    private String sex;
    
    private String introduction;
    
    private short level;
        
    private	String headImg;

    private BigDecimal restMoney;

    private Integer score;
    
    private Integer exp;

    private Short userType;
    
    private Short addFrom;

    private Long addTime;

    private Long updateTime;
    
	private String realName;

	private String idCard;

	private String qrCode;


    public Users() {
    }


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getThirdType() {
		return thirdType;
	}


	public void setThirdType(String thirdType) {
		this.thirdType = thirdType;
	}

	public String getProvinceName() {
		return provinceName;
	}


	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSex() {
		return sex;
	}


	public void setSex(String sex) {
		this.sex = sex;
	}


	public String getIntroduction() {
		return introduction;
	}


	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}


	public short getLevel() {
		return level;
	}


	public void setLevel(short level) {
		this.level = level;
	}

	public String getHeadImg() {
		return headImg;
	}


	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}


	public BigDecimal getRestMoney() {
		return restMoney;
	}


	public void setRestMoney(BigDecimal restMoney) {
		this.restMoney = restMoney;
	}


	public Integer getScore() {
		return score;
	}


	public void setScore(Integer score) {
		this.score = score;
	}


	public Short getUserType() {
		return userType;
	}


	public void setUserType(Short userType) {
		this.userType = userType;
	}

	public Short getAddFrom() {
		return addFrom;
	}


	public void setAddFrom(Short addFrom) {
		this.addFrom = addFrom;
	}


	public Long getAddTime() {
		return addTime;
	}


	public void setAddTime(Long addTime) {
		this.addTime = addTime;
	}


	public Long getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public String getRealName() {
		return realName;
	}


	public void setRealName(String realName) {
		this.realName = realName;
	}


	public String getIdCard() {
		return idCard;
	}


	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getOpenid() {
		return openid;
	}


	public void setOpenid(String openid) {
		this.openid = openid;
	}


	public String getQrCode() {
		return qrCode;
	}


	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}


	public Integer getExp() {
		return exp;
	}


	public void setExp(Integer exp) {
		this.exp = exp;
	}

	
}