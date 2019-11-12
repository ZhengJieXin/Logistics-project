package cn.zjx.logistics.service;

import java.util.List;

import cn.zjx.logistics.pojo.Customer;
import cn.zjx.logistics.pojo.CustomerExample;

public interface CustomerService {
	int deleteByPrimaryKey(Long customerId);

	int insert(Customer record);
	
	int updateByPrimaryKeySelective(Customer record);

	List<Customer> selectByExample(CustomerExample example);

	Customer selectByPrimaryKey(Long customerId);

	


	

}
