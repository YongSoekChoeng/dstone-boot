package net.dstone.common.tools.analyzer.svc.mtd.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.dstone.common.core.BaseObject;
import net.dstone.common.tools.analyzer.svc.mtd.Mtd;
import net.dstone.common.tools.analyzer.util.ParseUtil;
import net.dstone.common.tools.analyzer.vo.MtdVo;
import net.dstone.common.tools.analyzer.vo.TblVo;
import net.dstone.common.utils.FileUtil;

public class DefaultMtd extends BaseObject implements Mtd {

	@Override
	public List<MtdVo> getMtdVoList(String src) {
		List<MtdVo> mList = new ArrayList<MtdVo>();
		String fileExt = FileUtil.getFileExt(src);
		String fileConts = FileUtil.readFile(src);
		
		if("java".equals(fileExt)) {
			List<Map<String, String>> mapList = ParseUtil.getMtdListFromJava(fileConts);
			for(Map<String, String> item : mapList) {
				MtdVo mtdVo = new MtdVo();
				mtdVo.setMethodId(item.get("METHOD_ID"));
				mtdVo.setMethodName(item.get("METHOD_NAME"));
				mtdVo.setMethodBody(item.get("METHOD_BODY"));
			}
		}
		return mList;
	}

	@Override
	public String getUrl(String src) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MtdVo> getCallsMtdVoList(String src) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblVo> getCallsTblVo(String src) {
		// TODO Auto-generated method stub
		return null;
	}

}
