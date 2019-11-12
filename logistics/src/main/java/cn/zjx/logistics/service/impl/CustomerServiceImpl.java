package cn.zjx.logistics.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zjx.logistics.mapper.CustomerMapper;
import cn.zjx.logistics.pojo.Customer;
import cn.zjx.logistics.pojo.CustomerExample;
import cn.zjx.logistics.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerMapper customerMapper;
	
	@Override
	public int deleteByPrimaryKey(Long customerId) {
		return customerMapper.deleteByPrimaryKey(customerId);
	}

	@Override
	public int insert(Customer record) {
		return customerMapper.insert(record);
	}


	@Override
	public List<Customer> selectByExample(CustomerExample example) {
		return customerMapper.selectByExample(example);
	}

	@Override
	public Customer selectByPrimaryKey(Long customerId) {
		return customerMapper.selectByPrimaryKey(customerId);
	}

	@Override
	public int updateByPrimaryKeySelective(Customer record) {
		return customerMapper.updateByPrimaryKeySelective(record);
	}



}
