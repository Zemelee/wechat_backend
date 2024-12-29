package org.example.controller;


import cn.hutool.crypto.SecureUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import java.util.Random;

@RestController
public class TranslationController {
    private static class BaiduContent {
        // 百度翻译 api
        static final String APPID = "";
        static final String SECRET = "";
    }

    @GetMapping("/translate")
    public Object translate(@RequestParam String origin) {
        Random random = new Random(10);
        String salt = Integer.toString(random.nextInt());
        String appid = BaiduContent.APPID + origin + salt + BaiduContent.SECRET; //MD5加密
        String sign = SecureUtil.md5(appid);
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();//封装请求参数
        paramMap.add("q", origin);
        paramMap.add("from", "auto");
        paramMap.add("to", "zh");
        paramMap.add("appid", BaiduContent.APPID);
        paramMap.add("salt", salt);
        paramMap.add("sign", sign);
        String url = "http://api.fanyi.baidu.com/api/trans/vip/translate";
        HttpHeaders headers = new HttpHeaders();//封装请求头
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(paramMap, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> response = restTemplate.postForEntity(url, httpEntity, Object.class);
        return response.getBody();
    }

}