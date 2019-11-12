package cn.zjx.logistics.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zjx.logistics.mapper.OrderDetailMapper;
import cn.zjx.logistics.mapper.OrderMapper;
import cn.zjx.logistics.pojo.Order;
import cn.zjx.logistics.pojo.OrderDetail;
import cn.zjx.logistics.pojo.OrderDetailExample;
import cn.zjx.logistics.pojo.OrderDetailExample.Criteria;
import cn.zjx.logistics.pojo.OrderExample;
import cn.zjx.logistics.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderDetailMapper orderDetailMapper;
	
	@Override
	public int deleteByPrimaryKey(Long orderId) {
		return orderMapper.deleteByPrimaryKey(orderId);
	}


	@Override
	public List<Order> selectByExample(OrderExample example) {
		return orderMapper.selectByExample(example);
	}

	@Override
	public Order selectByPrimaryKey(Long orderId) {
		return orderMapper.selectByPrimaryKey(orderId);
	}

	@Override
	public int updateByPrimaryKeySelective(Order record) {
		return orderMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<OrderDetail> selectOrderDetailsByOrderId(Long orderId) {
		OrderDetailExample example =new OrderDetailExample();
		 Criteria criteria = example.createCriteria();
		 criteria.andOrderIdEqualTo(orderId);
		 List<OrderDetail> orderDetails  = orderDetailMapper.selectByExample(example);
		return orderDetails;
	}
	
	@Override
	public int insert(Order record) {
		/*
		 * 新增订单业务 思路
		 * 
		 * 1，插入订单同时获取订单的主键id
		 * 
		 * 2，插入订单明细
		 * 	插入之前先设置订单明细对应的订单id
		 * 
		 */
		System.out.println("record新增前:"+record);
		int row = orderMapper.insert(record);
		
		if(row==1) {
		List<OrderDetail> orderDetails = record.getOrderDetails();
		
		//插入订单明细
		for (OrderDetail orderDetail : orderDetails) {
			//设置每个订单明细的orderId
			orderDetail.setOrderId(record.getOrderId());
			System.out.println("新增后:"+orderDetail);
			//将订单明细插入到数据库
			orderDetailMapper.insert(orderDetail);
			
		}
		}
		
		return row ;
		
	}



}
