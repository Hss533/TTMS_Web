package com.ttms.controller.background;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ttms.entity.PageBean;
import com.ttms.entity.Power;
import com.ttms.service.PowerService;
import com.ttms.util.ResponseUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 后台权限Controller模块
 * @author hu
 *
 */
@Controller
@RequestMapping("/background/power")
public class PowerController 
{
	@Resource 
	private PowerService powerService;
	
	/**
	 * 权限列表
	 * @param page
	 * @param rows
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public String  list(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,HttpServletResponse response) throws Exception 
	{
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("start",pageBean.getStart());
		map.put("size",pageBean.getPageSize());
		List<Power> powerList=powerService.find(map);
		Long total=powerService.count(map);
		JSONObject result=new JSONObject();
		JSONArray jsonArray=JSONArray.fromObject(powerList);
		result.put("rows",jsonArray);
		result.put("total", total);
		ResponseUtil.write(response, result);
		return null;
	}

	/**
	 * 添加和修改权限类型
	 * @param power
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public String save(Power power,HttpServletResponse response) throws Exception
	{
		int resultTotal = 0;
		if(power.getId() == null)
		{
			resultTotal = powerService.add(power);
		}
		else
		{
			resultTotal = powerService.update(power);
		}
		JSONObject result = new JSONObject();
		if(resultTotal>0)
		{
			result.put("success", true);
		}
		else
		{
			result.put("success", false);
		}
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 删除权限
	 * @param ids
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public String delete(@RequestParam(value="ids")String ids,HttpServletResponse response) throws Exception
	{
		String strIds[] = ids.split(",");
		for(int i=0;i<strIds.length;i++)
		{
			powerService.delete(Integer.parseInt(strIds[i]));
		}
		JSONObject result = new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 获取当前所有权限类型
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/combList")
	public String  combList(HttpServletResponse response) throws Exception
	{
		JSONArray jsonArray=new JSONArray();
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("id","");
		jsonObject.put("name","请选择");
		jsonArray.add(jsonObject);
		List<Power> powerList=powerService.find(null);
		JSONArray rows=JSONArray.fromObject(powerList);
		jsonArray.addAll(rows);
		ResponseUtil.write(response, jsonArray);
		return null;
	}

}
