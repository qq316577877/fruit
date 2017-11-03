package com.fruit.sys.admin.service.neworder;

import com.fruit.loan.biz.common.LoanInfoStatusEnum;
import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.newOrder.biz.common.ContainerEventEnum;
import com.fruit.newOrder.biz.common.ContainerStatusEnum;
import com.fruit.newOrder.biz.dto.ContainerInfoDTO;
import com.fruit.newOrder.biz.service.ContainerInfoService;
import com.fruit.sys.admin.model.ContainerProcessBean;
import com.fruit.sys.admin.queue.PushContainerProcessProxy;
import com.fruit.sys.admin.service.user.loanManagement.UserLoanManagementListService;
import com.fruit.sys.admin.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContainerNewScanReceive {

	@Autowired
	private ContainerInfoService containerInfoService;

	@Autowired
	private UserLoanManagementListService userLoanManagementListService;

	@Autowired
	private PushContainerProcessProxy pushContainerProcessProxy;

	private static final Logger logger = LoggerFactory.getLogger(ContainerNewScanReceive.class);

	@Transactional(rollbackFor = Exception.class)
	public void changeStatus() {
		// 先扫描本地货柜状态
		List<ContainerInfoDTO> containers = containerInfoService.loadListByStatus(ContainerStatusEnum.CLEARED.getStatus());
		if (containers == null || containers.size() == 0) {
			logger.info("the time {} has no containers to change status", new Date());
			return;
		}
		List<String> transactionNoList = new ArrayList<String>();
		for (ContainerInfoDTO con : containers) {
			transactionNoList.add(con.getContainerSerialNo());
		}
		// 扫描贷款信息，查询还款成功, 还款失败, 代扣成功, 代扣失败等四种
		List<LoanInfoDTO> loanInfos = userLoanManagementListService.loadGivenByTransNoList(transactionNoList);
		if (loanInfos == null || loanInfos.size() == 0) {
			logger.info("the time {} has no loan info to change containers' status", new Date());
			return;
		}
		for (LoanInfoDTO loan : loanInfos) {

			ContainerProcessBean containerProcessBean = new ContainerProcessBean();

			int status = loan.getStatus();
			if (status == LoanInfoStatusEnum.REPAYMENTS.getStatus()) {
				// 已还款
				containerProcessBean.setEventCode(ContainerEventEnum.SETTLEMENT.getCode());
			} else {
				// 其他状态变更为平台已处理
				containerProcessBean.setEventCode(ContainerEventEnum.REPAYFAILED.getCode());
			}

			containerProcessBean.setLoanInfoId(loan.getId());
			pushContainerProcessProxy.sendMsgCon(JsonUtil.toString(containerProcessBean));
		}

	}
}
