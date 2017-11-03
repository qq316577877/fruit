/*
 *
 * Copyright (c) 2017-2020 by wuhan HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service.user.loanManagement;


import com.fruit.base.biz.common.BaseRuntimeConfig;
import com.fruit.loan.biz.common.LoanInfoStatusEnum;
import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.loan.biz.dto.LoanUserApplyCreditDTO;
import com.fruit.loan.biz.request.sys.SysUserLoanManagementListRequest;
import com.fruit.loan.biz.service.sys.SysLoanInfoService;
import com.fruit.loan.biz.service.sys.SysLoanUserApplyCreditService;
import com.fruit.loan.biz.socket.util.DateStyle;
import com.fruit.loan.biz.socket.util.MathUtil;
import com.fruit.sys.admin.model.order.OrderInfoForLoanVo;
import com.fruit.sys.admin.model.request.UserLoanManagementQueryRequest;
import com.fruit.sys.admin.model.user.loanManagement.LoanCollectModel;
import com.fruit.sys.admin.model.user.loanManagement.LoanManagementModel;
import com.fruit.sys.admin.model.user.loanManagement.RepaymentCollectModel;
import com.fruit.sys.admin.service.BaseService;
import com.fruit.sys.admin.service.common.RuntimeConfigurationService;
import com.fruit.sys.admin.service.order.OrderForOtherService;
import com.fruit.sys.admin.utils.DateUtil;
import com.ovft.contract.api.bean.QueryEContractBean;
import com.ovft.contract.api.bean.ResponseVo;
import com.ovft.contract.api.service.EcontractService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

import static org.apache.commons.beanutils.BeanUtils.copyProperties;

/**
 * Description: 贷款管理、贷后管理
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-05-24
 * Project        : fruit
 * File Name      : UserLoanManagementListService.java
 */
@Service
public class UserLoanManagementListService extends BaseService
{
    private static final String TIME_NULL = "系统暂未录入";
    @Autowired
    private SysLoanInfoService sysLoanInfoService;

    @Autowired
    private SysLoanUserApplyCreditService sysLoanUserApplyCreditService;

    @Autowired
    private OrderForOtherService orderForOtherService;

    @Autowired
    private RuntimeConfigurationService runtimeConfigurationService;

    @Autowired
    private EcontractService econtractService;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    //申请总额--已放款之前的所有状态：待审核 待放款 审核不通过 放款-待确认
    private static final List<Integer> APPLY_AMOUNT_STATUS_LIST = new ArrayList<Integer>(4);
    static
    {
        APPLY_AMOUNT_STATUS_LIST.add(LoanInfoStatusEnum.PENDING_AUDIT.getStatus());
        APPLY_AMOUNT_STATUS_LIST.add(LoanInfoStatusEnum.LOAN_PENDING.getStatus());
        APPLY_AMOUNT_STATUS_LIST.add(LoanInfoStatusEnum.NOT_THROUGH.getStatus());
        APPLY_AMOUNT_STATUS_LIST.add(LoanInfoStatusEnum.TO_CONFIRMED.getStatus());
    }

    //待放款--包含 待放款、放款待确认
    private static final List<Integer> LOAD_PENDING_STATUS_LIST = new ArrayList<Integer>(2);
    static
    {
        LOAD_PENDING_STATUS_LIST.add(LoanInfoStatusEnum.LOAN_PENDING.getStatus());
        LOAD_PENDING_STATUS_LIST.add(LoanInfoStatusEnum.TO_CONFIRMED.getStatus());
    }

    //借据总额  有借据（放款之后状态）
    private static final List<Integer> OFFER_LOAN_AMOUNT_STATUS_LIST = new ArrayList<Integer>(7);
    static
    {
        OFFER_LOAN_AMOUNT_STATUS_LIST.add(LoanInfoStatusEnum.SECURED_LOAN.getStatus());
        OFFER_LOAN_AMOUNT_STATUS_LIST.add(LoanInfoStatusEnum.REPAYMENTS.getStatus());
        OFFER_LOAN_AMOUNT_STATUS_LIST.add(LoanInfoStatusEnum.REPAYMENT.getStatus());
        OFFER_LOAN_AMOUNT_STATUS_LIST.add(LoanInfoStatusEnum.REPAYMENT_FAILURE.getStatus());
        OFFER_LOAN_AMOUNT_STATUS_LIST.add(LoanInfoStatusEnum.MARGIN_REPAYMENT_SUCCESS.getStatus());
        OFFER_LOAN_AMOUNT_STATUS_LIST.add(LoanInfoStatusEnum.MARGIN_REPAYMENT_FAILURE.getStatus());
        OFFER_LOAN_AMOUNT_STATUS_LIST.add(LoanInfoStatusEnum.RECONCILIATION_CONFIRMED.getStatus());
    }


    //已放款--包含 已放款、已还款、待还款、还款失败、保证金还款成功、保证金还款失败、对账-待确认
    private static final List<Integer> LOAD_SECURED_STATUS_LIST = new ArrayList<Integer>(7);
    static
    {
        LOAD_SECURED_STATUS_LIST.add(LoanInfoStatusEnum.SECURED_LOAN.getStatus());
        LOAD_SECURED_STATUS_LIST.add(LoanInfoStatusEnum.REPAYMENTS.getStatus());
        LOAD_SECURED_STATUS_LIST.add(LoanInfoStatusEnum.REPAYMENT.getStatus());
        LOAD_SECURED_STATUS_LIST.add(LoanInfoStatusEnum.REPAYMENT_FAILURE.getStatus());
        LOAD_SECURED_STATUS_LIST.add(LoanInfoStatusEnum.MARGIN_REPAYMENT_SUCCESS.getStatus());
        LOAD_SECURED_STATUS_LIST.add(LoanInfoStatusEnum.MARGIN_REPAYMENT_FAILURE.getStatus());
        LOAD_SECURED_STATUS_LIST.add(LoanInfoStatusEnum.RECONCILIATION_CONFIRMED.getStatus());
    }


    //待还款--包含 已放款 待还款 保证金还款失败  还款失败
    private static final List<Integer> LOAD_UNPAY_STATUS_LIST = new ArrayList<Integer>(4);
    static
    {
        LOAD_UNPAY_STATUS_LIST.add(LoanInfoStatusEnum.SECURED_LOAN.getStatus());
        LOAD_UNPAY_STATUS_LIST.add(LoanInfoStatusEnum.REPAYMENT.getStatus());
        LOAD_UNPAY_STATUS_LIST.add(LoanInfoStatusEnum.REPAYMENT_FAILURE.getStatus());
        LOAD_UNPAY_STATUS_LIST.add(LoanInfoStatusEnum.MARGIN_REPAYMENT_FAILURE.getStatus());
    }

    //已还款--包含 已还款
    private static final List<Integer> LOAD_PAYED_STATUS_LIST = new ArrayList<Integer>(1);
    static
    {
        LOAD_PAYED_STATUS_LIST.add(LoanInfoStatusEnum.REPAYMENTS.getStatus());
    }

    //代偿还款--包含 代偿还款成功
    private static final List<Integer> LOAD_MARGIN_REPAYMENT_STATUS_LIST = new ArrayList<Integer>(1);
    static
    {
        LOAD_MARGIN_REPAYMENT_STATUS_LIST.add(LoanInfoStatusEnum.MARGIN_REPAYMENT_SUCCESS.getStatus());
    }




    public List<LoanManagementModel> loadUserLoanManagementList(UserLoanManagementQueryRequest queryRequest, int pageNo, int pageSize)
    {
        SysUserLoanManagementListRequest request = this.transfer(queryRequest);

        List<LoanManagementModel> result = Collections.emptyList();

        List<LoanInfoDTO> loanInfoDTOs = sysLoanInfoService.searchByKeyword(request, pageNo, pageSize);

        if (CollectionUtils.isNotEmpty(loanInfoDTOs))
        {
            List<LoanManagementModel> userLoanModelList = this.getLoanUserModelLists(loanInfoDTOs);
            result = userLoanModelList;
        }

        return result;
    }

    private List<LoanManagementModel> getLoanUserModelLists(List<LoanInfoDTO> loanInfoDTOs)
    {
        List<FutureTask<LoanManagementModel>> tasks = new ArrayList<FutureTask<LoanManagementModel>>(loanInfoDTOs.size());

        List<LoanManagementModel> userLoanList = new ArrayList<LoanManagementModel>(loanInfoDTOs.size());

        if (CollectionUtils.isNotEmpty(loanInfoDTOs))
        {
            for (LoanInfoDTO userLoan : loanInfoDTOs)
            {
                FutureTask<LoanManagementModel> futureTask = new FutureTask(new UserLoanModelCallable(userLoan));
                executorService.submit(futureTask);
                tasks.add(futureTask);
            }
            for (FutureTask<LoanManagementModel> task : tasks)
            {
                try
                {
                    LoanManagementModel userLoan = task.get();
                    userLoanList.add(userLoan);
                }
                catch (InterruptedException e)
                {
                    //
                }
                catch (ExecutionException e)
                {
                    //
                }
            }
        }
        return userLoanList;
    }


    class UserLoanModelCallable implements Callable<LoanManagementModel>
    {
        private LoanInfoDTO userLoan;

        public UserLoanModelCallable(LoanInfoDTO userLoan)
        {
            this.userLoan = userLoan;
        }

        @Override
        public LoanManagementModel call() throws Exception
        {
            try {
                return loadUserLoanManagementModel(userLoan, true);
            } catch (RuntimeException re) {
                re.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 根据id获取用户申请信息
     * @param userLoanId
     * @param loadOrderInfo
     * @return
     */
    public LoanManagementModel loadUserLoanManagementModel(int userLoanId, boolean loadOrderInfo)
    {
        LoanManagementModel loanManagementModel = null;
        if (userLoanId > 0)
        {
            LoanInfoDTO loanInfoDTO = sysLoanInfoService.loadById(userLoanId);

            if (null != loanInfoDTO)
            {
                return loadUserLoanManagementModel (loanInfoDTO , loadOrderInfo);
            }
        }
        return loanManagementModel;
    }

    /**
     * 将loanInfoDTO转换为loanManagementModel
     * @param loanInfoDTO
     * @param loadOrderInfo
     * @return
     */
    protected LoanManagementModel loadUserLoanManagementModel(LoanInfoDTO loanInfoDTO, boolean loadOrderInfo)
    {
        LoanManagementModel loanManagementModel = null;

        OrderInfoForLoanVo orderInfoForLoanVo =this.loadOrderinfo(loanInfoDTO.getTransactionNo());
        if (loanInfoDTO != null)
        {
            loanManagementModel = new LoanManagementModel();

            LoanUserApplyCreditDTO userApplyInfo = sysLoanUserApplyCreditService.loadByUserId(loanInfoDTO.getUserId());

            //将loanInfoDTO的属性copy到loanManagementModel
            BeanUtils.copyProperties(loanInfoDTO, loanManagementModel,new String[]{"username","identity","mobile"});

            loanManagementModel.setRepaymentInterest(loanInfoDTO.getRepaymentAmount().subtract(loanInfoDTO.getOfferLoan()));

            loanManagementModel.setUsername(userApplyInfo.getUsername());
            loanManagementModel.setIdentity(userApplyInfo.getIdentity());
            loanManagementModel.setMobile(userApplyInfo.getMobile());

            loanManagementModel.setDbtExpDtString(DateUtil.DateToString(loanInfoDTO.getDbtExpDt(), DateStyle.YYYY_MM_DD));
            loanManagementModel.setOfferTimeString(DateUtil.DateToString(loanInfoDTO.getOfferTime(), DateStyle.YYYY_MM_DD));
            loanManagementModel.setExpiresTimeString(DateUtil.DateToString(loanInfoDTO.getExpiresTime(), DateStyle.YYYY_MM_DD));
            loanManagementModel.setRepaymentTimeString(DateUtil.DateToString(loanInfoDTO.getRepaymentTime(), DateStyle.YYYY_MM_DD));
            loanManagementModel.setAddTimeString(DateUtil.DateToString(loanInfoDTO.getAddTime(), DateStyle.YYYY_MM_DD));
            loanManagementModel.setUpdateTimeString(DateUtil.DateToString(loanInfoDTO.getUpdateTime(), DateStyle.YYYY_MM_DD));

            if(orderInfoForLoanVo!=null){
                BeanUtils.copyProperties(orderInfoForLoanVo, loanManagementModel,new String[]{"transactionNo","deliveryTime","preReceiveTime"});

            }

            if(orderInfoForLoanVo!=null && orderInfoForLoanVo.getDeliveryTime()!=null){
                String deliveryTime = DateUtil.DateToString(orderInfoForLoanVo.getDeliveryTime(), DateStyle.YYYY_MM_DD_HH_MM_SS);
                loanManagementModel.setDeliveryTime(deliveryTime);
            } else {
                loanManagementModel.setDeliveryTime(TIME_NULL);
            }
            if(orderInfoForLoanVo!=null && orderInfoForLoanVo.getPreReceiveTime()!=null){
                String preReceiveTime = DateUtil.DateToString(orderInfoForLoanVo.getPreReceiveTime(), DateStyle.YYYY_MM_DD);
                loanManagementModel.setPreReceiveTime(preReceiveTime);
            } else {
                loanManagementModel.setPreReceiveTime(TIME_NULL);
            }

            // 获取合同ID 查询合同URL
            Long contractId =loanInfoDTO.getContractId();
            //dubbo接口查询合同URL地址
            String contractUrl = "";
            if(contractId > 0) {
                String source = envService.getConfig("contract.source");
                ResponseVo response = econtractService.queryContractUrlById(new QueryEContractBean(contractId,source));//
                if (response.isSuccess()) {
                    contractUrl = (String) response.getData();
                }
            }
            loanManagementModel.setContractUrl(contractUrl);

        }
        return loanManagementModel;
    }



    private SysUserLoanManagementListRequest transfer(UserLoanManagementQueryRequest queryRequest)
    {
        SysUserLoanManagementListRequest request = new SysUserLoanManagementListRequest();
        try
        {
            copyProperties(request, queryRequest);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return request;
    }

    public RepaymentCollectModel loadRepaymentCollectModel()
    {
        BigDecimal offerLoanAmount = new BigDecimal("0");    //借据总额
        int offerLoanCount = 0;    //借据总笔数
        BigDecimal unpayAmount = new BigDecimal("0");    //待还款总额
        int unpayCount = 0;    //待还款笔数
        BigDecimal repaymentAmount = new BigDecimal("0");    //已还款总额
        int repaymentCount = 0;    //已还款笔数
        BigDecimal depositAmount = new BigDecimal("0");    //保证金代还款总额
        int depositCount = 0;    //保证金代还款笔数

        RepaymentCollectModel repaymentCollectModel = new RepaymentCollectModel();

        //借据总额--包含放款后所有状态
        Map<String,String> columns = new ConcurrentHashMap<String,String>();

        //申请额度
        columns.clear();
        columns.put("offerLoan","sum");

        LoanInfoDTO offerDTO = sysLoanInfoService.searchSumAmount(OFFER_LOAN_AMOUNT_STATUS_LIST,columns,true);

        repaymentCollectModel.setOfferLoanAmount(offerDTO.getOfferLoan()!=null?offerDTO.getOfferLoan():offerLoanAmount);
        repaymentCollectModel.setOfferLoanCount(offerDTO.getCount());

//        List<LoanInfoDTO> offerLoanDTOs = sysLoanInfoService.searchAmount(OFFER_LOAN_AMOUNT_STATUS_LIST);
//        if (offerLoanDTOs != null)      //借据总额
//        {
//            for (LoanInfoDTO loanInfo:offerLoanDTOs) {
//                offerLoanAmount = offerLoanAmount.add(loanInfo.getOfferLoan());
//                offerLoanCount ++;
//        }
//            repaymentCollectModel.setOfferLoanAmount(offerLoanAmount);
//            repaymentCollectModel.setOfferLoanCount(offerLoanCount);
//        }

        //待还款list
        columns.clear();
        columns.put("offerLoan","sum");

        LoanInfoDTO unpayDTOs = sysLoanInfoService.searchSumAmount(LOAD_UNPAY_STATUS_LIST,columns,true);

        repaymentCollectModel.setUnpayAmount(unpayDTOs.getOfferLoan()!=null?unpayDTOs.getOfferLoan():unpayAmount);
        repaymentCollectModel.setUnpayCount(unpayDTOs.getCount());

//        List<LoanInfoDTO> unpayDTOs = sysLoanInfoService.searchAmount(LOAD_UNPAY_STATUS_LIST);
//        if (unpayDTOs != null)
//        {
//            for (LoanInfoDTO loanInfo:unpayDTOs) {
//                unpayAmount = unpayAmount.add(loanInfo.getOfferLoan());
//                unpayCount ++;
//            }
//            repaymentCollectModel.setUnpayAmount(unpayAmount);
//            repaymentCollectModel.setUnpayCount(unpayCount);
//        } else {
//            repaymentCollectModel.setUnpayAmount(unpayAmount);
//            repaymentCollectModel.setUnpayCount(unpayCount);
//        }

        //已还款list
        columns.clear();
        columns.put("repaymentAmount","sum");

        LoanInfoDTO repaymentDTOs = sysLoanInfoService.searchSumAmount(LOAD_PAYED_STATUS_LIST,columns,true);

        repaymentCollectModel.setRepaymentAmount(repaymentDTOs.getRepaymentAmount()!=null?repaymentDTOs.getRepaymentAmount():repaymentAmount);
        repaymentCollectModel.setRepaymentCount(repaymentDTOs.getCount());

//        List<LoanInfoDTO> repaymentDTOs = sysLoanInfoService.searchAmount(LOAD_PAYED_STATUS_LIST);
//        if (repaymentDTOs != null)
//        {
//            for (LoanInfoDTO loanInfo:repaymentDTOs) {
//                repaymentAmount = repaymentAmount.add(loanInfo.getRepaymentAmount());
//                repaymentCount ++;
//            }
//            repaymentCollectModel.setRepaymentAmount(repaymentAmount);
//            repaymentCollectModel.setRepaymentCount(repaymentCount);
//        }else {
//            repaymentCollectModel.setRepaymentAmount(repaymentAmount);
//            repaymentCollectModel.setRepaymentCount(repaymentCount);
//        }

        //代还款状态list
        columns.clear();
        columns.put("repaymentAmount","sum");

        LoanInfoDTO depositDTOs = sysLoanInfoService.searchSumAmount(LOAD_MARGIN_REPAYMENT_STATUS_LIST,columns,true);

        repaymentCollectModel.setDepositAmount(depositDTOs.getRepaymentAmount()!=null?depositDTOs.getRepaymentAmount():depositAmount);
        repaymentCollectModel.setDepositCount(depositDTOs.getCount());

//        List<LoanInfoDTO> depositDTOs = sysLoanInfoService.searchAmount(LOAD_MARGIN_REPAYMENT_STATUS_LIST);
//        if (depositDTOs != null)
//        {
//            for (LoanInfoDTO loanInfo:depositDTOs) {
//                depositAmount = depositAmount.add(loanInfo.getRepaymentAmount());
//                depositCount ++;
//            }
//            repaymentCollectModel.setDepositAmount(depositAmount);
//            repaymentCollectModel.setDepositCount(depositCount);
//        } else {
//            repaymentCollectModel.setDepositAmount(depositAmount);
//            repaymentCollectModel.setDepositCount(depositCount);
//        }

        return repaymentCollectModel;
    }


    public LoanCollectModel loadLoanCollectModel()
    {
        BigDecimal applyAmount = new BigDecimal("0") ;    //申请总额
        BigDecimal applyFee = new BigDecimal("0");    //申请总服务费
//        int applyCount = 0;    //申请总笔数
        BigDecimal pendingAmount = new BigDecimal("0") ;    //待放款总额
        BigDecimal pendingFee = new BigDecimal("0") ;    //待放款服务费
//        int pendingCount = 0;    //待放款笔数
//        BigDecimal repaymentAmount = new BigDecimal("0") ;    //已放款总额
//        BigDecimal repaymentFee = new BigDecimal("0") ;    //已放款服务费
//        int repaymentCount = 0;    //已放款笔数

        LoanCollectModel loanCollectModel = new LoanCollectModel();

        Map<String,String> columns = new ConcurrentHashMap<String,String>();

        //申请额度
        columns.clear();
        columns.put("appliyLoan","sum");
        columns.put("serviceFee","sum");


        LoanInfoDTO applyDTO = sysLoanInfoService.searchSumAmount(APPLY_AMOUNT_STATUS_LIST,columns,true);



        loanCollectModel.setApplyAmount(applyDTO.getAppliyLoan()!=null?applyDTO.getAppliyLoan():applyAmount);
        loanCollectModel.setApplyFee(applyDTO.getServiceFee()!=null?applyDTO.getServiceFee():applyFee);
        loanCollectModel.setApplyCount(applyDTO.getCount());

//        List<LoanInfoDTO> applyDTOs = sysLoanInfoService.searchAmount(APPLY_AMOUNT_STATUS_LIST);
//        if (applyDTOs != null)
//        {
//            for (LoanInfoDTO loanInfo:applyDTOs) {
//                applyAmount = applyAmount.add(loanInfo.getAppliyLoan());
//                applyFee = applyFee.add(loanInfo.getServiceFee());
//                applyCount ++;
//            }
//            loanCollectModel.setApplyAmount(applyAmount);
//            loanCollectModel.setApplyFee(applyFee);
//            loanCollectModel.setApplyCount(applyCount);
//        } else {
//            loanCollectModel.setApplyAmount(applyAmount);
//            loanCollectModel.setApplyFee(applyFee);
//            loanCollectModel.setApplyCount(applyCount);
//        }

        //待放款的状态list
        columns.clear();
        columns.put("appliyLoan","sum");
        columns.put("serviceFee","sum");

        applyDTO = sysLoanInfoService.searchSumAmount(LOAD_PENDING_STATUS_LIST,columns,true);


        loanCollectModel.setPendingAmount(applyDTO.getAppliyLoan()!=null?applyDTO.getAppliyLoan():pendingAmount);
        loanCollectModel.setPendingFee(applyDTO.getServiceFee()!=null?applyDTO.getServiceFee():pendingFee);
        loanCollectModel.setPendingCount(applyDTO.getCount());
//        List<LoanInfoDTO> pendingDTOs = sysLoanInfoService.searchAmount(LOAD_PENDING_STATUS_LIST);
//        if (pendingDTOs != null)
//        {
//            for (LoanInfoDTO loanInfo:pendingDTOs) {
//                pendingAmount = pendingAmount.add(loanInfo.getAppliyLoan());
//                pendingFee = pendingFee.add(loanInfo.getServiceFee());
//                pendingCount ++;
//            }
//            loanCollectModel.setPendingAmount(pendingAmount);
//            loanCollectModel.setPendingFee(pendingFee);
//            loanCollectModel.setPendingCount(pendingCount);
//        } else {
//            loanCollectModel.setPendingAmount(pendingAmount);
//            loanCollectModel.setPendingFee(pendingFee);
//            loanCollectModel.setPendingCount(pendingCount);
//        }

        //已放款状态list
//        columns.clear();
//        columns.put("repaymentAmount","sum");
//        columns.put("serviceFee","sum");
//
//        applyDTO = sysLoanInfoService.searchSumAmount(LOAD_SECURED_STATUS_LIST,columns,true);
//
//
//        loanCollectModel.setRepaymentAmount(applyDTO.getRepaymentAmount()!=null?applyDTO.getRepaymentAmount():repaymentAmount);
//        loanCollectModel.setRepaymentFee(applyDTO.getServiceFee()!=null?applyDTO.getServiceFee():repaymentFee);
//        loanCollectModel.setRepaymentCount(applyDTO.getCount());

//        List<LoanInfoDTO> repaymentDTOs = sysLoanInfoService.searchAmount(LOAD_SECURED_STATUS_LIST);
//        if (repaymentDTOs != null)
//        {
//            for (LoanInfoDTO loanInfo:repaymentDTOs) {
//                repaymentAmount = repaymentAmount.add(loanInfo.getRepaymentAmount());
//                repaymentFee = repaymentFee.add(loanInfo.getServiceFee());
//                repaymentCount ++;
//            }
//            loanCollectModel.setRepaymentAmount(repaymentAmount);
//            loanCollectModel.setRepaymentFee(repaymentFee);
//            loanCollectModel.setRepaymentCount(repaymentCount);
//        } else {
//            loanCollectModel.setRepaymentAmount(repaymentAmount);
//            loanCollectModel.setRepaymentFee(repaymentFee);
//            loanCollectModel.setRepaymentCount(repaymentCount);
//        }

        return loanCollectModel;
    }

    /**
     * 获取订单号
     * @return
     */
    public OrderInfoForLoanVo loadOrderinfo(String getTransactionNo){
        OrderInfoForLoanVo orderInfoForLoanVo = this.orderForOtherService.queryOrderInfoForLoan(getTransactionNo);
        return orderInfoForLoanVo;
    }


    /**
     * 更改货柜状态，从待审核状态更改为待放款状态
     *
     * @param transactionNo  唯一流水号（如水果订单模块为货柜唯一code）
     */
    public int updateFromAuditToLoan(String transactionNo){
        return sysLoanInfoService.updateFromAuditToLoan(transactionNo);
    }

    /**
     * 更改货柜状态，从已放款到待还款，同时更新强制还款时间
     *
     * @param transactionNo  唯一流水号（如水果订单模块为货柜唯一code）
     * @param extArrivalTime  预计到货时间
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateFromLoanToPrepay(String transactionNo,Date extArrivalTime){
        return sysLoanInfoService.updateFromLoanToPrepay(transactionNo,extArrivalTime);
    }

    /**
     * 通过唯一流水号List加载资金服务，只加载已还款的、还款失败、代扣还款失败的
     *
     * @param ransactionNoList
     * @return
     */
    public List<LoanInfoDTO> loadGivenByTransNoList(List<String> ransactionNoList) {
        return sysLoanInfoService.loadGivenByTransNoList(ransactionNoList);
    }


    /**
     * 创建资金服务---后台客户操作所需
     * @param loanInfoDTOs
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createLoanInfos(List<LoanInfoDTO> loanInfoDTOs,int userId)
    {
        if (CollectionUtils.isNotEmpty(loanInfoDTOs))
        {
            //1.先将该订单的所有货柜状态改为已删除状态
            String orderNo = loanInfoDTOs.get(0).getOrderNo();
            sysLoanInfoService.deleteByOrderNo(userId,orderNo);

            //2.集体操作内容
            //最低贷款金额
            String value = runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, BaseRuntimeConfig.MINIMUM_LOAN_AMOUNT);

            BigDecimal minimumLoanAmount = MathUtil.stringToBigDecima(value);
            sysLoanInfoService.updateLoanInfos(loanInfoDTOs,userId,minimumLoanAmount);
        }
    }


}
