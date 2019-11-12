package cn.zjx.logistics.controller;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.zjx.logistics.mo.MessageObject;
import cn.zjx.logistics.pojo.BaseData;
import cn.zjx.logistics.pojo.Customer;
import cn.zjx.logistics.pojo.CustomerExample;
import cn.zjx.logistics.pojo.Order;
import cn.zjx.logistics.pojo.OrderDetail;
import cn.zjx.logistics.pojo.OrderExample;
import cn.zjx.logistics.pojo.OrderExample.Criteria;
import cn.zjx.logistics.pojo.User;
import cn.zjx.logistics.pojo.UserExample;
import cn.zjx.logistics.service.BaseDataService;
import cn.zjx.logistics.service.CustomerService;
import cn.zjx.logistics.service.OrderService;
import cn.zjx.logistics.service.UserService;
import cn.zjx.logistics.utils.Constant;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BaseDataService baseDataService;
	
	@Autowired
	private CustomerService customerService;

	@RequestMapping("/orderPage")
	public String orderPage() {

		return "orderPage";
	}
	

	@RequestMapping("/list")
	@ResponseBody
	@RequiresPermissions("order:list")
	public PageInfo<Order> list(@RequestParam(defaultValue = "1") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize, String keyword) {

		PageHelper.startPage(pageNum, pageSize);
		
		// 条件查询对象
		OrderExample example = new OrderExample();
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
			
			
			
			
		}
	 
		
		  
		List<Order> orders = orderService.selectByExample(example);
		for (Order order : orders) {
			System.out.println(order);
		}

		PageInfo<Order> pageInfo = new PageInfo<Order>(orders);

		return pageInfo;
	}
	
	//根据订单的id查询出订单明细
	@RequestMapping("/detail")
	@ResponseBody
	public List<OrderDetail> detail(Long orderId){
		
		List<OrderDetail> orderDetails = orderService.selectOrderDetailsByOrderId(orderId);
		
		return orderDetails;
	}
	
	
	@RequestMapping("/delete")
	@ResponseBody
	@RequiresPermissions("order:delete")
	public MessageObject delete(Long orderId) {
		
		MessageObject mo = new MessageObject(0, "删除数据失败，请联系管理员");
			// 执行删除操作
			int row = orderService.deleteByPrimaryKey(orderId);
			if (row == 1) {
				mo = new MessageObject(1, "删除数据成功");
			}
		return mo;
	}

	// 编辑功能
	@RequestMapping("/edit")
	public String edit(Model m, Long orderId) {
		/*
		 * 新增修改订单要查询显示给用户选择数据
		 * 
		 * 1，业务员
		 * 2，客户
		 * 3，到达区域
		 * 4，付款方式
		 * 5，货运方式
		 * 6，取件方式
		 * 7，单位
		 * 
		 */
		
		//1.查询所有业务员
		UserExample example = new UserExample();
		cn.zjx.logistics.pojo.UserExample.Criteria criteria = example.createCriteria();
		criteria.andRolenameEqualTo(Constant.ROLE_SALESMAN);
		List<User> users = userService.selectByExample(example);
		m.addAttribute("users", users);
		//2，客户
		
		CustomerExample customerExample = new CustomerExample();
		List<Customer> customers=customerService.selectByExample(customerExample);
		 m.addAttribute("customers", customers);
		
		//3.查询所有区间
		List<BaseData> intervals=baseDataService.selectBaseDataByParentName(Constant.BASIC_COMMON_INTERVAL);
		m.addAttribute("intervals", intervals);
		// 4，付款方式
		List<BaseData> payments  = baseDataService.selectBaseDataByParentName(Constant.BASIC_PAYMENT_TYPE);
		m.addAttribute("payments", payments);
		 //5，货运方式
		List<BaseData> freights = baseDataService.selectBaseDataByParentName(Constant.BASIC_FREIGHT_TYPE);
		m.addAttribute("freights", freights);
		// 6，取件方式
		List<BaseData> fetchTypes = baseDataService.selectBaseDataByParentName(Constant.BASIC_FETCH_TYPE);
		m.addAttribute("fetchTypes", fetchTypes);
		//7，单位
		List<BaseData> units  = baseDataService.selectBaseDataByParentName(Constant.BASIC_UNIT);
		m.addAttribute("units", units);
		// 根据id查询出Order对象，以供修改的回显
		if (orderId != null) {
			Order order = orderService.selectByPrimaryKey(orderId);
			m.addAttribute("order", order);
		}


		return "orderEdit";
	}

	
	// 新增操作
	@RequestMapping("/insert")
	@ResponseBody
	@RequiresPermissions("order:insert")
	public MessageObject insert(@RequestBody Order order) {

		MessageObject mo = new MessageObject(0, "新增数据失败，请联系管理员");

		// 执行新增操作
		int row = orderService.insert(order);
		if (row == 1) {
			mo = new MessageObject(1, "新增数据成功");
		}

		return mo;
	}

	// 修改操作
	@RequestMapping("/update")
	@ResponseBody
	@RequiresPermissions("order:update")
	public MessageObject update(Order order) {

		MessageObject mo = new MessageObject(0, "修改数据失败，请联系管理员");

		// 执行修改操作
		int row = orderService.updateByPrimaryKeySelective(order);
		if (row == 1) {
			mo = new MessageObject(1, "修改数据成功");
		}
		return mo;
	}
}
