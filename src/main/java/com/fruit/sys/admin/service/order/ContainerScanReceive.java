package com.fruit.sys.admin.service.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fruit.loan.biz.common.LoanInfoStatusEnum;
import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.order.biz.common.ContainerStatusEnum;
import com.fruit.order.biz.dto.ContainerDTO;
import com.fruit.order.biz.service.ContainerService;
import com.fruit.sys.admin.service.user.loanManagement.UserLoanManagementListService;

@Service
public class ContainerScanReceive {

	@Autowired
	private ContainerService containerService;

	@Autowired
	private UserLoanManagementListService userLoanManagementListService;

	private static final Logger logger = LoggerFactory.getLogger(ContainerScanReceive.class);

	@Transactional(rollbackFor = Exception.class)
	public void changeStatus() {
		// 先扫描本地货柜状态
		List<ContainerDTO> containers = containerService.listByStatus(ContainerStatusEnum.REPAYMENT.getStatus());
		if (containers == null || containers.size() == 0) {
			logger.info("the time {} has no containers to change status", new Date());
			return;
		}
		List<String> transactionNoList = new ArrayList<String>();
		for (ContainerDTO con : containers) {
			transactionNoList.add(con.getTransactionNo());
		}
		// 扫描贷款信息，查询还款成功, 还款失败, 代扣成功, 代扣失败等四种
		List<LoanInfoDTO> loanInfos = userLoanManagementListService.loadGivenByTransNoList(transactionNoList);
		if (loanInfos == null || loanInfos.size() == 0) {
			logger.info("the time {} has no loan info to change containers' status", new Date());
			return;
		}
		for (LoanInfoDTO loan : loanInfos) {
			ContainerDTO cdto = containerService.loadByTransactionNo(loan.getTransactionNo());
			int status = loan.getStatus();
			if (status == LoanInfoStatusEnum.REPAYMENTS.getStatus()) {
				// 已还款
				cdto.setStatus(ContainerStatusEnum.SETTLEMENTED.getStatus());
			} else {
				// 其他状态变更为平台已处理
				cdto.setStatus(ContainerStatusEnum.PLATFORM_HANDLE.getStatus());
			}
			containerService.updateById(cdto);
		}

	}
}
