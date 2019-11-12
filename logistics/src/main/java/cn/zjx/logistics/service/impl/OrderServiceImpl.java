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
		 * ��������ҵ�� ˼·
		 * 
		 * 1�����붩��ͬʱ��ȡ����������id
		 * 
		 * 2�����붩����ϸ
		 * 	����֮ǰ�����ö�����ϸ��Ӧ�Ķ���id
		 * 
		 */
		System.out.println("record����ǰ:"+record);
		int row = orderMapper.insert(record);
		
		if(row==1) {
		List<OrderDetail> orderDetails = record.getOrderDetails();
		
		//���붩����ϸ
		for (OrderDetail orderDetail : orderDetails) {
			//����ÿ��������ϸ��orderId
			orderDetail.setOrderId(record.getOrderId());
			System.out.println("������:"+orderDetail);
			//��������ϸ���뵽���ݿ�
			orderDetailMapper.insert(orderDetail);
			
		}
		}
		
		return row ;
		
	}



}
