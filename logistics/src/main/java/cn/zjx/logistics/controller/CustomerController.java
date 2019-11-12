package cn.zjx.logistics.controller;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.zjx.logistics.mo.MessageObject;
import cn.zjx.logistics.pojo.BaseData;
import cn.zjx.logistics.pojo.Customer;
import cn.zjx.logistics.pojo.CustomerExample;
import cn.zjx.logistics.pojo.CustomerExample.Criteria;
import cn.zjx.logistics.pojo.User;
import cn.zjx.logistics.pojo.UserExample;
import cn.zjx.logistics.service.BaseDataService;
import cn.zjx.logistics.service.CustomerService;
import cn.zjx.logistics.service.UserService;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BaseDataService baseDataService;

	@RequestMapping("/customerPage")
	public String customerPage() {

		return "customerPage";
	}
	

	@RequestMapping("/list")
	@ResponseBody
	@RequiresPermissions("customer:list")
	public PageInfo<Customer> list(@RequestParam(defaultValue = "1") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize, String keyword) {

		PageHelper.startPage(pageNum, pageSize);
		
		// 条件查询对象
		CustomerExample example = new CustomerExample();
		// 创建条件限制对象
		Criteria criteria = example.createCriteria();
		
		//判断该角色是否为业务员
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		if(user.getRolename().equals("业务员")) {
			//查询出客户表中 user_id 为当前身份的信息
			criteria.andUserIdEqualTo(user.getUserId());
			
		}
		if (StringUtils.isNotBlank(keyword)) {
			
			criteria.andCustomerNameLike("%" + keyword + "%");
			//电话查询
			CustomerExample example2 = new CustomerExample();
			Criteria criteria2 = example2.createCriteria();
			 criteria2.andPhoneLike("%"+keyword+"%");
			 example.or(criteria2);
			 //邮箱查询
			 CustomerExample example3 = new CustomerExample();
			 Criteria criteria3 = example3.createCriteria();
			 criteria3.andEmailLike("%"+keyword+"%");
			 example.or(criteria3);
			 //客户地址
			 CustomerExample example4 = new CustomerExample();
			 Criteria criteria4 = example4.createCriteria();
			 criteria4.andAddressLike("%"+keyword+"%");
			 example.or(criteria4);
			 //客户描述
			 CustomerExample example5 = new CustomerExample();
			 Criteria criteria5 = example5.createCriteria();
			 criteria5.andRemarkLike("%"+keyword+"%");
			 example.or(criteria5);
		}
		 
	
			 
		
		  
		List<Customer> customers = customerService.selectByExample(example);
		for (Customer customer : customers) {
			System.out.println(customer);
		}

		PageInfo<Customer> pageInfo = new PageInfo<Customer>(customers);

		return pageInfo;
	}

	@RequestMapping("/delete")
	@ResponseBody
	@RequiresPermissions("customer:delete")
	public MessageObject delete(Long customerId) {
		
		MessageObject mo = new MessageObject(0, "删除数据失败，请联系管理员");
			// 执行删除操作
			int row = customerService.deleteByPrimaryKey(customerId);
			if (row == 1) {
				mo = new MessageObject(1, "删除数据成功");
			}
		return mo;
	}

	// 编辑功能
	@RequestMapping("/edit")
	public String edit(Model m, Long customerId) {
		
		
		//共享当前认证的身份
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		m.addAttribute("user", user);
		
		
		//查询所有业务员
		UserExample example = new UserExample();
		cn.zjx.logistics.pojo.UserExample.Criteria criteria = example.createCriteria();
		criteria.andRolenameEqualTo("业务员");
		List<User> users = userService.selectByExample(example);
		
		m.addAttribute("users", users);
		
		//查询所有区间
		List<BaseData> baseDatas=baseDataService.selectBaseDataByParentName("区间管理");
		m.addAttribute("baseDatas", baseDatas);
		
		// 根据id查询出Customer对象，以供修改的回显
		if (customerId != null) {
			Customer customer = customerService.selectByPrimaryKey(customerId);
			m.addAttribute("customer", customer);
		}


		return "customerEdit";
	}

	// 检查权限是否存在
	@RequestMapping("/checkCustomername")
	@ResponseBody
	
	public boolean checkCustomername(String customerName) {

		CustomerExample example = new CustomerExample();
		Criteria criteria = example.createCriteria();
		criteria.andCustomerNameEqualTo(customerName);

		List<Customer> customers = customerService.selectByExample(example);

		return customers.size() > 0 ? false : true;
	}

	// 新增操作
	@RequestMapping("/insert")
	@ResponseBody
	@RequiresPermissions("customer:insert")
	public MessageObject insert(Customer customer) {

		MessageObject mo = new MessageObject(0, "新增数据失败，请联系管理员");

		// 执行新增操作
		int row = customerService.insert(customer);
		if (row == 1) {
			mo = new MessageObject(1, "新增数据成功");
		}

		return mo;
	}

	// 修改操作
	@RequestMapping("/update")
	@ResponseBody
	@RequiresPermissions("customer:update")
	public MessageObject update(Customer customer) {

		MessageObject mo = new MessageObject(0, "修改数据失败，请联系管理员");

		// 执行修改操作
		int row = customerService.updateByPrimaryKeySelective(customer);
		if (row == 1) {
			mo = new MessageObject(1, "修改数据成功");
		}
		return mo;
	}
}
