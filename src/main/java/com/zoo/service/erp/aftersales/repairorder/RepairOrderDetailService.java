package com.zoo.service.erp.aftersales.repairorder;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zoo.mapper.erp.aftersales.repairorder.RepairOrderDetailMapper;
import com.zoo.model.erp.aftersales.repairorder.RepairOrderDetail;
@Service
public class RepairOrderDetailService {

	@Autowired
	RepairOrderDetailMapper detailMapper;
	/**
	 * 添加数据
	 * @param detail
	 * @return
	 */
	public RepairOrderDetail addDetail(RepairOrderDetail detail) {
		
		detail.setId(UUID.randomUUID().toString());
		
		detailMapper.addDetail(detail);
		
		return detail;
		
	}
	
	/**
	 * 更新数据
	 * @param detail
	 * @return
	 */
	public int updateDetail(RepairOrderDetail detail) {
		
		return detailMapper.updateDetail(detail);
		
	}
	
	/**
	 * 批量删除数据
	 * @param ids
	 * @return
	 */
	public int deleteDetailById(String ids) {
		
		String[] split = ids.split(",");
		
		return detailMapper.deleteDetailById(split);
		
	}
	
	/**
	 * 更新维修数量
	 * @param id
	 * @param number
	 * @return
	 */
	public int updateNumberById(String id, BigDecimal number) {
		
		return detailMapper.updateNumberById(number, id);
		
	}
	
	/**
	 * 根据换货单id获取数据
	 * @param changeOrderId
	 * @return
	 */
	public List<RepairOrderDetail> getDetailByRepairOrderId(String repairOrderId) {
		
		List<RepairOrderDetail> list = detailMapper.getDetailByRepairOrderId(repairOrderId);
		return list;
		
	}
	
}
