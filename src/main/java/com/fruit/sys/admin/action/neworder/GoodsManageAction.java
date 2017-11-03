package com.fruit.sys.admin.action.neworder;

import com.fruit.newOrder.biz.dto.GoodsCategoryDTO;
import com.fruit.newOrder.biz.dto.GoodsVarietyDTO;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.neworder.GoodsCommodityInfoVO;
import com.fruit.sys.admin.model.neworder.GoodsProductInfoVO;
import com.fruit.sys.admin.model.neworder.UpdateCommodityPriceBean;
import com.fruit.sys.admin.model.portal.AjaxResult;
import com.fruit.sys.admin.model.portal.AjaxResultCode;
import com.fruit.sys.admin.service.neworder.GoodsInfoShowService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 商品管理
 * 
 * @author paul
 *
 */
@Component
@UriMapping("/neworder/goods")
public class GoodsManageAction extends BaseAction {

	@Autowired
	private GoodsInfoShowService goodsInfoShowService;

	private static final Logger logger = LoggerFactory.getLogger(GoodsManageAction.class);


	/**
	 * 商品价格管理--每日
	 * @return
     */
	@UriMapping("/commodity_price_manage_day")
	public String commodityPriceManage() {
		return "/neworder/commodity_price_manage_day";
	}



	/**
	 * 查询所有商品类别
	 *
	 * @return
	 */
	@UriMapping(value = "/find_all_goods_categories")
	public AjaxResult findAllGoodsCategories() {
		int code = AjaxResultCode.OK.getCode();
		String msg = SUCCESS;
		try
		{
			Map<String, Object> dataMap = new HashMap<String, Object>();
			AjaxResult ajaxResult = new AjaxResult(code, msg);

			List<GoodsCategoryDTO> goodsCategories = goodsInfoShowService.loadAllGoodsCategories();
			dataMap.put("goodsCategories", goodsCategories);
			ajaxResult.setData(dataMap);

			return ajaxResult;
		}
		catch (IllegalArgumentException e)
		{
			logger.error("[/neworder/goods/findAllGoodsCategories].Exception:{}",e);
			return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
		}
	}


	/**
	 * 根据商品类别获取商品品种
	 *
	 * @return
	 */
	@UriMapping(value = "/find_goods_varieties_by_category" , interceptors = {"validationInterceptor"})
	public AjaxResult findGoodsVarietiesByCategory() {
		Map<String, Object> params = getParamsValidationResults();
		int categoryId = (Integer) params.get("categoryId");

		int code = AjaxResultCode.OK.getCode();
		String msg = SUCCESS;
		try
		{
			Map<String, Object> dataMap = new HashMap<String, Object>();
			AjaxResult ajaxResult = new AjaxResult(code, msg);

			List<GoodsVarietyDTO> goodsVarieties = goodsInfoShowService.loadGoodsVarietiesByCategory(categoryId);
			dataMap.put("goodsVarieties", goodsVarieties);
			ajaxResult.setData(dataMap);

			return ajaxResult;
		}
		catch (IllegalArgumentException e)
		{
			logger.error("[/neworder/goods/findGoodsVarietiesByCategory].Exception:{}",e);
			return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
		}
	}

	/**
	 * 根据商品品种，获取所有产品
	 *
	 * @return
	 */
	@UriMapping(value = "/find_goods_products_by_variety" , interceptors = {"validationInterceptor"})
	public AjaxResult findGoodsProductsByVariety() {
		Map<String, Object> params = getParamsValidationResults();
		int varietyId = (Integer) params.get("varietyId");

		int code = AjaxResultCode.OK.getCode();
		String msg = SUCCESS;
		try
		{
			Map<String, Object> dataMap = new HashMap<String, Object>();
			AjaxResult ajaxResult = new AjaxResult(code, msg);

			List<GoodsProductInfoVO> goodsProductInfos = goodsInfoShowService.loadGoodsProductInfosByVariety(varietyId,false);
			dataMap.put("goodsProductInfos", goodsProductInfos);
			ajaxResult.setData(dataMap);

			return ajaxResult;
		}
		catch (IllegalArgumentException e)
		{
			logger.error("[/neworder/goods/findGoodsProductsByCategory].Exception:{}",e);
			return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
		}
	}


	/**
	 * 根据产品获取所有商品
	 *
	 * @return
	 */
	@UriMapping(value = "/find_goods_commodities_by_product" , interceptors = {"validationInterceptor"})
	public AjaxResult findGoodsCommoditiesByProduct() {
		Map<String, Object> params = getParamsValidationResults();
		int productId = (Integer) params.get("productId");

		int code = AjaxResultCode.OK.getCode();
		String msg = SUCCESS;
		try
		{
			Map<String, Object> dataMap = new HashMap<String, Object>();
			AjaxResult ajaxResult = new AjaxResult(code, msg);

			List<GoodsCommodityInfoVO> goodsCommodityInfos = goodsInfoShowService.loadGoodsCommodityInfosByProduct(productId);
			dataMap.put("goodsCommodityInfos", goodsCommodityInfos);
			ajaxResult.setData(dataMap);

			return ajaxResult;
		}
		catch (IllegalArgumentException e)
		{
			logger.error("[/neworder/goods/findGoodsCommoditiesByProduct].Exception:{}",e);
			return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
		}
	}


	/**
	 * 根据商品品种，获取所有产品，产品中包含有商品
	 *
	 * @return
	 */
	@UriMapping(value = "/find_goods_products_commodities_by_variety" , interceptors = {"validationInterceptor"})
	public AjaxResult findGoodsProductsCommoditiesByVariety() {
		Map<String, Object> params = getParamsValidationResults();
		int varietyId = (Integer) params.get("varietyId");

		int code = AjaxResultCode.OK.getCode();
		String msg = SUCCESS;
		try
		{
			Map<String, Object> dataMap = new HashMap<String, Object>();
			AjaxResult ajaxResult = new AjaxResult(code, msg);

			List<GoodsProductInfoVO> goodsProductInfos = goodsInfoShowService.loadGoodsProductInfosByVariety(varietyId,true);
			dataMap.put("goodsProductInfos", goodsProductInfos);
			ajaxResult.setData(dataMap);

			return ajaxResult;
		}
		catch (IllegalArgumentException e)
		{
			logger.error("[/neworder/goods/findGoodsProductsCommoditiesByVariety].Exception:{}",e);
			return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
		}
	}

	/**
	 * 根据商品类别获取所有商品
	 *
	 * @return
	 */
	@UriMapping(value = "/find_goods_commodities_by_category" , interceptors = {"validationInterceptor"})
	public AjaxResult findGoodsCommoditiesByCategory() {
		Map<String, Object> params = getParamsValidationResults();
		int categoryId = (Integer) params.get("categoryId");

		int code = AjaxResultCode.OK.getCode();
		String msg = SUCCESS;
		try
		{
			Map<String, Object> dataMap = new HashMap<String, Object>();
			AjaxResult ajaxResult = new AjaxResult(code, msg);

			List<GoodsCommodityInfoVO> goodsCommodityInfos = goodsInfoShowService.loadGoodsCommodityInfosByCategory(categoryId);
			dataMap.put("goodsCommodityInfos", goodsCommodityInfos);
			ajaxResult.setData(dataMap);

			return ajaxResult;
		}
		catch (IllegalArgumentException e)
		{
			logger.error("[/neworder/goods/findGoodsCommoditiesByVariety].Exception:{}",e);
			return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
		}
	}


	/**
	 * 根据商品品种获取所有商品
	 *
	 * @return
	 */
	@UriMapping(value = "/find_goods_commodities_by_variety" , interceptors = {"validationInterceptor"})
	public AjaxResult findGoodsCommoditiesByVariety() {
		Map<String, Object> params = getParamsValidationResults();
		int varietyId = (Integer) params.get("varietyId");

		int code = AjaxResultCode.OK.getCode();
		String msg = SUCCESS;
		try
		{
			Map<String, Object> dataMap = new HashMap<String, Object>();
			AjaxResult ajaxResult = new AjaxResult(code, msg);

			List<GoodsCommodityInfoVO> goodsCommodityInfos = goodsInfoShowService.loadGoodsCommodityInfosByVariety(varietyId);
			dataMap.put("goodsCommodityInfos", goodsCommodityInfos);
			ajaxResult.setData(dataMap);

			return ajaxResult;
		}
		catch (IllegalArgumentException e)
		{
			logger.error("[/neworder/goods/findGoodsCommoditiesByVariety].Exception:{}",e);
			return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
		}
	}



	/*
	 * 编辑商品的价格每天
	 */
	@UriMapping(value = "/update_commodities_price_everyday")
	public AjaxResult updateCommoditiesPriceEveryday() {
		try {
			UpdateCommodityPriceBean commodityPriceBean = super.getBodyObject(UpdateCommodityPriceBean.class);
			int userId = super.getLoginUserId();
			goodsInfoShowService.updateCommoditiesPriceEveryday(commodityPriceBean,userId);
			return new AjaxResult(AjaxResultCode.OK.getCode(), "success");
		} catch (Exception e) {
			logger.error("[/neworder/goods/update_commodities_price_every].Exception:{}", e);
			if(e instanceof IllegalArgumentException){
				return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
			}else{
				return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(), e.getMessage());
			}
		}
	}


}
