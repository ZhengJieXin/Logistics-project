package cn.zjx.logistics.service;

import java.util.List;

import cn.zjx.logistics.pojo.Order;
import cn.zjx.logistics.pojo.OrderDetail;
import cn.zjx.logistics.pojo.OrderExample;

public interface OrderService {
	int deleteByPrimaryKey(Long orderId);

	int insert(Order record);
	
	int updateByPrimaryKeySelective(Order record);

	List<Order> selectByExample(OrderExample example);

	Order selectByPrimaryKey(Long orderId);

	List<OrderDetail> selectOrderDetailsByOrderId(Long orderId);


	

}
