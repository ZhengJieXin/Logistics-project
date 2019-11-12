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
		
		// ������ѯ����
		CustomerExample example = new CustomerExample();
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
			
			criteria.andCustomerNameLike("%" + keyword + "%");
			//�绰��ѯ
			CustomerExample example2 = new CustomerExample();
			Criteria criteria2 = example2.createCriteria();
			 criteria2.andPhoneLike("%"+keyword+"%");
			 example.or(criteria2);
			 //�����ѯ
			 CustomerExample example3 = new CustomerExample();
			 Criteria criteria3 = example3.createCriteria();
			 criteria3.andEmailLike("%"+keyword+"%");
			 example.or(criteria3);
			 //�ͻ���ַ
			 CustomerExample example4 = new CustomerExample();
			 Criteria criteria4 = example4.createCriteria();
			 criteria4.andAddressLike("%"+keyword+"%");
			 example.or(criteria4);
			 //�ͻ�����
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
		
		MessageObject mo = new MessageObject(0, "ɾ������ʧ�ܣ�����ϵ����Ա");
			// ִ��ɾ������
			int row = customerService.deleteByPrimaryKey(customerId);
			if (row == 1) {
				mo = new MessageObject(1, "ɾ�����ݳɹ�");
			}
		return mo;
	}

	// �༭����
	@RequestMapping("/edit")
	public String edit(Model m, Long customerId) {
		
		
		//����ǰ��֤�����
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		m.addAttribute("user", user);
		
		
		//��ѯ����ҵ��Ա
		UserExample example = new UserExample();
		cn.zjx.logistics.pojo.UserExample.Criteria criteria = example.createCriteria();
		criteria.andRolenameEqualTo("ҵ��Ա");
		List<User> users = userService.selectByExample(example);
		
		m.addAttribute("users", users);
		
		//��ѯ��������
		List<BaseData> baseDatas=baseDataService.selectBaseDataByParentName("�������");
		m.addAttribute("baseDatas", baseDatas);
		
		// ����id��ѯ��Customer�����Թ��޸ĵĻ���
		if (customerId != null) {
			Customer customer = customerService.selectByPrimaryKey(customerId);
			m.addAttribute("customer", customer);
		}


		return "customerEdit";
	}

	// ���Ȩ���Ƿ����
	@RequestMapping("/checkCustomername")
	@ResponseBody
	
	public boolean checkCustomername(String customerName) {

		CustomerExample example = new CustomerExample();
		Criteria criteria = example.createCriteria();
		criteria.andCustomerNameEqualTo(customerName);

		List<Customer> customers = customerService.selectByExample(example);

		return customers.size() > 0 ? false : true;
	}

	// ��������
	@RequestMapping("/insert")
	@ResponseBody
	@RequiresPermissions("customer:insert")
	public MessageObject insert(Customer customer) {

		MessageObject mo = new MessageObject(0, "��������ʧ�ܣ�����ϵ����Ա");

		// ִ����������
		int row = customerService.insert(customer);
		if (row == 1) {
			mo = new MessageObject(1, "�������ݳɹ�");
		}

		return mo;
	}

	// �޸Ĳ���
	@RequestMapping("/update")
	@ResponseBody
	@RequiresPermissions("customer:update")
	public MessageObject update(Customer customer) {

		MessageObject mo = new MessageObject(0, "�޸�����ʧ�ܣ�����ϵ����Ա");

		// ִ���޸Ĳ���
		int row = customerService.updateByPrimaryKeySelective(customer);
		if (row == 1) {
			mo = new MessageObject(1, "�޸����ݳɹ�");
		}
		return mo;
	}
}
