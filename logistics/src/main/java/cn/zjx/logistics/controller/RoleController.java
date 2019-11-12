package cn.zjx.logistics.controller;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.zjx.logistics.mo.MessageObject;
import cn.zjx.logistics.pojo.Role;
import cn.zjx.logistics.pojo.RoleExample;
import cn.zjx.logistics.pojo.RoleExample.Criteria;
import cn.zjx.logistics.pojo.User;
import cn.zjx.logistics.pojo.UserExample;
import cn.zjx.logistics.service.RoleService;
import cn.zjx.logistics.service.UserService;

@Controller
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;

	@RequestMapping("/rolePage")
	public String rolePage() {

		return "rolePage";
	}
	

	@RequestMapping("/list")
	@ResponseBody
	@RequiresPermissions("role:list")
	public PageInfo<Role> list(@RequestParam(defaultValue = "1") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize, String keyword) {

		PageHelper.startPage(pageNum, pageSize);

		// ������ѯ����
		RoleExample example = new RoleExample();

		if (StringUtils.isNotBlank(keyword)) {
			// �����������ƶ���
			Criteria criteria = example.createCriteria();
			criteria.andRolenameLike("%" + keyword + "%");
		}

		List<Role> roles = roleService.selectByExample(example);
		for (Role role : roles) {
			System.out.println(role);
		}

		PageInfo<Role> pageInfo = new PageInfo<Role>(roles);

		return pageInfo;
	}

	@RequestMapping("/delete")
	@ResponseBody
	@RequiresPermissions("role:delete")
	public MessageObject delete(Long roleId) {
		
		MessageObject mo = new MessageObject(0, "ɾ������ʧ�ܣ�����ϵ����Ա");
		//ɾ����ɫ֮ǰ�ȸ���Ҫ��ɾ���Ľ�ɫȥ�û����в�ѯ�Ƿ�ӵ�д˽�ɫ���û�������У�����ɾ��
		
		UserExample example = new UserExample();
		cn.zjx.logistics.pojo.UserExample.Criteria criteria = example.createCriteria();
		criteria.andRoleIdEqualTo(roleId);
		
		List<User> users = userService.selectByExample(example );
		
		if(users.size() > 0) {
			mo = new MessageObject(0, "�˽�ɫ��ӵ���û�������ֱ��ɾ��");
		}else {
			// ִ��ɾ������
			int row = roleService.deleteByPrimaryKey(roleId);
			if (row == 1) {
				mo = new MessageObject(1, "ɾ�����ݳɹ�");
			}

		}
		

		

		
		return mo;
	}

	// �༭����
	@RequestMapping("/edit")
	public String edit(Model m, Long roleId) {

		// ����id��ѯ��Role�����Թ��޸ĵĻ���
		if (roleId != null) {
			Role role = roleService.selectByPrimaryKey(roleId);
			m.addAttribute("role", role);
		}


		return "roleEdit";
	}

	// ���Ȩ���Ƿ����
	@RequestMapping("/checkRolename")
	@ResponseBody
	
	public boolean checkRolename(String rolename) {

		RoleExample example = new RoleExample();
		Criteria criteria = example.createCriteria();
		criteria.andRolenameEqualTo(rolename);

		List<Role> roles = roleService.selectByExample(example);

		return roles.size() > 0 ? false : true;
	}

	// ��������
	@RequestMapping("/insert")
	@ResponseBody
	@RequiresPermissions("role:insert")
	public MessageObject insert(Role role) {

		MessageObject mo = new MessageObject(0, "��������ʧ�ܣ�����ϵ����Ա");

		// ִ����������
		int row = roleService.insert(role);
		if (row == 1) {
			mo = new MessageObject(1, "�������ݳɹ�");
		}

		return mo;
	}

	// �޸Ĳ���
	@RequestMapping("/update")
	@ResponseBody
	@RequiresPermissions("role:update")
	public MessageObject update(Role role) {

		MessageObject mo = new MessageObject(0, "�޸�����ʧ�ܣ�����ϵ����Ա");

		// ִ���޸Ĳ���
		int row = roleService.updateByPrimaryKeySelective(role);
		if (row == 1) {
			mo = new MessageObject(1, "�޸����ݳɹ�");
		}
		return mo;
	}
}
