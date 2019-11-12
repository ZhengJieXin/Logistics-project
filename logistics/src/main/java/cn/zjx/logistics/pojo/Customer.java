package cn.zjx.logistics.pojo;

import java.io.Serializable;

public class Customer implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2027275358114775806L;

	private Long customerId;

    private String customerName;

    private String phone;

    private String email;

    private String address;

    private Integer gender;

    private String remark;

    private String idCard;

    private Long userId;

    private Long baseId;
    
    private String realName;
    
    private String baseName;

    public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBaseId() {
        return baseId;
    }

    public void setBaseId(Long baseId) {
        this.baseId = baseId;
    }

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", customerName=" + customerName + ", phone=" + phone + ", email="
				+ email + ", address=" + address + ", gender=" + gender + ", remark=" + remark + ", idCard=" + idCard
				+ ", userId=" + userId + ", baseId=" + baseId + ", realName=" + realName + ", baseName=" + baseName
				+ "]";
	}
    
    
    
}