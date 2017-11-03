package com.fruit.sys.admin.action;

import com.fruit.sys.admin.model.JsonResult;
import com.fruit.sys.admin.source.DictionarySource;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Component
@UriMapping("/dic")
public class DicAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DicAction.class);

    private static final String CLAZZ_TYPE = DicAction.class.getSimpleName();

    @ResponseBody
    @UriMapping(value = "/list")
    public JsonResult delete()
    {
        JsonResult<Map<String, String>> dto = new JsonResult<Map<String, String>>();
        try
        {
            String key = getRequiredStringParamter("key");
            Map<String, String> map = DictionarySource.getDic(key);
            dto.setT(map);
            dto.setResult(true);
            dto.setMsg("成功!");
        }
        catch (Exception e)
        {
            LOGGER.error("delete error", e);
            dto.setResult(false);
            dto.setMsg("失败:" + e.getMessage());
        }
        return dto;
    }
}


