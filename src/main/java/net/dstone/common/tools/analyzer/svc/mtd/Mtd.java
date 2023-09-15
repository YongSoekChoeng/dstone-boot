package net.dstone.common.tools.analyzer.svc.mtd;

import java.util.List;

import net.dstone.common.tools.analyzer.vo.MtdVo;
import net.dstone.common.tools.analyzer.vo.TblVo;

public interface Mtd {
	public List<MtdVo> getMtdVoList(String src);
	public String getUrl(String src);
	public List<MtdVo> getCallsMtdVoList(String src);
	public List<TblVo> getCallsTblVo(String src);
}
