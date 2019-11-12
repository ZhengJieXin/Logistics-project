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
import cn.zjx.logistics.pojo.BaseData;
import cn.zjx.logistics.pojo.BaseDataExample;
import cn.zjx.logistics.pojo.BaseDataExample.Criteria;
import cn.zjx.logistics.pojo.BaseData;
import cn.zjx.logistics.pojo.BaseDataExample;
import cn.zjx.logistics.service.BaseDataService;
import cn.zjx.logistics.service.BaseDataService;

@Controller
@RequestMapping("/baseData")
public class BaseDataController {

	@Autowired
	private BaseDataService baseDataService;
	
	@Autowired
	private BaseDataService userService;

	@RequestMapping("/baseDataPage")
	public String baseDataPage() {

		return "baseDataPage";
	}

	@RequestMapping("/list")
	@ResponseBody
	@RequiresPermissions("basicData:list")
	public PageInfo<BaseData> list(@RequestParam(defaultValue = "1") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize, String keyword) {

		PageHelper.startPage(pageNum, pageSize);

		// ������ѯ����
		BaseDataExample example = new BaseDataExample();

		if (StringUtils.isNotBlank(keyword)) {
			// �����������ƶ���
			Criteria criteria = example.createCriteria();
			criteria.andBaseNameLike("%" + keyword + "%");
		}

		List<BaseData> baseDatas = baseDataService.selectByExample(example);
		for (BaseData baseData : baseDatas) {
			System.out.println(baseData);
		}

		PageInfo<BaseData> pageInfo = new PageInfo<BaseData>(baseDatas);

		return pageInfo;
	}

	@RequestMapping("/delete")
	@ResponseBody
	@RequiresPermissions("basicData:delete")
	public MessageObject delete(Long baseId) {
		
		MessageObject mo = new MessageObject(0, "ɾ������ʧ�ܣ�����ϵ����Ա");
		//ɾ����ɫ֮ǰ�ȸ���Ҫ��ɾ���Ľ�ɫȥ�û����в�ѯ�Ƿ�ӵ�д˽�ɫ���û�������У�����ɾ��
		
		BaseDataExample example = new BaseDataExample();
		Criteria criteria = example.createCriteria();
		criteria.andBaseIdEqualTo(baseId);
		
		List<BaseData> users = userService.selectByExample(example );
		
		if(users.size() > 0) {
			mo = new MessageObject(0, "�˽�ɫ��ӵ���û�������ֱ��ɾ��");
		}else {
			// ִ��ɾ������
			int row = baseDataService.deleteByPrimaryKey(baseId);
			if (row == 1) {
				mo = new MessageObject(1, "ɾ�����ݳɹ�");
			}

		}
		

		

		
		return mo;
	}

	// �༭����
	@RequestMapping("/edit")
	public String edit(Model m, Long baseId) {

		// ����id��ѯ��BaseData�����Թ��޸ĵĻ���
		if (baseId != null) {
			BaseData baseData = baseDataService.selectByPrimaryKey(baseId);
			m.addAttribute("baseData", baseData);
		}
						
		BaseDataExample example = new BaseDataExample();
		Criteria criteria = example.createCriteria();
		//�ж�ParentId�Ƿ�Ϊ��
		criteria.andParentIdIsNull();
		List<BaseData> parents  = baseDataService.selectByExample(example);
			m.addAttribute("parents", parents);
		
		return "baseDataEdit";
	}

	// ���Ȩ���Ƿ����
	@RequestMapping("/checkBaseDataName")
	@ResponseBody
	public boolean checkBaseDataName(String baseName) {

		BaseDataExample example = new BaseDataExample();
		Criteria criteria = example.createCriteria();
		criteria.andBaseNameEqualTo(baseName);

		List<BaseData> baseDatas = baseDataService.selectByExample(example);

		return baseDatas.size() > 0 ? false : true;
	}

	// ��������
	@RequestMapping("/insert")
	@ResponseBody
	@RequiresPermissions("basicData:insert")
	public MessageObject insert(BaseData baseData) {

		MessageObject mo = new MessageObject(0, "��������ʧ�ܣ�����ϵ����Ա");

		// ִ����������
		int row = baseDataService.insert(baseData);
		if (row == 1) {
			mo = new MessageObject(1, "�������ݳɹ�");
		}

		return mo;
	}

	// �޸Ĳ���
	@RequestMapping("/update")
	@ResponseBody
	@RequiresPermissions("basicData:update")
	public MessageObject update(BaseData baseData) {

		MessageObject mo = new MessageObject(0, "�޸�����ʧ�ܣ�����ϵ����Ա");

		// ִ���޸Ĳ���
		int row = baseDataService.updateByPrimaryKeySelective(baseData);
		if (row == 1) {
			mo = new MessageObject(1, "�޸����ݳɹ�");
		}
		return mo;
	}
}
