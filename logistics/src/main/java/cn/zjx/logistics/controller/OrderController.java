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
		
		// ������ѯ����
		OrderExample example = new OrderExample();
		// �����������ƶ���
		Criteria criteria = example.createCriteria();
		
		//�жϸý�ɫ�Ƿ�Ϊҵ��Ա
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		if(user.getRolename().equals("ҵ��Ա")) {
			//��ѯ���ͻ����� user_id Ϊ��ǰ��ݵ���Ϣ
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
	
	//���ݶ�����id��ѯ��������ϸ
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
		
		MessageObject mo = new MessageObject(0, "ɾ������ʧ�ܣ�����ϵ����Ա");
			// ִ��ɾ������
			int row = orderService.deleteByPrimaryKey(orderId);
			if (row == 1) {
				mo = new MessageObject(1, "ɾ�����ݳɹ�");
			}
		return mo;
	}

	// �༭����
	@RequestMapping("/edit")
	public String edit(Model m, Long orderId) {
		/*
		 * �����޸Ķ���Ҫ��ѯ��ʾ���û�ѡ������
		 * 
		 * 1��ҵ��Ա
		 * 2���ͻ�
		 * 3����������
		 * 4�����ʽ
		 * 5�����˷�ʽ
		 * 6��ȡ����ʽ
		 * 7����λ
		 * 
		 */
		
		//1.��ѯ����ҵ��Ա
		UserExample example = new UserExample();
		cn.zjx.logistics.pojo.UserExample.Criteria criteria = example.createCriteria();
		criteria.andRolenameEqualTo(Constant.ROLE_SALESMAN);
		List<User> users = userService.selectByExample(example);
		m.addAttribute("users", users);
		//2���ͻ�
		
		CustomerExample customerExample = new CustomerExample();
		List<Customer> customers=customerService.selectByExample(customerExample);
		 m.addAttribute("customers", customers);
		
		//3.��ѯ��������
		List<BaseData> intervals=baseDataService.selectBaseDataByParentName(Constant.BASIC_COMMON_INTERVAL);
		m.addAttribute("intervals", intervals);
		// 4�����ʽ
		List<BaseData> payments  = baseDataService.selectBaseDataByParentName(Constant.BASIC_PAYMENT_TYPE);
		m.addAttribute("payments", payments);
		 //5�����˷�ʽ
		List<BaseData> freights = baseDataService.selectBaseDataByParentName(Constant.BASIC_FREIGHT_TYPE);
		m.addAttribute("freights", freights);
		// 6��ȡ����ʽ
		List<BaseData> fetchTypes = baseDataService.selectBaseDataByParentName(Constant.BASIC_FETCH_TYPE);
		m.addAttribute("fetchTypes", fetchTypes);
		//7����λ
		List<BaseData> units  = baseDataService.selectBaseDataByParentName(Constant.BASIC_UNIT);
		m.addAttribute("units", units);
		// ����id��ѯ��Order�����Թ��޸ĵĻ���
		if (orderId != null) {
			Order order = orderService.selectByPrimaryKey(orderId);
			m.addAttribute("order", order);
		}


		return "orderEdit";
	}

	
	// ��������
	@RequestMapping("/insert")
	@ResponseBody
	@RequiresPermissions("order:insert")
	public MessageObject insert(@RequestBody Order order) {

		MessageObject mo = new MessageObject(0, "��������ʧ�ܣ�����ϵ����Ա");

		// ִ����������
		int row = orderService.insert(order);
		if (row == 1) {
			mo = new MessageObject(1, "�������ݳɹ�");
		}

		return mo;
	}

	// �޸Ĳ���
	@RequestMapping("/update")
	@ResponseBody
	@RequiresPermissions("order:update")
	public MessageObject update(Order order) {

		MessageObject mo = new MessageObject(0, "�޸�����ʧ�ܣ�����ϵ����Ա");

		// ִ���޸Ĳ���
		int row = orderService.updateByPrimaryKeySelective(order);
		if (row == 1) {
			mo = new MessageObject(1, "�޸����ݳɹ�");
		}
		return mo;
	}
}
