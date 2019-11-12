package cn.zjx.logistics.mapper;

import cn.zjx.logistics.pojo.Order;
import cn.zjx.logistics.pojo.OrderDetail;
import cn.zjx.logistics.pojo.OrderExample;
import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(Order record);

    int insertSelective(Order record);

    List<Order> selectByExample(OrderExample example);

    Order selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

	List<OrderDetail> selectOrderDetailsByOrderId(Long orderId);
}