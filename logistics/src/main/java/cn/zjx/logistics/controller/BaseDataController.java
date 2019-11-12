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

		// 条件查询对象
		BaseDataExample example = new BaseDataExample();

		if (StringUtils.isNotBlank(keyword)) {
			// 创建条件限制对象
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
		
		MessageObject mo = new MessageObject(0, "删除数据失败，请联系管理员");
		//删除角色之前先根据要被删除的角色去用户表中查询是否还拥有此角色的用户，如果有，不能删除
		
		BaseDataExample example = new BaseDataExample();
		Criteria criteria = example.createCriteria();
		criteria.andBaseIdEqualTo(baseId);
		
		List<BaseData> users = userService.selectByExample(example );
		
		if(users.size() > 0) {
			mo = new MessageObject(0, "此角色还拥有用户，不能直接删除");
		}else {
			// 执行删除操作
			int row = baseDataService.deleteByPrimaryKey(baseId);
			if (row == 1) {
				mo = new MessageObject(1, "删除数据成功");
			}

		}
		

		

		
		return mo;
	}

	// 编辑功能
	@RequestMapping("/edit")
	public String edit(Model m, Long baseId) {

		// 根据id查询出BaseData对象，以供修改的回显
		if (baseId != null) {
			BaseData baseData = baseDataService.selectByPrimaryKey(baseId);
			m.addAttribute("baseData", baseData);
		}
						
		BaseDataExample example = new BaseDataExample();
		Criteria criteria = example.createCriteria();
		//判断ParentId是否为空
		criteria.andParentIdIsNull();
		List<BaseData> parents  = baseDataService.selectByExample(example);
			m.addAttribute("parents", parents);
		
		return "baseDataEdit";
	}

	// 检查权限是否存在
	@RequestMapping("/checkBaseDataName")
	@ResponseBody
	public boolean checkBaseDataName(String baseName) {

		BaseDataExample example = new BaseDataExample();
		Criteria criteria = example.createCriteria();
		criteria.andBaseNameEqualTo(baseName);

		List<BaseData> baseDatas = baseDataService.selectByExample(example);

		return baseDatas.size() > 0 ? false : true;
	}

	// 新增操作
	@RequestMapping("/insert")
	@ResponseBody
	@RequiresPermissions("basicData:insert")
	public MessageObject insert(BaseData baseData) {

		MessageObject mo = new MessageObject(0, "新增数据失败，请联系管理员");

		// 执行新增操作
		int row = baseDataService.insert(baseData);
		if (row == 1) {
			mo = new MessageObject(1, "新增数据成功");
		}

		return mo;
	}

	// 修改操作
	@RequestMapping("/update")
	@ResponseBody
	@RequiresPermissions("basicData:update")
	public MessageObject update(BaseData baseData) {

		MessageObject mo = new MessageObject(0, "修改数据失败，请联系管理员");

		// 执行修改操作
		int row = baseDataService.updateByPrimaryKeySelective(baseData);
		if (row == 1) {
			mo = new MessageObject(1, "修改数据成功");
		}
		return mo;
	}
}
