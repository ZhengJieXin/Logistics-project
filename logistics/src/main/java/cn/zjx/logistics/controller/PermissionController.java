package cn.zjx.logistics.controller;

import java.util.Date;
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
import cn.zjx.logistics.pojo.Permission;
import cn.zjx.logistics.pojo.PermissionExample;
import cn.zjx.logistics.pojo.PermissionExample.Criteria;
import cn.zjx.logistics.service.RoleService;
import cn.zjx .logistics.service.PermissionService;

@Controller
@RequestMapping("/permission")
public class PermissionController {
	
	@Autowired
	private PermissionService permissionService;
	
	@RequestMapping("/permissionPage")
	public String permissionPage() {
		
		return "permissionPage";
	}
	
	@RequestMapping("/list")
	@ResponseBody
	@RequiresPermissions("permission:list")
	public PageInfo<Permission> list(@RequestParam(defaultValue = "1")Integer pageNum,
								@RequestParam(defaultValue = "10")Integer pageSize,
								String keyword){

		PageHelper.startPage(pageNum, pageSize);
		
		//������ѯ����
		PermissionExample example = new PermissionExample();
		
		
		if(StringUtils.isNotBlank(keyword)) {
			//�����������ƶ���
			Criteria criteria = example.createCriteria();
			criteria.andNameLike("%"+keyword+"%");
		}
		
		
		List<Permission> permissions = permissionService.selectByExample(example );
		for (Permission permission : permissions) {
			System.out.println(permission);
		}
		
		PageInfo<Permission> pageInfo = new PageInfo<Permission>(permissions);
		
		return pageInfo;
	}
	@RequestMapping("/getAllPermissions")
	@ResponseBody
	public List<Permission> getAllPermissions() {
		PermissionExample example=new PermissionExample();
		List<Permission> permissions = permissionService.selectByExample(example);
		
		return permissions;
	}
	@RequestMapping("/delete")
	@ResponseBody
	@RequiresPermissions("permission:delete")
	public MessageObject delete(Long permissionId) {
		
		MessageObject mo = new MessageObject(0, "ɾ������ʧ�ܣ�����ϵ����Ա");
		
		
		//ɾ��֮ǰ���ж� ��ǰ��ɾ����Ȩ���Ƿ�����Ȩ��
		
		PermissionExample example = new PermissionExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(permissionId);
		
		List<Permission> children = permissionService.selectByExample(example);
		
		//����Ȩ�ޣ�����ɾ��
		if(children.size() > 0) {
			mo = new MessageObject(0, "��Ȩ�޻�����Ȩ�ޣ�����ɾ��");
		}else {
			//ִ��ɾ������
			int row = permissionService.deleteByPrimaryKey(permissionId);
			if(row == 1) {
				mo = new MessageObject(1, "ɾ�����ݳɹ�");
			}
		}
		
		
		
		return mo;
	}
	//�༭����
	@RequestMapping("/edit")
	public String edit(Model m,Long permissionId) {
		
		//����id��ѯ��Permission�����Թ��޸ĵĻ���
		if(permissionId !=null) {
			Permission permission = permissionService.selectByPrimaryKey(permissionId);
			m.addAttribute("permission", permission);
		}
		
		
		//��ѯ���е�Ȩ����Ϊ��Ȩ��ѡ��
		PermissionExample example = new PermissionExample();
		List<Permission> permissions = permissionService.selectByExample(example );
		m.addAttribute("permissions", permissions);
		
		return "permissionEdit";
	}
	
	//���Ȩ���Ƿ����
	@RequestMapping("/checkPermissionName")
	@ResponseBody
	public boolean checkPermissionname(String permissionname) {
		
		PermissionExample example = new PermissionExample();
		Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(permissionname);
		
		List<Permission> permissions = permissionService.selectByExample(example);
		
		return permissions.size() > 0 ? false : true; 
	}
	
	
	
	
	//��������
	@RequestMapping("/insert")
	@ResponseBody
	@RequiresPermissions("permission:insert")
	public MessageObject insert(Permission permission) {
		
		MessageObject mo = new MessageObject(0, "��������ʧ�ܣ�����ϵ����Ա");
		
		//ִ����������
		int row = permissionService.insert(permission);
		if(row == 1) {
			mo = new MessageObject(1, "�������ݳɹ�");
		}
		
		return mo;
	}
	
	//�޸Ĳ���
	@RequestMapping("/update")
	@ResponseBody
	@RequiresPermissions("permission:update")
	public MessageObject update(Permission permission) {
		
		MessageObject mo = new MessageObject(0, "�޸�����ʧ�ܣ�����ϵ����Ա");
		
		//ִ���޸Ĳ���
		int row = permissionService.updateByPrimaryKeySelective(permission);
		if(row == 1) {
			mo = new MessageObject(1, "�޸����ݳɹ�");
		}
		return mo;
	}	
}
