package cn.zjx.logistics.pojo;

import java.io.Serializable;

public class BaseData implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2425509740146093892L;

	private Long baseId;

    private String baseName;

    private String baseDesc;

    private Long parentId;
    
    private String parentName;

    public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Long getBaseId() {
        return baseId;
    }

    public void setBaseId(Long baseId) {
        this.baseId = baseId;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getBaseDesc() {
        return baseDesc;
    }

    public void setBaseDesc(String baseDesc) {
        this.baseDesc = baseDesc;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}