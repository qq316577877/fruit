package com.fruit.sys.admin.service.order;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fruit.base.biz.common.LoanSmsBizTypeEnum;
import com.fruit.base.biz.dto.LoanSmsContactsConfigDTO;
import com.fruit.base.biz.service.*;
import com.fruit.newOrder.biz.dto.ContainerInfoDTO;
import com.fruit.sys.admin.model.wechat.TemplateParamVO;
import com.fruit.sys.admin.model.wechat.TemplateVO;
import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.service.neworder.ContainerNewService;
import com.fruit.sys.admin.service.wechat.WeChatBaseService;
import com.fruit.sys.admin.utils.WechatConstants;
import org.apache.commons.lang.StringUtils;
import com.fruit.account.biz.common.UserTypeEnum;
import com.fruit.base.biz.common.EnterpriseTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.dto.UserDeliveryAddressDTO;
import com.fruit.account.biz.dto.UserEnterpriseDTO;
import com.fruit.account.biz.dto.UserProductLoanInfoDTO;
import com.fruit.account.biz.dto.UserSupplierDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.account.biz.service.UserDeliveryAddressService;
import com.fruit.account.biz.service.UserEnterpriseService;
import com.fruit.account.biz.service.sys.SysUserProductLoanService;
import com.fruit.base.biz.common.BaseRuntimeConfig;
import com.fruit.base.biz.common.BizFileEnum;
import com.fruit.base.biz.dto.BizFileDTO;
import com.fruit.base.biz.dto.EnterpriseInfoDTO;
import com.fruit.loan.biz.common.LoanInfoStatusEnum;
import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.loan.biz.dto.LoanUserAuthInfoDTO;
import com.fruit.loan.biz.service.LoanInfoService;
import com.fruit.loan.biz.service.LoanMessageService;
import com.fruit.loan.biz.service.LoanUserAuthInfoService;
import com.fruit.order.biz.common.ContainerStatusEnum;
import com.fruit.order.biz.common.OrderStatusEnum;
import com.fruit.order.biz.common.OrderUpdateTypeEnum;
import com.fruit.order.biz.dto.ContainerDTO;
import com.fruit.order.biz.dto.ContainerDetailDTO;
import com.fruit.order.biz.dto.ContainerDetailTmpDTO;
import com.fruit.order.biz.dto.ContainerInsuranceDTO;
import com.fruit.order.biz.dto.ContainerTmpDTO;
import com.fruit.order.biz.dto.DeliveryInfoDTO;
import com.fruit.order.biz.dto.LogisticsDTO;
import com.fruit.order.biz.dto.LogisticsDetailDTO;
import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.order.biz.dto.ProductDTO;
import com.fruit.order.biz.dto.ProductPropertyDTO;
import com.fruit.order.biz.dto.ProductPropertyValueDTO;
import com.fruit.order.biz.request.OrderSearchRequest;
import com.fruit.order.biz.service.ContainerDetailService;
import com.fruit.order.biz.service.ContainerDetailTmpService;
import com.fruit.order.biz.service.ContainerInsuranceService;
import com.fruit.order.biz.service.ContainerService;
import com.fruit.order.biz.service.ContainerTmpService;
import com.fruit.order.biz.service.DeliveryInfoService;
import com.fruit.order.biz.service.LogisticsDetailService;
import com.fruit.order.biz.service.LogisticsService;
import com.fruit.order.biz.service.OrderService;
import com.fruit.order.biz.service.ProductPropertyService;
import com.fruit.order.biz.service.ProductPropertyValueService;
import com.fruit.order.biz.service.ProductService;
import com.fruit.sys.admin.model.PageResult;
import com.fruit.sys.admin.model.order.AddLogisticsInfoBean;
import com.fruit.sys.admin.model.order.ApplyLoanInfoVo;
import com.fruit.sys.admin.model.order.BizFileVo;
import com.fruit.sys.admin.model.order.ContainerDetailVo;
import com.fruit.sys.admin.model.order.ContainerLoanVo;
import com.fruit.sys.admin.model.order.ContainerSimpleInfoVo;
import com.fruit.sys.admin.model.order.DeliveryInfoVo;
import com.fruit.sys.admin.model.order.EnterpriseInfoVo;
import com.fruit.sys.admin.model.order.LastOrderVo;
import com.fruit.sys.admin.model.order.LogisticsDetailBean;
import com.fruit.sys.admin.model.order.LogisticsDetailShowVo;
import com.fruit.sys.admin.model.order.LogisticsDetailVo;
import com.fruit.sys.admin.model.order.OrderContainer;
import com.fruit.sys.admin.model.order.OrderContainerDetail;
import com.fruit.sys.admin.model.order.OrderContainerFee;
import com.fruit.sys.admin.model.order.OrderDetailShowBean;
import com.fruit.sys.admin.model.order.OrderDetailShowVo;
import com.fruit.sys.admin.model.order.OrderDetailVo;
import com.fruit.sys.admin.model.order.OrderForLogisticsVo;
import com.fruit.sys.admin.model.order.OrderLogistics;
import com.fruit.sys.admin.model.order.OrderVo;
import com.fruit.sys.admin.model.order.ProductInfoVo;
import com.fruit.sys.admin.model.order.ProductPropertyVo;
import com.fruit.sys.admin.model.order.SupplierVo;
import com.fruit.sys.admin.model.order.UserSupplierVo;
import com.fruit.sys.admin.model.portal.AjaxResult;
import com.fruit.sys.admin.model.portal.AjaxResultCode;
import com.fruit.sys.admin.model.user.Loan.LoanInfoModel;
import com.fruit.sys.admin.model.user.account.AddressVo;
import com.fruit.sys.admin.queue.PushDebtContractProxy;
import com.fruit.sys.admin.service.common.FileUploadService;
import com.fruit.sys.admin.service.common.MessageService;
import com.fruit.sys.admin.service.common.RuntimeConfigurationService;
import com.fruit.sys.admin.service.common.UrlConfigService;
import com.fruit.sys.admin.service.drivers.DriversMemberService;
import com.fruit.sys.admin.service.trade.OrderTaskHelper;
import com.fruit.sys.admin.service.user.account.DeliveryAddressService;
import com.fruit.sys.admin.service.user.loan.UserLoanListService;
import com.fruit.sys.admin.service.user.loanManagement.UserLoanManagementListService;
import com.fruit.sys.admin.service.user.supplier.SupplierService;
import com.fruit.sys.admin.utils.BizUtils;
import com.fruit.sys.admin.utils.DateUtil;
import com.fruit.sys.admin.utils.MathUtil;
import com.ovfintech.arch.common.event.EventHelper;
import com.ovfintech.arch.common.event.EventLevel;
import com.ovfintech.arch.common.event.EventPublisher;
import com.ovfintech.arch.utils.ServerIpUtils;
import com.ovfintech.cache.client.CacheClient;

@Service
public class OrderGeneralService {

	@Autowired
	private OrderService orderService;

	@Autowired
	private SupplierService supplierService;

	@Autowired
	private ContainerService containerService;

	@Autowired
	private ContainerDetailService containerDetailService;

	@Resource
	private CacheClient cacheClient;

	@Autowired
	private UserDeliveryAddressService userDeliveryAddressService;

	@Autowired
	private DeliveryInfoService deliveryInfoService;

	@Autowired
	private EnterpriseInfoService enterpriseInfoService;

	@Autowired
	private CountryService countryService;

	@Autowired
	private ProvinceService provinceService;

	@Autowired
	private CityService cityService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private LogisticsService logisticsService;

	@Autowired
	private LogisticsDetailService logisticsDetailService;

	@Autowired
	private BizFileService bizFileService;

	@Autowired
	private OrderTaskHelper orderTaskHelper;

	@Autowired
	private OrderLogService orderLogService;

	@Autowired(required = false)
	protected List<EventPublisher> eventPublishers;

	@Autowired
	private ContainerTmpService containerTmpService;

	@Autowired
	private ContainerDetailTmpService containerDetailTmpService;

	@Autowired
	private UrlConfigService urlConfigService;

	@Autowired
	private UserLoanManagementListService userLoanManagementListService;

	@Autowired
	private UserEnterpriseService userEnterpriseService;

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductPropertyService productPropertyService;

	@Autowired
	private ProductPropertyValueService productPropertyValueService;

	@Autowired
	private DeliveryAddressService delieryAddressService;

	@Autowired
	private ContainerInsuranceService containerInsuranceService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserLoanManagementListService loanInformationService;

	@Autowired
	private UserLoanListService userLoanListService;

	@Autowired
	private SysUserProductLoanService sysUserProductLoanService;

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private PushDebtContractProxy pushDebtContractProxy;

	@Autowired
	private LoanUserAuthInfoService loanUserAuthInfoService;

	@Autowired
	private DriversMemberService driversMemberService;

	@Autowired
	private RuntimeConfigurationService runtimeConfigurationService;

	@Autowired
	private LoanMessageService loanMessageService;

	@Autowired
	private LoanInfoService loanInfoService;

	@Autowired
	private ContainerNewService containerNewService;


	private static final Logger logger = LoggerFactory.getLogger(OrderGeneralService.class);

	/**
	 * 查询所有的商品信息
	 * 
	 * @return
	 */
	public Map<String, ProductInfoVo> findAllGoods() {
		// 先去查缓存，为空再去load DB
		Map<String, String> goodsMap = cacheClient.hgetAll("goods");
		Map<String, ProductInfoVo> products = new HashMap<String, ProductInfoVo>();
		if (goodsMap == null || goodsMap.isEmpty()) {
			// 查询商品基本信息
			List<ProductDTO> dtos = productService.listAll();
			if (dtos != null && dtos.size() > 0) {
				for (ProductDTO dto : dtos) {
					ProductInfoVo p = new ProductInfoVo();
					BeanUtils.copyProperties(dto, p);
					// 同时加载商品属性信息
					List<ProductPropertyVo> productDetails = new ArrayList<ProductPropertyVo>();
					Integer productId = p.getId();
					List<ProductPropertyDTO> productPropertyDtos = productPropertyService.listByProductId(productId);
					for (ProductPropertyDTO productPropertyDto : productPropertyDtos) {
						List<ProductPropertyValueDTO> productPropertyValueDtos = productPropertyValueService
								.listByProductPropertyId(productPropertyDto.getId());
						ProductPropertyVo vo = new ProductPropertyVo();
						vo.setName(productPropertyDto.getName());
						vo.setEngName(productPropertyDto.getEngName());
						vo.setValues(productPropertyValueDtos);
						productDetails.add(vo);
					}
					p.setProductDetails(productDetails);
					products.put(p.getId().toString(), p);
					cacheClient.hset("goods", p.getId().toString(), JSON.toJSONString(p));
					cacheClient.expire("goods", 1800);
				}
			}
			return products;
		}
		for (Map.Entry<String, String> entry : goodsMap.entrySet()) {
			ProductInfoVo vo = JSON.parseObject(entry.getValue(), ProductInfoVo.class);
			products.put(entry.getKey(), vo);
		}
		return products;
	}

	/**
	 * 根据商品ID查询商品信息
	 * 
	 * @param productId
	 * @return
	 */
	public ProductInfoVo getGoodsInfoById(String productId) {
		// 先去查缓存，为空再去load DB
		String result = cacheClient.hget("goods", productId);
		ProductInfoVo p = new ProductInfoVo();
		if (result == null || "".equals(result)) {
			ProductDTO dto = productService.loadById(Integer.parseInt(productId));
			BeanUtils.copyProperties(dto, p);
			List<ProductPropertyVo> productDetails = new ArrayList<ProductPropertyVo>();
			List<ProductPropertyDTO> productPropertyDtos = productPropertyService.listByProductId(Integer.parseInt(productId));
			for (ProductPropertyDTO productPropertyDto : productPropertyDtos) {
				List<ProductPropertyValueDTO> productPropertyValueDtos = productPropertyValueService
						.listByProductPropertyId(productPropertyDto.getId());
				ProductPropertyVo vo = new ProductPropertyVo();
				vo.setName(productPropertyDto.getName());
				vo.setEngName(productPropertyDto.getEngName());
				vo.setValues(productPropertyValueDtos);
				productDetails.add(vo);
			}
			p.setProductDetails(productDetails);
			cacheClient.hset("goods", p.getId().toString(), JSON.toJSONString(p));
			cacheClient.expire("goods", 1800);
			return p;
		}
		return JSON.parseObject(result, ProductInfoVo.class);
	}

	/**
	 * 分页查询订单
	 * 
	 * @param request
	 * @return
	 */
	public PageResult<OrderDetailVo> searchOrderByPage(OrderSearchRequest request) {
		PageResult<OrderDetailVo> result = new PageResult<OrderDetailVo>();
		int limit = request.getPageSize();
		if (limit == 0) {
			limit = 10;
		}
		int pageNo = request.getPageNo();
		int count = orderService.selectCountByExample(request);
		result.setTotalCount(count);
		List<OrderDTO> orderDtos = orderService.paginateExpiredOrdersByPage(request);
		List<OrderDetailVo> list = new ArrayList<OrderDetailVo>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (orderDtos != null && orderDtos.size() != 0) {
			for (OrderDTO order : orderDtos) {
				OrderDetailVo vo = new OrderDetailVo();
				vo.setDate(sdf.format(order.getAddTime()));
				vo.setOrderNo(order.getNo());
				vo.setOrderStatus(order.getStatus());
				vo.setOrderStatusDesc(OrderStatusEnum.get(order.getStatus()).getUserDesc());
				vo.setOrderDetailUrl(urlConfigService.getOrderDetailUrl(order.getNo()));

				//获取采购商
				int userid = order.getUserId();
				String purchaseName = "";
				UserEnterpriseDTO dto = userEnterpriseService.loadByUserId(userid);
				if(dto != null) {
					purchaseName = dto.getType() == UserTypeEnum.PERSONAL.getType() ? dto.getName() : dto.getEnterpriseName();
				}
				vo.setPurchaseName(purchaseName);

				UserSupplierDTO userSupplier = supplierService.loadSupplierById(order.getSupplierId());
				if (userSupplier == null) {
					vo.setSupplierName("");
				} else {
					vo.setSupplierName(userSupplier.getSupplierName());
				}
				List<ContainerDetailVo> containerDetails = new ArrayList<ContainerDetailVo>();
				if (isQueryTmp(order.getStatus())) {
					// 查询临时表
					List<ContainerTmpDTO> containers = containerTmpService.listByOrderNo(order.getNo());
					if (containers != null && containers.size() != 0) {
						for (ContainerTmpDTO cdto : containers) {
							ContainerDetailVo cvo = new ContainerDetailVo();
							cvo.setContainerId(cdto.getNo());
							cvo.setContainerStatus(cdto.getStatus());
							cvo.setContainerStatusDesc(ContainerStatusEnum.getStatusDesc(cdto.getStatus()));
							cvo.setProductName(cdto.getProductName());
							// 稍后补上贷款信息
							LoanInfoModel loanInfoModel = userLoanListService.loadLoanInfosByTransactionNo(cdto
									.getTransactionNo());
							if (loanInfoModel != null) {
								cvo.setAppliyLoan(loanInfoModel.getAppliyLoan());
								cvo.setLoanStatus(loanInfoModel.getStatus());
								cvo.setLoanStatusDesc(loanInfoModel.getStatusDesc());
							}
							containerDetails.add(cvo);
						}
					}
					vo.setContainerDetails(containerDetails);
				} else {
					List<ContainerDTO> containers = containerService.listByOrderNo(order.getNo());
					if (containers != null && containers.size() != 0) {
						for (ContainerDTO cdto : containers) {
							ContainerDetailVo cvo = new ContainerDetailVo();
							cvo.setContainerId(cdto.getNo());
							cvo.setContainerStatus(cdto.getStatus());
							cvo.setContainerStatusDesc(ContainerStatusEnum.getStatusDesc(cdto.getStatus()));
							cvo.setProductName(cdto.getProductName());
							// 稍后补上贷款信息
							LoanInfoModel loanInfoModel = userLoanListService.loadLoanInfosByTransactionNo(cdto
									.getTransactionNo());
							if (loanInfoModel != null) {
								cvo.setAppliyLoan(loanInfoModel.getAppliyLoan());
								cvo.setLoanStatus(loanInfoModel.getStatus());
								cvo.setLoanStatusDesc(loanInfoModel.getStatusDesc());
							}
							containerDetails.add(cvo);
						}
					}
					vo.setContainerDetails(containerDetails);
				}
				list.add(vo);
			}
			result.setRecords(list);
		}
		result.setPageNo(pageNo == 0 ? 1 : pageNo);
		result.setPageSize(limit);
		result.setTotalPage((count + limit - 1) / limit);
		return result;
	}

	/**
	 * 查询订单详情
	 * 
	 * @param orderId
	 * @return
	 */
	public OrderDetailShowBean queryOrderDetail(String orderId) {
		OrderDetailShowBean orderDetail = new OrderDetailShowBean();
		OrderDTO order = orderService.loadByNo(orderId);
		BeanUtils.copyProperties(order, orderDetail);
		orderDetail.setOrderNo(orderId);
		orderDetail.setOrderStatusDesc(OrderStatusEnum.get(order.getStatus()).getUserDesc());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		orderDetail.setPlaceOrderTime(sdf.format(order.getAddTime()));
		// 加载收货地址信息
		DeliveryInfoDTO deliveryInfoDto = deliveryInfoService.loadByOrderNo(orderId);
		if (deliveryInfoDto != null) {
			DeliveryInfoVo deliveryInfo = new DeliveryInfoVo();
			BeanUtils.copyProperties(deliveryInfoDto, deliveryInfo);
			deliveryInfo.setCountryName(countryService.loadById(deliveryInfoDto.getCountryId()).getName());
			deliveryInfo.setProvinceName(provinceService.loadById(deliveryInfoDto.getProvinceId()).getName());
			deliveryInfo.setCityName(cityService.loadById(deliveryInfoDto.getCityId()).getName());
			deliveryInfo.setDistrictName(areaService.loadById(deliveryInfoDto.getDistrictId()).getName());
			orderDetail.setDeliveryAddress(deliveryInfo);
		}
		// 加载供应商信息
		UserSupplierDTO userSupplier = supplierService.loadSupplierById(order.getSupplierId());
		UserSupplierVo supplier = new UserSupplierVo();
		if (userSupplier != null) {
			BeanUtils.copyProperties(userSupplier, supplier);
			supplier.setCountryName(countryService.loadById(userSupplier.getCountryId()).getName());
			supplier.setProvinceName(provinceService.loadById(userSupplier.getProvinceId()).getName());
			supplier.setCityName(cityService.loadById(userSupplier.getCityId()).getName());
			supplier.setDistrictName(areaService.loadById(userSupplier.getDistrictId()).getName());
		} else {
			supplier.setCountryName("");
			supplier.setProvinceName("");
			supplier.setCityName("");
			supplier.setDistrictName("");
		}
		orderDetail.setSupplier(supplier);
		List<OrderContainer> orderContainers = new ArrayList<OrderContainer>();

		if (isQueryTmp(order.getStatus())) {
			List<ContainerTmpDTO> containers = containerTmpService.listByOrderNo(order.getNo());
			if (containers != null && containers.size() != 0) {
				for (ContainerTmpDTO dto : containers) {
					OrderContainer oc = new OrderContainer();
					BeanUtils.copyProperties(dto, oc);
					oc.setBatchNumber(dto.getNo());

					String result = cacheClient.hget("goods", String.valueOf(dto.getProductId()));
					ProductInfoVo p = new ProductInfoVo();
					if (result == null || "".equals(result)) {
						ProductDTO pdto = productService.loadById(dto.getProductId());
						BeanUtils.copyProperties(pdto, p);
					} else {
						p = JSON.parseObject(result, ProductInfoVo.class);
					}
					oc.setMaxQuantity(p.getCapacitySize());

					List<OrderContainerDetail> orderContainerDetails = new ArrayList<OrderContainerDetail>();
					List<ContainerDetailTmpDTO> containerDetailDtos = containerDetailTmpService.listByContainerNo(dto.getNo());
					if (containerDetailDtos != null && containerDetailDtos.size() != 0) {
						for (ContainerDetailTmpDTO cdto : containerDetailDtos) {
							OrderContainerDetail ocd = new OrderContainerDetail();
							BeanUtils.copyProperties(cdto, ocd, new String[] { "productDetail" });
							@SuppressWarnings("unchecked")
							Map<String, String> productDetail = JSON.parseObject(cdto.getProductDetail(), HashMap.class);
							ocd.setProductDetail(productDetail);
							orderContainerDetails.add(ocd);
						}
					}
					oc.setOrderContainerDetails(orderContainerDetails);
					orderContainers.add(oc);
				}
			}
		} else {
			List<ContainerDTO> containers = containerService.listByOrderNo(order.getNo());
			if (containers != null && containers.size() != 0) {
				for (ContainerDTO dto : containers) {
					OrderContainer oc = new OrderContainer();
					BeanUtils.copyProperties(dto, oc);
					oc.setBatchNumber(dto.getNo());

					String result = cacheClient.hget("goods", String.valueOf(dto.getProductId()));
					ProductInfoVo p = new ProductInfoVo();
					if (result == null || "".equals(result)) {
						ProductDTO pdto = productService.loadById(dto.getProductId());
						BeanUtils.copyProperties(pdto, p);
					} else {
						p = JSON.parseObject(result, ProductInfoVo.class);
					}
					oc.setMaxQuantity(p.getCapacitySize());

					List<OrderContainerDetail> orderContainerDetails = new ArrayList<OrderContainerDetail>();
					List<ContainerDetailDTO> containerDetailDtos = containerDetailService.listByContainerNo(dto.getNo());
					if (containerDetailDtos != null && containerDetailDtos.size() != 0) {
						for (ContainerDetailDTO cdto : containerDetailDtos) {
							OrderContainerDetail ocd = new OrderContainerDetail();
							BeanUtils.copyProperties(cdto, ocd, new String[] { "productDetail" });
							@SuppressWarnings("unchecked")
							Map<String, String> productDetail = JSON.parseObject(cdto.getProductDetail(), HashMap.class);
							ocd.setProductDetail(productDetail);
							orderContainerDetails.add(ocd);
						}
					}
					oc.setOrderContainerDetails(orderContainerDetails);
					orderContainers.add(oc);
				}
			}
		}
		orderDetail.setOrderContainers(orderContainers);
		LogisticsDTO logisticsDto = logisticsService.loadByOrderNo(order.getNo());
		if (logisticsDto != null) {
			BeanUtils.copyProperties(logisticsDto, orderDetail);
			orderDetail.setLogisticsId(logisticsDto.getId());
			orderDetail.setLogisticsType(logisticsDto.getType());
			// 加载清关,国内国外物流公司信息
			EnterpriseInfoDTO clearanceCompanyDto = enterpriseInfoService.loadById(logisticsDto.getClearanceCompanyId());
			EnterpriseInfoVo clearanceCompany = new EnterpriseInfoVo();
			BeanUtils.copyProperties(clearanceCompanyDto, clearanceCompany);
			orderDetail.setClearanceCompany(clearanceCompany);
			EnterpriseInfoDTO innerExpressDto = enterpriseInfoService.loadById(logisticsDto.getInnerExpressId());
			EnterpriseInfoVo innerExpress = new EnterpriseInfoVo();
			BeanUtils.copyProperties(innerExpressDto, innerExpress);
			orderDetail.setInnerExpress(innerExpress);
			EnterpriseInfoDTO outerExpressDto = enterpriseInfoService.loadById(logisticsDto.getOuterExpressId());
			EnterpriseInfoVo outerExpress = new EnterpriseInfoVo();
			BeanUtils.copyProperties(outerExpressDto, outerExpress);
			orderDetail.setOuterExpress(outerExpress);
		}
		return orderDetail;
	}

	/**
	 * 查询物流详情
	 * 
	 * @param containerNo
	 * @return
	 */
	public LogisticsDetailVo queryLogistics(String containerNo) {
		LogisticsDetailVo result = new LogisticsDetailVo();
		ContainerDTO container = containerService.loadByContainerId(containerNo);
		if(container!=null){
			result.setContainerNo(containerNo);
			result.setContainerName(container.getProductName());
			int containerStatus = container.getStatus();
			result.setContainerStatus(containerStatus);
			if (containerStatus >= ContainerStatusEnum.SHIPPED.getStatus()) {
				// 如果货柜状态是已发货，设置发货时间
				result.setDeliveryTime(container.getDeliveryTime());
			}
			if (containerStatus >= ContainerStatusEnum.CLEARED.getStatus()) {
				// 如果货柜状态是已清关，设置发货时间
				result.setPreReceiveTime(container.getPreReceiveTime());
				result.setClearFlag(true);
			}
			List<LogisticsDetailDTO> logisticsDetais = logisticsDetailService.loadByContainerNo(containerNo);
			List<LogisticsDetailBean> list = new ArrayList<LogisticsDetailBean>();
			if (logisticsDetais != null && logisticsDetais.size() != 0) {
				for (LogisticsDetailDTO dto : logisticsDetais) {
					LogisticsDetailBean bean = new LogisticsDetailBean();
					BeanUtils.copyProperties(dto, bean);
					List<BizFileVo> fileList = new ArrayList<BizFileVo>();
					List<BizFileDTO> files = bizFileService.listByBizIdAndType(String.valueOf(dto.getId()),
							BizFileEnum.LOGISTICS_DETAIL.getType());
					if (files != null && files.size() != 0) {
						for (BizFileDTO biz : files) {
							BizFileVo bizVo = new BizFileVo();
							BeanUtils.copyProperties(biz, bizVo);
							bizVo.setUrl(fileUploadService.buildDiskUrl(biz.getPath()));
							fileList.add(bizVo);
						}
					}
					bean.setFilePaths(fileList);
					list.add(bean);
				}
			}
			result.setLogisticsDetails(list);
		}else{//从新订单中查询
			result = containerNewService.getLogisticsDetailByContainerNo(containerNo);
		}
		return result;
	}

	/**
	 * 创建订单 发生异常则进行回滚
	 * 
	 * @param orderVo
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public AjaxResult createOrder(OrderVo orderVo) {
		String orderName = "";
		try {
			// 使用分布式锁防止重复生成订单
			long count = cacheClient.setnx("USER_" + orderVo.getUserId(), orderVo.getUserIp());
			if (count == 1) {
				cacheClient.expire("USER_" + orderVo.getUserId(), 3);
				String orderId = orderVo.getOrderId();
				OrderDTO oldOrder = orderService.loadByNo(orderId);
				if (oldOrder != null) {
					// 更新订单
					return updateOrder(orderVo, oldOrder);
				}
				OrderDTO order = new OrderDTO();

				BeanUtils.copyProperties(orderVo, order);
				order.setTransactionNo(BizUtils.getUUID());
				order.setStatus(OrderStatusEnum.SAVED.getStatus());
				StringBuilder uid = new StringBuilder();
				boolean flag = false;
				while (!flag) {
					uid = new StringBuilder();
					uid.append("1").append(orderVo.getType());
					SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
					uid.append(sdf.format(new Date())).append((int) (Math.random() * (9999 - 1000 + 1)) + 1000);
					if (orderService.loadByNo(uid.toString()) == null) {
						flag = true;
					}
				}
				orderName = uid.toString();
				order.setNo(orderName);
				order.setLastEditor("USER :" + orderVo.getUserId());

				BigDecimal productAmount = BigDecimal.ZERO;
				BigDecimal totalAmount = BigDecimal.ZERO;
				BigDecimal agencyAmount = BigDecimal.ZERO;
				BigDecimal premiumAmount = BigDecimal.ZERO;
				BigDecimal finalAmount = BigDecimal.ZERO;

				List<OrderContainer> orderContainers = orderVo.getOrderContainers();
				int i = 1;
				for (OrderContainer oc : orderContainers) {
					ContainerTmpDTO dto = new ContainerTmpDTO();
					BeanUtils.copyProperties(oc, dto);
					dto.setAddTime(new Date());
					dto.setUpdateTime(new Date());
					dto.setOrderNo(orderName);
					dto.setTransactionNo(BizUtils.getUUID());
					String batchNumber = uid.toString();
					if (i < 10) {
						batchNumber += "0";
					}
					batchNumber += i;
					dto.setNo(batchNumber);
					dto.setEditor("USER :" + orderVo.getUserId());
					dto.setStatus(ContainerStatusEnum.SAVED.getStatus());
					BigDecimal containerProductPrice = BigDecimal.ZERO;
					BigDecimal containerQuantity = BigDecimal.ZERO;
					// 第一步写货柜详情表order_container_detail
					List<OrderContainerDetail> ocds = oc.getOrderContainerDetails();
					for (OrderContainerDetail ocd : ocds) {
						ContainerDetailTmpDTO cddto = new ContainerDetailTmpDTO();
						BeanUtils.copyProperties(ocd, cddto, new String[] { "productDetail" });
						cddto.setProductDetail(JSON.toJSONString(ocd.getProductDetail()));
						cddto.setContainerNo(batchNumber);
						cddto.setStatus(ContainerStatusEnum.SAVED.getStatus());
						BigDecimal oddtoTotalPrice = ocd.getPrice().multiply(ocd.getQuantity());
						cddto.setTotalPrice(oddtoTotalPrice);
						containerProductPrice = containerProductPrice.add(oddtoTotalPrice);
						containerQuantity = containerQuantity.add(ocd.getQuantity());
						containerDetailTmpService.create(cddto);
					}
					dto.setTotalQuantity(containerQuantity);
					dto.setProductAmount(containerProductPrice);
					dto.setAgencyAmount(BigDecimal.ZERO);
					dto.setPremiumAmount(BigDecimal.ZERO);
					dto.setTotalPrice(containerProductPrice);
					// 第二步写订单货柜表order_container
					containerTmpService.insertSelective(dto);
					productAmount = productAmount.add(containerProductPrice);
					totalAmount = totalAmount.add(dto.getTotalPrice());
					agencyAmount = agencyAmount.add(dto.getAgencyAmount());
					premiumAmount = premiumAmount.add(dto.getPremiumAmount());
					i++;
				}
				finalAmount = totalAmount;
				order.setProductAmount(productAmount);
				order.setTotalAmount(totalAmount);
				order.setAgencyAmount(agencyAmount);
				order.setPremiumAmount(premiumAmount);
				order.setFinalAmount(finalAmount);
				// 第三步写订单表order_info
				orderService.create(order);
				// 同时添加订单变更日志
				final String orderNo = orderName;
				final int userId = orderVo.getUserId();

				//统计表
				orderLogService.addRecordTime(order.getNo(),OrderStatusEnum.SAVED.getStatus());

				orderTaskHelper.submitRunnable(new Runnable() {

					@Override
					public void run() {
						orderLogService.addLog(orderNo, userId, OrderUpdateTypeEnum.SAVE.getType(), 0,
								OrderStatusEnum.SAVED.getStatus(), OrderUpdateTypeEnum.SAVE.getMessage());
					}
				});
				// 释放锁
				cacheClient.del("USER_" + orderVo.getUserId());
			} else {
				return new AjaxResult(AjaxResultCode.REQUEST_INVALID.getCode(), "不能重复提交订单", null);
			}
		} catch (Exception e) {
			logger.error("the userId {} occur exception when committing order, the exception is : {}", orderVo.getUserId(),
					e.getMessage());
			EventHelper.triggerEvent(this.eventPublishers, "create.order." + orderVo.getUserId(), "failed to commit order "
					+ JSON.toJSONString(orderVo), EventLevel.URGENT, e, ServerIpUtils.getServerIp());
			throw new RuntimeException("提交订单出现异常" + e.getMessage());
		}
		return new AjaxResult(orderName);
	}

	private AjaxResult updateOrder(OrderVo orderVo, OrderDTO oldOrder) {
		/*
		 * if (oldOrder.getUserId() != orderVo.getUserId()) { return new
		 * AjaxResult(400, "该用户没有权限修改此订单", null); } if (oldOrder.getStatus() !=
		 * OrderStatusEnum.SAVED.getStatus()) { return new AjaxResult(400,
		 * "非暂存订单不能编辑", null); }
		 */
		int userId = oldOrder.getUserId();
		if (oldOrder.getStatus() >= OrderStatusEnum.CONFIRMED.getStatus()) {
			// 释放锁
			cacheClient.del("USER_" + orderVo.getUserId());
			return new AjaxResult(AjaxResultCode.REQUEST_INVALID.getCode(), "非法操作", null);
		}
		BeanUtils.copyProperties(orderVo, oldOrder);
		final String orderNo = orderVo.getOrderId();
		oldOrder.setLastEditor("USER :" + orderVo.getUserId());
		oldOrder.setUserId(userId);
		BigDecimal productAmount = BigDecimal.ZERO;
		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal agencyAmount = BigDecimal.ZERO;
		BigDecimal premiumAmount = BigDecimal.ZERO;
		BigDecimal finalAmount = BigDecimal.ZERO;

		List<OrderContainer> orderContainers = orderVo.getOrderContainers();
		List<ContainerTmpDTO> oldOrderContainers = containerTmpService.listByOrderNo(oldOrder.getNo());
		// 更新原来的全部货柜信息
		if (oldOrderContainers != null && oldOrderContainers.size() != 0) {
			int k = 0;
			for (ContainerTmpDTO cdto : oldOrderContainers) {

				List<ContainerDetailTmpDTO> oldOrderContainerDetails = containerDetailTmpService
						.listByContainerNo(cdto.getNo());
				// 删除原来的全部货柜详情
				for (ContainerDetailTmpDTO ocdtd : oldOrderContainerDetails) {
					ocdtd.setStatus(0);
					containerDetailTmpService.updateById(ocdtd);
				}
				BigDecimal containerProductPrice = BigDecimal.ZERO;
				BigDecimal containerQuantity = BigDecimal.ZERO;
				List<OrderContainerDetail> ocds = orderContainers.get(k).getOrderContainerDetails();
				for (OrderContainerDetail ocd : ocds) {
					ContainerDetailTmpDTO cddto = new ContainerDetailTmpDTO();
					BeanUtils.copyProperties(ocd, cddto, new String[] { "productDetail" });
					cddto.setProductDetail(JSON.toJSONString(ocd.getProductDetail()));
					cddto.setContainerNo(cdto.getNo());
					cddto.setStatus(ContainerStatusEnum.AUDIT.getStatus());
					BigDecimal oddtoTotalPrice = ocd.getPrice().multiply(ocd.getQuantity());
					cddto.setTotalPrice(oddtoTotalPrice);
					containerProductPrice = containerProductPrice.add(oddtoTotalPrice);
					containerQuantity = containerQuantity.add(ocd.getQuantity());
					containerDetailTmpService.create(cddto);
				}
				cdto.setTotalQuantity(containerQuantity);
				cdto.setProductAmount(containerProductPrice);
				cdto.setAgencyAmount(BigDecimal.ZERO);
				cdto.setPremiumAmount(BigDecimal.ZERO);
				cdto.setTotalPrice(containerProductPrice);
				// 第二步写订单货柜表order_container
				containerTmpService.updateById(cdto);

				productAmount = productAmount.add(containerProductPrice);
				totalAmount = totalAmount.add(cdto.getTotalPrice());
				agencyAmount = agencyAmount.add(cdto.getAgencyAmount());
				premiumAmount = premiumAmount.add(cdto.getPremiumAmount());
				k++;
			}
		}
		/*
		 * int i = 1; for (OrderContainer oc : orderContainers) {
		 * ContainerTmpDTO dto = new ContainerTmpDTO();
		 * BeanUtils.copyProperties(oc, dto); dto.setAddTime(new Date());
		 * dto.setUpdateTime(new Date());
		 * dto.setTransactionNo(BizUtils.getTradeNo()); String batchNumber =
		 * oldOrder.getNo(); if (i < 10) { batchNumber += "0"; } batchNumber +=
		 * i; dto.setNo(batchNumber); dto.setOrderNo(orderNo);
		 * dto.setTransactionNo(BizUtils.getTradeNo());
		 * dto.setStatus(ContainerStatusEnum.AUDIT.getStatus());
		 * dto.setEditor("USER :" + orderVo.getUserId()); BigDecimal
		 * containerProductPrice = BigDecimal.ZERO; BigDecimal containerQuantity
		 * = BigDecimal.ZERO; // 第一步写货柜详情表order_container_detail
		 * List<OrderContainerDetail> ocds = oc.getOrderContainerDetails(); for
		 * (OrderContainerDetail ocd : ocds) { ContainerDetailTmpDTO cddto = new
		 * ContainerDetailTmpDTO(); BeanUtils.copyProperties(ocd, cddto, new
		 * String[] { "productDetail" });
		 * cddto.setProductDetail(JSON.toJSONString(ocd.getProductDetail()));
		 * cddto.setContainerNo(batchNumber);
		 * cddto.setStatus(ContainerStatusEnum.AUDIT.getStatus()); BigDecimal
		 * oddtoTotalPrice = ocd.getPrice().multiply(ocd.getQuantity());
		 * cddto.setTotalPrice(oddtoTotalPrice); containerProductPrice =
		 * containerProductPrice.add(oddtoTotalPrice); containerQuantity =
		 * containerQuantity.add(ocd.getQuantity());
		 * containerDetailTmpService.create(cddto); }
		 * dto.setTotalQuantity(containerQuantity);
		 * dto.setProductAmount(containerProductPrice);
		 * dto.setAgencyAmount(BigDecimal.ZERO);
		 * dto.setPremiumAmount(BigDecimal.ZERO);
		 * dto.setTotalPrice(containerProductPrice); // 第二步写订单货柜表order_container
		 * containerTmpService.insertSelective(dto); productAmount =
		 * productAmount.add(containerProductPrice); totalAmount =
		 * totalAmount.add(dto.getTotalPrice()); agencyAmount =
		 * agencyAmount.add(dto.getAgencyAmount()); premiumAmount =
		 * premiumAmount.add(dto.getPremiumAmount()); i++; }
		 */
		finalAmount = totalAmount;
		oldOrder.setProductAmount(productAmount);
		oldOrder.setTotalAmount(totalAmount);
		oldOrder.setAgencyAmount(agencyAmount);
		oldOrder.setPremiumAmount(premiumAmount);
		oldOrder.setFinalAmount(finalAmount);
		// 第三步写订单表order_info
		orderService.update(oldOrder);
		// 同时添加订单变更日志
		// 同时添加订单变更日志
		final int opertatorId = orderVo.getUserId();

		//统计表
		orderLogService.addRecordTime(oldOrder.getNo(),OrderStatusEnum.SAVED.getStatus());

		orderTaskHelper.submitRunnable(new Runnable() {

			@Override
			public void run() {
				orderLogService.addLog(orderNo, opertatorId, OrderUpdateTypeEnum.SAVE.getType(), 0,
						OrderStatusEnum.SAVED.getStatus(), OrderUpdateTypeEnum.SAVE.getMessage());
			}
		});
		// 释放锁
		cacheClient.del("USER_" + orderVo.getUserId());
		return new AjaxResult(orderNo);
	}

	/**
	 * 预留接口
	 * 
	 * @param orderId
	 * @return
	 */
	public AjaxResult lastOrder(String orderId) {
		LastOrderVo vo = new LastOrderVo();
		OrderDTO order = orderService.loadByNo(orderId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		vo.setPlaceOrderTime(sdf.format(order.getAddTime()));
		vo.setOrderId(order.getNo());
		vo.setOrderStatus(order.getStatus());
		vo.setOrderStatusDesc(OrderStatusEnum.get(order.getStatus()).getSysDesc());
		OrderSearchRequest request = new OrderSearchRequest();
		request.setUserId(order.getUserId());
		request.setStatus(OrderStatusEnum.FINISHED.getStatus());
		int count = orderService.selectCountByExample(request);
		vo.setSuccessCount(count);
		if (count != 0) {
			List<OrderDTO> orders = orderService.paginateExpiredOrdersByPage(request);
			vo.setLastOrderId(orders.get(0).getNo());
			vo.setLastTime(sdf.format(orders.get(0).getAddTime()));
		}
		UserEnterpriseDTO enterprise = userEnterpriseService.loadByUserId(order.getUserId());
		vo.setContactMobile(enterprise.getPhoneNum());
		vo.setContactName(enterprise.getName());
		UserAccountDTO userInfo = userAccountService.loadById(order.getUserId());
		vo.setUserId(userInfo.getMobile());

		String purchaseName = "";
		if(enterprise != null) {
			purchaseName = enterprise.getType() == UserTypeEnum.PERSONAL.getType() ? enterprise.getName() : enterprise.getEnterpriseName();
		}
		vo.setPurchaserName(purchaseName);

		return new AjaxResult(vo);
	}

	/**
	 * 查询订单详情--Tab第一页
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public AjaxResult orderDetail(String orderId, int userId) {
		OrderDetailShowVo vo = new OrderDetailShowVo();
		OrderDTO order = orderService.loadByNo(orderId);
		vo.setId(order.getId());
		vo.setOrderNo(order.getNo());
		vo.setType(order.getType());
		vo.setStatus(order.getStatus());
		vo.setSupplierId(order.getSupplierId());
		vo.setOrderStatusDesc(OrderStatusEnum.get(order.getStatus()).getSysDesc());
		vo.setNeedLoan(order.getNeedLoan());
		List<OrderContainer> orderContainers = new ArrayList<OrderContainer>();

		if (isQueryTmp(order.getStatus())) {
			List<ContainerTmpDTO> containers = containerTmpService.listByOrderNo(order.getNo());
			if (containers != null && containers.size() != 0) {
				for (ContainerTmpDTO dto : containers) {
					OrderContainer oc = new OrderContainer();
					BeanUtils.copyProperties(dto, oc);
					oc.setBatchNumber(dto.getNo());

					String result = cacheClient.hget("goods", String.valueOf(dto.getProductId()));
					ProductInfoVo p = new ProductInfoVo();
					if (result == null || "".equals(result)) {
						ProductDTO pdto = productService.loadById(dto.getProductId());
						BeanUtils.copyProperties(pdto, p);
					} else {
						p = JSON.parseObject(result, ProductInfoVo.class);
					}
					oc.setMaxQuantity(p.getCapacitySize());

					List<OrderContainerDetail> orderContainerDetails = new ArrayList<OrderContainerDetail>();
					List<ContainerDetailTmpDTO> containerDetailDtos = containerDetailTmpService.listByContainerNo(dto.getNo());
					if (containerDetailDtos != null && containerDetailDtos.size() != 0) {
						for (ContainerDetailTmpDTO cdto : containerDetailDtos) {
							OrderContainerDetail ocd = new OrderContainerDetail();
							BeanUtils.copyProperties(cdto, ocd, new String[] { "productDetail" });
							@SuppressWarnings("unchecked")
							Map<String, String> productDetail = JSON.parseObject(cdto.getProductDetail(), HashMap.class);
							ocd.setProductDetail(productDetail);
							orderContainerDetails.add(ocd);
						}
					}
					oc.setOrderContainerDetails(orderContainerDetails);
					orderContainers.add(oc);
				}
			}
		} else {
			List<ContainerDTO> containers = containerService.listByOrderNo(order.getNo());
			if (containers != null && containers.size() != 0) {
				for (ContainerDTO dto : containers) {
					OrderContainer oc = new OrderContainer();
					BeanUtils.copyProperties(dto, oc);
					oc.setBatchNumber(dto.getNo());

					String result = cacheClient.hget("goods", String.valueOf(dto.getProductId()));
					ProductInfoVo p = new ProductInfoVo();
					if (result == null || "".equals(result)) {
						ProductDTO pdto = productService.loadById(dto.getProductId());
						BeanUtils.copyProperties(pdto, p);
					} else {
						p = JSON.parseObject(result, ProductInfoVo.class);
					}
					oc.setMaxQuantity(p.getCapacitySize());

					List<OrderContainerDetail> orderContainerDetails = new ArrayList<OrderContainerDetail>();
					List<ContainerDetailDTO> containerDetailDtos = containerDetailService.listByContainerNo(dto.getNo());
					if (containerDetailDtos != null && containerDetailDtos.size() != 0) {
						for (ContainerDetailDTO cdto : containerDetailDtos) {
							OrderContainerDetail ocd = new OrderContainerDetail();
							BeanUtils.copyProperties(cdto, ocd, new String[] { "productDetail" });
							@SuppressWarnings("unchecked")
							Map<String, String> productDetail = JSON.parseObject(cdto.getProductDetail(), HashMap.class);
							ocd.setProductDetail(productDetail);
							orderContainerDetails.add(ocd);
						}
					}
					oc.setOrderContainerDetails(orderContainerDetails);
					orderContainers.add(oc);
				}
			}
		}
		vo.setOrderContainers(orderContainers);

		List<UserSupplierDTO> supplierDTOs = supplierService.loadAllSupplier(order.getUserId());
		List<SupplierVo> supplierList = this.supplierService.wrapVOs(supplierDTOs);
		vo.setSupplierList(supplierList);
		return new AjaxResult(vo);
	}

	/**
	 * 查询物流服务信息
	 * 
	 * @param orderId
	 * @return
	 */
	public AjaxResult logisticsDetail(String orderId) {
		LogisticsDetailShowVo vo = new LogisticsDetailShowVo();
		LogisticsDTO logisticsDto = logisticsService.loadByOrderNo(orderId);
		OrderDTO order = orderService.loadByNo(orderId);
		vo.setNeedLoan(order.getNeedLoan());
		vo.setBackMemo(order.getBackMemo());
		if (logisticsDto != null) {
			BeanUtils.copyProperties(logisticsDto, vo);
			vo.setLogisticsType(logisticsDto.getType());
			// 加载清关,国内国外物流公司信息
			EnterpriseInfoDTO clearanceCompanyDto = enterpriseInfoService.loadById(logisticsDto.getClearanceCompanyId());
			EnterpriseInfoVo clearanceCompany = new EnterpriseInfoVo();
			BeanUtils.copyProperties(clearanceCompanyDto, clearanceCompany);
			vo.setClearanceCompany(clearanceCompany);
			EnterpriseInfoDTO innerExpressDto = enterpriseInfoService.loadById(logisticsDto.getInnerExpressId());
			EnterpriseInfoVo innerExpress = new EnterpriseInfoVo();
			BeanUtils.copyProperties(innerExpressDto, innerExpress);
			vo.setInnerExpress(innerExpress);
			EnterpriseInfoDTO outerExpressDto = enterpriseInfoService.loadById(logisticsDto.getOuterExpressId());
			EnterpriseInfoVo outerExpress = new EnterpriseInfoVo();
			BeanUtils.copyProperties(outerExpressDto, outerExpress);
			vo.setOuterExpress(outerExpress);
		}
		// 加载收货地址信息
		DeliveryInfoDTO deliveryInfoDto = deliveryInfoService.loadByOrderNo(orderId);
		if (deliveryInfoDto != null) {
			DeliveryInfoVo deliveryInfo = new DeliveryInfoVo();
			BeanUtils.copyProperties(deliveryInfoDto, deliveryInfo);
			deliveryInfo.setCountryName(countryService.loadById(deliveryInfoDto.getCountryId()).getName());
			deliveryInfo.setProvinceName(provinceService.loadById(deliveryInfoDto.getProvinceId()).getName());
			deliveryInfo.setCityName(cityService.loadById(deliveryInfoDto.getCityId()).getName());
			deliveryInfo.setDistrictName(areaService.loadById(deliveryInfoDto.getDistrictId()).getName());
			vo.setDeliveryAddress(deliveryInfo);
		}
		// 加载所有的收货地址
		List<UserDeliveryAddressDTO> deliveryAddressDTOs = delieryAddressService.loadAllAddress(order.getUserId());
		List<AddressVo> receiveAddress = delieryAddressService.wrapVOs(deliveryAddressDTOs);
		vo.setDeliveryAddressList(receiveAddress);
		vo.setAdvance(order.getAdvance());
		vo.setRestPay(order.getRestPay());
		vo.setPayType(order.getPayType());
		List<OrderContainerFee> orderContainers = new ArrayList<OrderContainerFee>();

		if (isQueryTmp(order.getStatus())) {
			List<ContainerTmpDTO> containers = containerTmpService.listByOrderNo(order.getNo());
			if (containers != null && containers.size() != 0) {
				for (ContainerTmpDTO dto : containers) {
					OrderContainerFee oc = new OrderContainerFee();
					BeanUtils.copyProperties(dto, oc);
					oc.setBatchNumber(dto.getNo());
					orderContainers.add(oc);
				}
			}
		} else {
			List<ContainerDTO> containers = containerService.listByOrderNo(order.getNo());
			if (containers != null && containers.size() != 0) {
				for (ContainerDTO dto : containers) {
					OrderContainerFee oc = new OrderContainerFee();
					BeanUtils.copyProperties(dto, oc);
					oc.setBatchNumber(dto.getNo());
					orderContainers.add(oc);
				}
			}
		}
		vo.setFeeList(orderContainers);
		return new AjaxResult(vo);
	}

	/**
	 * 更新物流服务信息 发生异常则进行回滚
	 * 
	 * @param orderLogistics
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public AjaxResult updateLogistics(OrderLogistics orderLogistics) {
		OrderDTO order = orderService.loadByNo(orderLogistics.getOrderNo());
		if (order.getStatus() >= OrderStatusEnum.SIGNED_CONTRACT.getStatus()) {
			return new AjaxResult(AjaxResultCode.REQUEST_INVALID.getCode(), "非法操作", false);
		}
		LogisticsDTO dto = new LogisticsDTO();
		BeanUtils.copyProperties(orderLogistics, dto);
		logisticsService.update(dto);
		if (isQueryTmp(order.getStatus())) {
			BigDecimal productAmount = BigDecimal.ZERO;
			BigDecimal totalAmount = BigDecimal.ZERO;
			BigDecimal agencyAmount = BigDecimal.ZERO;
			BigDecimal premiumAmount = BigDecimal.ZERO;
			// 更新费用清单
			List<OrderContainerFee> feeList = orderLogistics.getFeeList();
			for (OrderContainerFee fee : feeList) {
				ContainerTmpDTO cdtdto = containerTmpService.loadById(fee.getId());
				BigDecimal conProductAmount = cdtdto.getProductAmount();
				BigDecimal conTotalAmount = BigDecimal.ZERO;
				BigDecimal conAgencyAmount = fee.getAgencyAmount();
				BigDecimal conPremiumAmount = fee.getPremiumAmount();
				cdtdto.setAgencyAmount(conAgencyAmount);
				cdtdto.setPremiumAmount(conPremiumAmount);
				// cdtdto.setProductAmount(conProductAmount);
				conTotalAmount = conTotalAmount.add(fee.getAgencyAmount()).add(fee.getPremiumAmount()).add(conProductAmount);
				cdtdto.setTotalPrice(conTotalAmount);
				productAmount = productAmount.add(conProductAmount);
				agencyAmount = agencyAmount.add(conAgencyAmount);
				premiumAmount = premiumAmount.add(conPremiumAmount);
				totalAmount = totalAmount.add(conTotalAmount);
				containerTmpService.updateById(cdtdto);
			}
			BigDecimal finalAmount = totalAmount;
			order.setAgencyAmount(agencyAmount);
			order.setPremiumAmount(premiumAmount);
			order.setProductAmount(productAmount);
			order.setTotalAmount(totalAmount);
			order.setFinalAmount(finalAmount);
			order.setAdvance(orderLogistics.getAdvance());
			order.setRestPay(orderLogistics.getRestPay());

			// 添加收货地址信息
			UserDeliveryAddressDTO deliveryAddressDTO = userDeliveryAddressService.loadById(orderLogistics.getDeliveryId());
			DeliveryInfoDTO oldDeliveryInfoDTO = deliveryInfoService.loadByOrderNo(order.getNo());
			DeliveryInfoDTO deliveryInfoDto = new DeliveryInfoDTO();
			BeanUtils.copyProperties(deliveryAddressDTO, deliveryInfoDto, new String[] { "updateTime" });
			deliveryInfoDto.setOrderNo(orderLogistics.getOrderNo());
			deliveryInfoDto.setId(oldDeliveryInfoDTO.getId());
			deliveryInfoService.update(deliveryInfoDto);
			// 如果需要资金服务，更新订单表
			Integer needLoan = orderLogistics.getNeedLoan();
			if (needLoan != null && needLoan == 1) {
				order.setNeedLoan(needLoan);
				List<ApplyLoanInfoVo> loanInfos = orderLogistics.getLoadInfo();
				List<LoanInfoDTO> loanInfoList = new ArrayList<LoanInfoDTO>();
				if (loanInfos != null && loanInfos.size() != 0) {
					int userId = order.getUserId();
					for (ApplyLoanInfoVo loan : loanInfos) {
						LoanInfoDTO loanInfoDTO = new LoanInfoDTO();
						loanInfoDTO.setOrderNo(order.getNo());
						loanInfoDTO.setUserId(userId);
						loanInfoDTO.setTransactionNo(loan.getTransactionNo());
						loanInfoDTO.setAvailableLoan(loan.getLoanQuota());
						BigDecimal applyQuota = loan.getApplyQuota();
						if (applyQuota.compareTo(new BigDecimal(100)) < 1) {
							throw new IllegalArgumentException("贷款不能少于100");
						}
						loanInfoDTO.setAppliyLoan(applyQuota);
						loanInfoDTO.setConfirmLoan(loan.getConfirmLoan());
						loanInfoDTO.setProductId(loan.getProductId());
						loanInfoDTO.setProdictName(loan.getProductName());
						loanInfoList.add(loanInfoDTO);
					}
					userLoanManagementListService.createLoanInfos(loanInfoList, userId);
				}
			}
			order.setPayType(orderLogistics.getPayType());
			order.setBackMemo(orderLogistics.getBackMemo());
			orderService.update(order);
		}
		return new AjaxResult(true);
	}

	public List<ContainerSimpleInfoVo> queryContainerSimpleInfo(String orderId) {
		List<ContainerSimpleInfoVo> result = new ArrayList<ContainerSimpleInfoVo>();
		List<ContainerDTO> containers = containerService.listByOrderNo(orderId);
		if (containers != null && containers.size() != 0) {
			for (ContainerDTO c : containers) {
				ContainerSimpleInfoVo cv = new ContainerSimpleInfoVo();
				cv.setContainerId(c.getNo());
				cv.setTransactionNo(c.getTransactionNo());
				ContainerInsuranceDTO insurance = containerInsuranceService.loadByContainerNo(c.getTransactionNo());
				if (insurance != null) {
					cv.setContractNumber(insurance.getContractNumber());
				}
				result.add(cv);
			}
		}
		return result;
	}

	/**
	 * 添加或更新保险单号
	 * 
	 * @param result
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addInsurance(List<ContainerSimpleInfoVo> result) {
		for (ContainerSimpleInfoVo vo : result) {
			// ContainerSimpleInfoVo vo =
			// JSON.parseObject(jo.toJSONString(),ContainerSimpleInfoVo.class);
			ContainerInsuranceDTO insurance = containerInsuranceService.loadByContainerNo(vo.getTransactionNo());
			if (insurance != null) {
				insurance.setContractNumber(vo.getContractNumber());
				containerInsuranceService.updateByPrimaryKeySelective(insurance);
			} else {
				ContainerInsuranceDTO cin = new ContainerInsuranceDTO();
				cin.setContractNumber(vo.getContractNumber());
				cin.setContainerNo(vo.getTransactionNo());
				containerInsuranceService.insertSelective(cin);
			}
		}
	}

	/*
	 * 新增物流信息
	 * 
	 * @param logistics
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addLogisticsDetail(AddLogisticsInfoBean logistics) {
		int driverId = 0;
		OrderDTO order = orderService.loadByNo(logistics.getOrderNo());
		String containerNo = logistics.getContainerNo();
		final int userId = order.getUserId();
		UserAccountDTO userInfo = userAccountService.loadById(userId);
		final String mobile = userInfo.getMobile();
		ContainerDTO container = containerService.loadByContainerId(containerNo);
		if (container != null && ContainerStatusEnum.SHIP_PEND.getStatus() == container.getStatus()) {
			// 如果货柜状态是待发货，则该条物流信息是第一条,更新货柜发货时间
			final String content = DefaultOrderProcessor.smsTemplates.get(OrderStatusEnum.SHIPPED)
					.replace("{orderId}", order.getNo()).replace("{containerId}", container.getNo())
					.replace("{containerName}", container.getProductName());
			container.setDeliveryTime(new Date());
			// 同时更新货柜状态为已发货 并判断是否要更新订单状态
			container.setStatus(ContainerStatusEnum.SHIPPED.getStatus());
			containerService.updateById(container);
			orderTaskHelper.submitRunnable(new Runnable() {

				@Override
				public void run() {
					messageService.sendSms(userId, mobile, content);
				}
			});
			boolean flag = true;
			List<ContainerDTO> containers = containerService.listByOrderNo(order.getNo());
			if (null != containers && containers.size() != 0) {
				for (ContainerDTO con : containers) {
					if (con.getStatus() < ContainerStatusEnum.SHIPPED.getStatus()) {
						flag = false;
						break;
					}
				}
			}
			if (flag) {
				// 更新订单状态为已发货
				final int logStatus = order.getStatus();//原状态
				final String logOrderNo =order.getNo();

				order.setStatus(OrderStatusEnum.SHIPPED.getStatus());
				orderService.update(order);
				//统计表
				orderLogService.addRecordTime(order.getNo(),OrderStatusEnum.SHIPPED.getStatus());

				// 同时记录订单变更日志
				orderTaskHelper.submitRunnable(new Runnable() {
					@Override
					public void run() {
						orderLogService.addLog(logOrderNo, userId, OrderUpdateTypeEnum.STATUS.getType(), logStatus, OrderStatusEnum.SHIPPED.getStatus(), OrderUpdateTypeEnum.STATUS
								.getMessage());
					}
				});

			}

			// 如果发货时间是6:00-16:00立即发送消息到队列
			// 如果发货时间是16:00-6:00延迟到第二天7点发送
//			Calendar calendar = Calendar.getInstance();
//			calendar.set(Calendar.HOUR_OF_DAY, 16);
//			calendar.set(Calendar.MINUTE, 0);
//			calendar.set(Calendar.SECOND, 0);
//			Date current16H = calendar.getTime();
//			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
//			calendar.set(Calendar.HOUR_OF_DAY, 7);
//			Date tomorrow7H = calendar.getTime();
//			long delay = 0;
//			if (new Date().after(current16H)) {
//				delay = tomorrow7H.getTime() - new Date().getTime();
//			}
//			final long delayMis = delay;
//			final String projectCode = order.getTransactionNo();
//			final String transactionNo = container.getTransactionNo();
//			TaskProcessUtil.getMtTaskProcess().asyncExecuteTask(new TaskAction<Object>() {
//
//				@Override
//				public Object doInAction() throws Exception {
//					DebtMQbean bean = new DebtMQbean();
//					bean.setProjectCode(projectCode);
//					bean.setTransactionNo(transactionNo);
//					bean.setUserId(userId);
//					pushDebtContractProxy.sendMsgCon(JsonUtil.toString(bean), delayMis);
//					return null;
//				}
//			});
			// 同时通知资金服务发放贷款
			// 短信通知放款
			sendMsgOfPayPre(container);
			userLoanManagementListService.updateFromAuditToLoan(container.getTransactionNo());
		}
		if (container != null && ContainerStatusEnum.SHIPPED.getStatus() == container.getStatus()
				&& (logistics.getPreReceiveTime() != null && !"".equals(logistics.getPreReceiveTime()))) {
			if (logistics.getDriverMobile() != null && !"".equals(logistics.getDriverMobile())) {
				// 如果老司机手机号不为空，则绑定老司机
				driverId = driversMemberService.getDriverIdByMobile(logistics.getDriverMobile());
			}

			// 如果货柜状态是已发货，并且预计收货时间不为空,则更新货柜预计到货时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				container.setClearanceTime(new Date());
				container.setPreReceiveTime(sdf.parse(logistics.getPreReceiveTime()));
			} catch (ParseException e) {
				logger.info("订单编号为: {} 下的货柜 (编号为: {}) 新增物流信息出现异常: ", new Object[] { order.getNo(), containerNo, e });
				throw new RuntimeException("转换时间出现异常: " + e.getMessage());
			}
			if (order.getNeedLoan() == 1) {
				// 同时更新货柜状态待还款
				container.setStatus(ContainerStatusEnum.REPAYMENT.getStatus());
				// 同时发送预计到货时间给资金服务
				userLoanManagementListService.updateFromLoanToPrepay(container.getTransactionNo(),
						container.getPreReceiveTime());
				// 还款提醒
				sendmMsgRepay(container);
			} else {
				container.setStatus(ContainerStatusEnum.SETTLEMENTED.getStatus());
			}
			containerService.updateById(container);
			//更新订单状态
			boolean flag = true;
			Date nullDate = DateUtil.StringToDate("0001-01-01","YYYY-MM-DD");
			List<ContainerDTO> containers = containerService.listByOrderNo(order.getNo());
			if (null != containers && containers.size() != 0) {
				for (ContainerDTO con : containers) {
					if ( !(con.getStatus() >= ContainerStatusEnum.CLEARED.getStatus()
							&& con.getPreReceiveTime().compareTo(nullDate) > 0) ) {
						//未清关
						flag = false;
						break;
					}
				}
			}
			if (flag) {
				// 更新订单状态为已清关
				final int logStatus = order.getStatus();//原状态
				final String logOrderNo =order.getNo();
				order.setStatus(OrderStatusEnum.CLEARANCED.getStatus());
				orderService.update(order);
				//统计表
				orderLogService.addRecordTime(order.getNo(),OrderStatusEnum.CLEARANCED.getStatus());

				// 同时记录订单变更日志
				orderTaskHelper.submitRunnable(new Runnable() {
					@Override
					public void run() {
						orderLogService.addLog(logOrderNo, userId, OrderUpdateTypeEnum.STATUS.getType(), logStatus, OrderStatusEnum.CLEARANCED.getStatus(), OrderUpdateTypeEnum.STATUS
								.getMessage());
					}
				});

			}


			final String content = DefaultOrderProcessor.smsTemplates.get(OrderStatusEnum.CLEARANCED)
					.replace("{orderId}", order.getNo()).replace("{containerId}", container.getNo())
					.replace("{containerName}", container.getProductName())
					.replace("{preReceiveTime}", logistics.getPreReceiveTime());
			orderTaskHelper.submitRunnable(new Runnable() {

				@Override
				public void run() {
					messageService.sendSms(userId, mobile, content);
				}
			});
			//微信提示

		}
		if (container != null && logistics.getReceiveFlag() == 1) {
			if (ContainerStatusEnum.REPAYMENT.getStatus() == container.getStatus()) {
				logger.info("该货柜贷款可能未还, 请确认");
				throw new IllegalArgumentException("该货柜贷款可能未还, 请确认");
			}

			// 如果货柜状态是待收货，并且确认已签收，则该条物流信息是最后一条
			// 同时更新货柜状态 并判断是否要更新订单状态
			if (ContainerStatusEnum.SETTLEMENTED.getStatus() == container.getStatus()) {
				container.setStatus(ContainerStatusEnum.RECEIVED.getStatus());
				container.setReceiveTime(new Date());
				containerService.updateById(container);
				final String content = DefaultOrderProcessor.smsTemplates.get(OrderStatusEnum.RECEIVED)
						.replace("{orderId}", order.getNo()).replace("{containerId}", container.getNo())
						.replace("{containerName}", container.getProductName());
				orderTaskHelper.submitRunnable(new Runnable() {

					@Override
					public void run() {
						messageService.sendSms(userId, mobile, content);
					}
				});
			}
			boolean flag = true;
			List<ContainerDTO> containers = containerService.listByOrderNo(order.getNo());
			if (null != containers && containers.size() != 0) {
				for (ContainerDTO con : containers) {
					if (con.getStatus() < ContainerStatusEnum.RECEIVED.getStatus()) {
						flag = false;
						break;
					}
				}
			}
			if (flag) {
				// 更新订单状态为已完成
				final int logStatus = order.getStatus();//原状态
				final String logOrderNo =order.getNo();
				order.setStatus(OrderStatusEnum.FINISHED.getStatus());
				order.setFinishTime(new Date());
				orderService.update(order);
				//统计表
				orderLogService.addRecordTime(order.getNo(),OrderStatusEnum.FINISHED.getStatus());

				// 同时记录订单变更日志
				orderTaskHelper.submitRunnable(new Runnable() {
					@Override
					public void run() {
						orderLogService.addLog(logOrderNo, userId, OrderUpdateTypeEnum.STATUS.getType(), logStatus, OrderStatusEnum.FINISHED.getStatus(), OrderUpdateTypeEnum.STATUS
								.getMessage());
					}
				});

			}
		}

		// 新增物流详情
		LogisticsDetailDTO model = new LogisticsDetailDTO();
		model.setContainerNo(containerNo);
		model.setDetailInfo(logistics.getDetailInfo());
		model.setLogisticsId(logistics.getLogisticsId());
		model.setDriverId(driverId);
		long id = logisticsDetailService.insertSelective(model);
		List<String> filePaths = logistics.getFilePaths();
		if (filePaths != null && filePaths.size() != 0) {
			for (String path : filePaths) {
				BizFileDTO bizFile = new BizFileDTO();
				bizFile.setPath(path);
				bizFile.setAddTime(new Date());
				bizFile.setStatus(1);
				bizFile.setBizType(BizFileEnum.LOGISTICS_DETAIL.getType());
				bizFile.setBizId(String.valueOf(id));
				bizFileService.create(bizFile);
			}
		}
	}

	public OrderForLogisticsVo queryOrderForLogistics(String orderId) {
		OrderForLogisticsVo vo = new OrderForLogisticsVo();
		OrderDTO order = orderService.loadByNo(orderId);
		vo.setOrderNo(order.getNo());
		vo.setPlaceOrderTime(new SimpleDateFormat("yyyy-MM-dd").format(order.getAddTime()));
		List<ContainerDTO> containers = containerService.listByOrderNo(order.getNo());
		List<String> orderContainers = new ArrayList<String>();
		if (containers != null && containers.size() != 0) {
			for (ContainerDTO dto : containers) {
				orderContainers.add(dto.getNo());
			}
		}
		vo.setOrderContainers(orderContainers);
		LogisticsDTO logisticsDto = logisticsService.loadByOrderNo(orderId);
		if (logisticsDto != null) {
			BeanUtils.copyProperties(logisticsDto, vo);
			vo.setLogiticsId(logisticsDto.getId());
			// 加载清关,国内国外物流公司信息
			EnterpriseInfoDTO innerExpressDto = enterpriseInfoService.loadById(logisticsDto.getInnerExpressId());
			EnterpriseInfoVo innerExpress = new EnterpriseInfoVo();
			BeanUtils.copyProperties(innerExpressDto, innerExpress);
			vo.setInnerExpress(innerExpress);
			EnterpriseInfoDTO outerExpressDto = enterpriseInfoService.loadById(logisticsDto.getOuterExpressId());
			EnterpriseInfoVo outerExpress = new EnterpriseInfoVo();
			BeanUtils.copyProperties(outerExpressDto, outerExpress);
			vo.setOuterExpress(outerExpress);
		}
		return vo;
	}

	/**
	 * 资金服务查询货柜信息
	 * 
	 * @param orderId
	 * @return
	 */
	public List<ContainerLoanVo> queryContainer(String orderId) {
		List<ContainerLoanVo> result = new ArrayList<ContainerLoanVo>();
		OrderDTO order = orderService.loadByNo(orderId);
		int userId = order.getUserId();
		if (isQueryTmp(order.getStatus())) {
			List<ContainerTmpDTO> containers = containerTmpService.listByOrderNo(order.getNo());
			if (containers != null && containers.size() != 0) {
				for (ContainerTmpDTO dto : containers) {
					ContainerLoanVo vo = new ContainerLoanVo();
					vo.setContainerId(dto.getNo());
					vo.setProductName(dto.getProductName());
					vo.setProductId(dto.getProductId());
					vo.setTransactionNo(dto.getTransactionNo());
					UserProductLoanInfoDTO quota = sysUserProductLoanService.loadByExample(dto.getProductId(), userId);
					if (quota != null) {
						vo.setLoanQuota(quota.getProductLoan());
					} else {
						vo.setLoanQuota(BigDecimal.ZERO);
					}
					LoanInfoModel loanInfo = userLoanListService.loadLoanInfosByTransactionNo(dto.getTransactionNo());
					if (loanInfo != null) {
						vo.setApplyQuota(loanInfo.getAppliyLoan());
						vo.setConfirmLoan(loanInfo.getConfirmLoan());
						vo.setServiceFee(loanInfo.getServiceFee());
					}
					result.add(vo);
				}
			}
		} else {
			List<ContainerDTO> containers = containerService.listByOrderNo(order.getNo());
			if (containers != null && containers.size() != 0) {
				for (ContainerDTO dto : containers) {
					ContainerLoanVo vo = new ContainerLoanVo();
					vo.setContainerId(dto.getNo());
					vo.setProductName(dto.getProductName());
					vo.setProductId(dto.getProductId());
					vo.setTransactionNo(dto.getTransactionNo());
					UserProductLoanInfoDTO quota = sysUserProductLoanService.loadByExample(dto.getProductId(), userId);
					if (quota != null) {
						vo.setLoanQuota(quota.getProductLoan());
					} else {
						vo.setLoanQuota(BigDecimal.ZERO);
					}
					LoanInfoModel loanInfo = userLoanListService.loadLoanInfosByTransactionNo(dto.getTransactionNo());
					if (loanInfo != null) {
						vo.setApplyQuota(loanInfo.getAppliyLoan());
						vo.setConfirmLoan(loanInfo.getConfirmLoan());
						vo.setServiceFee(loanInfo.getServiceFee());
					}
					result.add(vo);
				}
			}
		}
		return result;
	}

	/**
	 * 根据订单号查询订单信息
	 * 
	 * @param orderId
	 * @return
	 */
	public OrderDTO queryOrderInfo(String orderId) {
		OrderDTO order = orderService.loadByNo(orderId);
		return order;
	}

	/**
	 * 更新订单备注
	 * 
	 * @param orderId
	 * @param backMemo
	 */
	public void updateOrderMemo(String orderId, String backMemo) {
		OrderDTO oldOrder = orderService.loadByNo(orderId);
		oldOrder.setBackMemo(backMemo);
		orderService.update(oldOrder);
	}

	/**
	 * 为地图查询参数
	 * 
	 * @param containerId
	 * @return
	 */
	public Map<String, Object> queryLogisticsMap(String containerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		ContainerDTO con = containerService.loadByContainerId(containerId);
		if (con.getStatus() < ContainerStatusEnum.SHIPPED.getStatus()) {
			throw new NullPointerException("非法请求");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (con.getStatus() > ContainerStatusEnum.SHIPPED.getStatus()) {
			map.put("clearFlag", true);
			map.put("clearanceTime", sdf.format(con.getClearanceTime()));
			map.put("preReceiveTime", sdf.format(con.getPreReceiveTime()));
		} else {
			map.put("clearFlag", false);
			map.put("clearanceTime", null);
			map.put("preReceiveTime", null);
		}

		map.put("clearAddress", "广西凭祥友谊关");
		// 加载收货地址信息
		DeliveryInfoDTO deliveryInfoDto = deliveryInfoService.loadByOrderNo(con.getOrderNo());
		StringBuilder receiverAddress = new StringBuilder();
		receiverAddress.append(provinceService.loadById(deliveryInfoDto.getProvinceId()).getName())
				.append(cityService.loadById(deliveryInfoDto.getCityId()).getName())
				.append(areaService.loadById(deliveryInfoDto.getDistrictId()).getName());
		map.put("receiverAddress", receiverAddress.toString());
		map.put("deliveryTime", sdf.format(con.getDeliveryTime()));
		return map;
	}

	/**
	 * 根据订单状态判断查询正式表还是临时表
	 * 
	 * @param status
	 * @return
	 */
	private boolean isQueryTmp(int status) {
		if (status == 1 || status == 2 || status == 3) {
			return true;
		}
		return false;
	}

	/**
	 * 放款通知提醒
	 * 
	 * @param container
	 */
	private void sendMsgOfPayPre(ContainerDTO container) {
		String template = "【九创金服】尊敬的客户，您的订单{0}中的货柜{1}已发货，该货柜资金服务申请已审批通过，资金预计将在2小时左右发放，请留意您的银行卡到账通知，如有疑问，请致电4008-265-128。";

		LoanInfoDTO loanInfoDTO = loanInfoService.loadByTransactionNo(container.getTransactionNo());
		if (loanInfoDTO != null && loanInfoDTO.getStatus() == LoanInfoStatusEnum.PENDING_AUDIT.getStatus()) {

			int userId = loanInfoDTO.getUserId();

			// 根据userId查询实名认证信息
			LoanUserAuthInfoDTO loanUserAuthInfoDTO = loanUserAuthInfoService.loadByUserId(userId);

			String content = MessageFormat.format(template, container.getOrderNo(),
					container.getNo() + container.getProductName());

			loanMessageService.sendSms(loanInfoDTO.getUserId(), loanUserAuthInfoDTO.getMobile(), content);

		}

	}

	/**
	 * 发送还款短信提醒
	 * 
	 * @param container
	 */
	private void sendmMsgRepay(ContainerDTO container) {

		LoanInfoDTO loanInfo = loanInfoService.loadByTransactionNo(container.getTransactionNo());
		// 有贷款信息
		if (loanInfo != null && StringUtils.isNotEmpty(loanInfo.getDbtNo())
				&& (loanInfo.getStatus() == LoanInfoStatusEnum.SECURED_LOAN.getStatus() || loanInfo.getStatus() == LoanInfoStatusEnum.REPAYMENT.getStatus())
		) {
			// 根据userId查询实名认证信息
			LoanUserAuthInfoDTO loanUserAuthInfoDTO = loanUserAuthInfoService.loadByUserId(loanInfo.getUserId());

			BigDecimal offerLoan = loanInfo.getOfferLoan();// 本金
			Date OfferTime = loanInfo.getOfferTime();
			Date expiresTime = loanInfo.getExpiresTime();// 到期强制还款时间
			String expiresTimeStr = DateUtil.getDate(expiresTime);
			int interestDays = DateUtil.getIntervalDays(OfferTime, expiresTime);
			// 利率
			double percent = Double.valueOf(runtimeConfigurationService.getConfig(
					RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, BaseRuntimeConfig.PERFORMANCE_RATE));
			// 利息
			double interest = calculateInterest(offerLoan, percent, interestDays);

			double principal = offerLoan.doubleValue() + interest;
			String template = "【九创金服】尊敬的客户，您的订单{0}中的货柜{1}所使用资金将于{2}到期，请于{3}15:00前预存{4}元，以免影响您的会员等级。如有疑问，请致电4008-265-128，感谢您的理解和支持！";
			String content = MessageFormat.format(template, container.getOrderNo(),
					container.getNo() + " " + container.getProductName(), expiresTimeStr, expiresTimeStr + " ", principal);

			loanMessageService.sendSms(loanInfo.getUserId(), loanUserAuthInfoDTO.getMobile(), content);


		}
	}

	/**
	 * 通过本金、利率、借款天数来计算利息
	 * 
	 * @param offerLoan
	 * @param percent
	 * @param interestDays
	 */
	private double calculateInterest(BigDecimal offerLoan, double percent, int interestDays) {
		double interest = offerLoan.doubleValue() * percent * interestDays / 360;

		BigDecimal interestBig = MathUtil.doubleToBigDecima(interest);// 保留两位小数

		return interestBig.doubleValue();
	}

	private String getCurrentConfig(LoanSmsContactsConfigDTO config) {
		String value;
		if ("product".equals(EnvService.getEnv())) {
			value = config.getProduct();
		} else if ("beta".equals(EnvService.getEnv())) {
			value = config.getBeta();
		} else if ("dev".equals(EnvService.getEnv())) {
			value = config.getDev();
		} else {
			value = config.getAlpha();
		}
		return String.valueOf(value);
	}

}
