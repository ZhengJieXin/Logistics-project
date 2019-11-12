package cn.zjx.logistics.pojo;

import java.io.Serializable;

public class OrderDetail implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6409445632947598153L;

	private Long orderDetailId;

    private Long orderId;

    private String goodsName;

    private Integer goodsNumber;

    private Long goodsUnit;

    private Long goodsUnitPrice;

    private Long goodsTotal;

    private String goodsRemark;

    public Long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(Integer goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public Long getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(Long goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public Long getGoodsUnitPrice() {
        return goodsUnitPrice;
    }

    public void setGoodsUnitPrice(Long goodsUnitPrice) {
        this.goodsUnitPrice = goodsUnitPrice;
    }

    public Long getGoodsTotal() {
        return goodsTotal;
    }

    public void setGoodsTotal(Long goodsTotal) {
        this.goodsTotal = goodsTotal;
    }

    public String getGoodsRemark() {
        return goodsRemark;
    }

    public void setGoodsRemark(String goodsRemark) {
        this.goodsRemark = goodsRemark;
    }

	@Override
	public String toString() {
		return "OrderDetail [orderDetailId=" + orderDetailId + ", orderId=" + orderId + ", goodsName=" + goodsName
				+ ", goodsNumber=" + goodsNumber + ", goodsUnit=" + goodsUnit + ", goodsUnitPrice=" + goodsUnitPrice
				+ ", goodsTotal=" + goodsTotal + ", goodsRemark=" + goodsRemark + "]";
	}
    
    
}