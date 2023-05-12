package org.coan.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.coan.pojo.XChainObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class XSuperChainService {
    @Value("${XSuperChain.baseUrl}")
    private String baseUrl;

    @Value("${XSuperChain.create}")
    private String creteUrl;

    @Value("${XSuperChain.publish}")
    private String publishUrl;

    @Value("${XSuperChain.query}")
    private String queryUrl;

    @Value("${XSuperChain.listastbyaddr}")
    private String listAstByAddrUrl;

    public Long createAsset(XChainObject object) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try{
            HttpResponse res = HttpRequest.post(baseUrl + creteUrl)
                    .contentType("application/x-www-form-urlencoded")
                    .body(mapper.writeValueAsBytes(object))
                    .timeout(20000)
                    .execute();
            if(res.isOk()){
                JSONObject jsonObject = JSON.parseObject(res.body());
                return (long) jsonObject.get("asset_id");
            }
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("json转换失败！");
        }
        return null;
    }

    public boolean publishAsset(XChainObject object) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try{
            HttpResponse res = HttpRequest.post(baseUrl + publishUrl)
                    .contentType("application/x-www-form-urlencoded")
                    .body(mapper.writeValueAsBytes(object))
                    .timeout(20000)
                    .execute();
            if(res.isOk()){
                JSONObject jsonObject = JSON.parseObject(res.body());
                if(jsonObject.get("errno").equals("0")){
                    return true;
                }
            }
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("json转换失败！");
        }
        return false;
    }

    public XChainAsset getAssetDetail(long id, Map<String, Object> params) {
        HttpResponse res = HttpRequest.post(baseUrl + queryUrl)
                .contentType("application/x-www-form-urlencoded")
                .form(params)
                .body("asset_id", String.valueOf(id))
                .timeout(20000)
                .execute();
        if(res.isOk()){
            JSONObject jsonObject = JSON.parseObject(res.body());
            return JSONObject.parseObject(String.valueOf(jsonObject), XChainAsset.class);
        }
        return null;
    }

    public List<XChainAsset> getAssetsList(String auth, Map<String, Object> params) {
        HttpResponse res = HttpRequest.post(baseUrl + listAstByAddrUrl)
                .bearerAuth(auth)
                .contentType("application/x-www-form-urlencoded")
                .body(params.toString())
                .timeout(20000)
                .execute();

        if(res.isOk()) {
            JSONObject jsonObject = JSON.parseObject(res.body());
            return JSONArray.parseArray(jsonObject.get("list").toString(), XChainAsset.class);
        }
        return null;
    }
}
