package com.fruit.sys.admin.action.user.address;


import com.fruit.account.biz.dto.UserDeliveryAddressDTO;
import com.fruit.newOrder.biz.dto.OrderNewInfoDTO;
import com.fruit.newOrder.biz.service.OrderNewInfoService;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.UserModel;
import com.fruit.sys.admin.model.portal.AjaxResult;
import com.fruit.sys.admin.model.portal.AjaxResultCode;
import com.fruit.sys.admin.model.user.account.AddressVo;
import com.fruit.sys.admin.model.user.account.UserDeliveryAddressRequest;
import com.fruit.sys.admin.service.user.UserListService;
import com.fruit.sys.admin.service.user.account.DeliveryAddressService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户收件地址相关
 * <p/>
 * Create Author  : paul
 * Create  Time   : 2017-05-18
 * Project        : portal
 */
@Component
@UriMapping("/user/delivery_address")
public class UserDeliveryAddressAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDeliveryAddressAction.class);

    @Autowired
    private DeliveryAddressService deliveryAddressService;

    @Autowired
    private OrderNewInfoService orderNewInfoService;

    @Autowired
    private UserListService userListService;

    @UriMapping(value = "/add_user_receive_address_ajax", interceptors = {"validationInterceptor"})
    public AjaxResult addAddress()
    {
        int sysId = getLoginUserId();
        Map<String, Object> params = getParamsValidationResults();
        String orderNo = (String) params.get("orderNo");
        String receiver = (String) params.get("receiver");
        int countryId = (Integer) params.get("countryId");
        int provinceId = (Integer) params.get("provinceId");
        int cityId = (Integer) params.get("cityId");
        int districtId = (Integer) params.get("districtId");
        String address = (String) params.get("address");
        String zipCode = (String) params.get("zipCode");
        String cellPhone = (String) params.get("cellPhone");
        String phoneNum = (String) params.get("phoneNum");
        int selected = (Integer) params.get("selected");

        try
        {
//            OrderNewInfoDTO orderNewInfoDTO = orderNewInfoService.loadByOrderNo(orderNo);
//            Validate.notNull(orderNewInfoDTO,"订单不存在");
//            final int userId = orderNewInfoDTO.getUserId();
//
//            UserModel userModel = userListService.loadUserModel(userId, false);
//            Validate.isTrue(null != userModel);

            UserModel userModel = getUserModelByOrderNo(orderNo);

            UserDeliveryAddressRequest userDeliveryAddressRequest = new UserDeliveryAddressRequest();

            userDeliveryAddressRequest.setUserId(userModel.getUserId());
            userDeliveryAddressRequest.setReceiver(receiver);
            userDeliveryAddressRequest.setProvinceId(provinceId);
            userDeliveryAddressRequest.setCountryId(countryId);
            userDeliveryAddressRequest.setCityId(cityId);
            userDeliveryAddressRequest.setDistrictId(districtId);
            userDeliveryAddressRequest.setAddress(address);
            userDeliveryAddressRequest.setZipCode(zipCode);
            userDeliveryAddressRequest.setCellPhone(cellPhone);
            userDeliveryAddressRequest.setPhoneNum(phoneNum);
            userDeliveryAddressRequest.setSelected(selected);

            deliveryAddressService.addAddress(sysId,userModel,userDeliveryAddressRequest);
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/user/delivery_address/add_user_receive_address_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }



    @UriMapping(value = "/update_user_receive_address_ajax", interceptors = { "validationInterceptor"})
    public AjaxResult updateAddress()
    {
        Map<String, Object> params = getParamsValidationResults();
        int id = (Integer) params.get("id");
        String orderNo = (String) params.get("orderNo");
        String receiver = (String) params.get("receiver");
        int countryId = (Integer) params.get("countryId");
        int provinceId = (Integer) params.get("provinceId");
        int cityId = (Integer) params.get("cityId");
        int districtId = (Integer) params.get("districtId");
        String address = (String) params.get("address");
        String zipCode = (String) params.get("zipCode");
        String cellPhone = (String) params.get("cellPhone");
        String phoneNum = (String) params.get("phoneNum");
        int selected = ((Integer) params.get("selected"));

        try
        {
            UserModel userModel = getUserModelByOrderNo(orderNo);

            UserDeliveryAddressDTO addressDto = deliveryAddressService.loadAddressById(id);
            if (null != addressDto)
            {
                addressDto.setReceiver(receiver);
                addressDto.setCountryId(countryId);
                addressDto.setProvinceId(provinceId);
                addressDto.setCityId(cityId);
                addressDto.setDistrictId(districtId);
                addressDto.setAddress(address);
                addressDto.setZipCode(zipCode);
                addressDto.setCellPhone(cellPhone);
                addressDto.setPhoneNum(phoneNum);
                addressDto.setSelected(selected);
                deliveryAddressService.updateAddress(userModel,addressDto);
            }
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/user/delivery_address/update_user_receive_address_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }



    /**
     * 查询会员收件地址信息
     * @return
     */
    @UriMapping(value = "/get_user_receive_address_ajax" , interceptors = { "validationInterceptor"})
    public AjaxResult getUserReceiveAddress()
    {
        Map<String, Object> params = getParamsValidationResults();
        String orderNo = (String) params.get("orderNo");

        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        try
        {
            UserModel userModel = getUserModelByOrderNo(orderNo);

            List<UserDeliveryAddressDTO> deliveryAddressDTOs = deliveryAddressService.loadAllAddress(userModel.getUserId());

            List<AddressVo> addressVoList = this.deliveryAddressService.wrapVOs(deliveryAddressDTOs);

            Map<String, Object> dataMap = new HashMap<String, Object>();

            AjaxResult ajaxResult = new AjaxResult(code, msg);

            dataMap.put("receiveAddress", addressVoList);
            ajaxResult.setData(dataMap);

            return ajaxResult;

        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/user/delivery_address/get_user_receive_address_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    /**
     * 根据订单NO获取userModel
     * @param orderNo
     * @return
     */
    private UserModel getUserModelByOrderNo(String orderNo){
        OrderNewInfoDTO orderNewInfoDTO = orderNewInfoService.loadByOrderNo(orderNo);
        Validate.notNull(orderNewInfoDTO,"订单不存在");
        final int userId = orderNewInfoDTO.getUserId();

        UserModel userModel = userListService.loadUserModel(userId, false);
        Validate.isTrue(null != userModel);

        return userModel;
    }




}
