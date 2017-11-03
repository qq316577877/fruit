package com.fruit.sys.admin.model.neworder;



import com.fruit.newOrder.biz.dto.GoodsCommodityPriceDaysDTO;

import java.util.List;

public class UpdateCommodityPriceBean {

	private List<GoodsCommodityPriceDaysDTO> goodsCommodityPriceDaysDTOs;

	private String quotationTime;

	public List<GoodsCommodityPriceDaysDTO> getGoodsCommodityPriceDaysDTOs() {
		return goodsCommodityPriceDaysDTOs;
	}

	public void setGoodsCommodityPriceDaysDTOs(List<GoodsCommodityPriceDaysDTO> goodsCommodityPriceDaysDTOs) {
		this.goodsCommodityPriceDaysDTOs = goodsCommodityPriceDaysDTOs;
	}

	public String getQuotationTime() {
		return quotationTime;
	}

	public void setQuotationTime(String quotationTime) {
		this.quotationTime = quotationTime;
	}

	private static final long serialVersionUID = 1L;

}
