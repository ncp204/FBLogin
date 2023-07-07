package com.example.jsp.controller;

import com.example.jsp.model.AccessTokenResponse;
import com.example.jsp.repository.UserRepository;
import com.example.jsp.service.FacebookApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {
    @Value("${spring.security.oauth2.client.registration.facebook.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.facebook.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.facebook.redirect-uri}")
    private String redirectUri;

    @Autowired
    private UserRepository userRepository;

    private FacebookApiService facebookApiService;

    @GetMapping("/home")
    public String home() {
        return "index";
    }

    @GetMapping("/login-page")
    public String login() {
        return "login-test";
    }

    @GetMapping("/oauth2/authorization/facebook/v2")
    public String handleFacebookCallback(@RequestParam("code") String code, RedirectAttributes attributes) {
        System.out.println("runme");
        List<String> myList = new ArrayList<>();
        myList.add("ajb");
        myList.add("dbf");
//        code = "AQBFvewxlVldjlpZT63DfrtwmTVaAOUKcIHGJbvVq6aVQznEKBuhu" +
//                "mB8JY1bumtVarPkfOr_qdagNZguxz8m7kpBTAkLn_XwiFVhVk82Y7FQM" +
//                "asNf0v4j8YjH4bXEoazhQUkMNX95MBh6FYpGmjOyS4UbZPoWJnKzbb5rbmo" +
//                "XGipePl_cTglq5HJrc3ojHBZaVTPqBv2Kt33UsFfO5QH6iAOSnBecO7ZxsV0Vws" +
//                "9NWFSaibVvunEk8QwbxSzYnQ1Yr6pCNavbMlu3yiInIP-_5z50MFM-XasfZjymWIsUd" +
//                "AS1OWXC_9RAZCZd7faSE12qf82dceuGmOs0G2dRce6jiD_RtBFoM-cunRAGBIWKj60MdEour" +
//                "WVB8H2dTkWuHBk_K0&state=wMHSFGXQVmhyMm7_njd_aiD_diu2aHDglIBoetyT4WY%3D";
        String code2 = "AQAd0UdDae3uHnX8Wd40ijeGDuwzf9GwJ6GqHiQuamp4W_tRI8K99_4AX10J4Llb5YsCYlChTyQOelgWhlRns-wJs1kKSoTu78eNnlsVz2-G4bEgdTUgPUGG4sRacjkjeT_Dmbb9J-PI2RZOxntny8jzdHEDiWyaxQHDiAHPloa9hWqc4RkGao3JGX6m-AjTTtrKjXhx2Y5QHsZcfdj2YsMqsjdvyuHh3_tee8RmC76i3odtTdgFoG87DzypVX-GLyukciO9-wqVC7i3hJbiplVJBenEjjij94Hdi0cjC_E48MlxnnIwkSMWR3pcbf8f9-jLC0JDClxl2teOZnf8LeBuEE_P8k4DBjD9wW24tm7rrbgTe9uwptcvvuVPrrDi9yei8Lb1GtHKuCOqXghsGKKi84Cz6OBga8SN1alxT9larg";
//        // 1. Lưu mã code
        String authorizationCode = code;

        // 2. Gửi yêu cầu POST để trao đổi mã code và lấy token truy cập từ Facebook
        String accessToken = exchangeAuthorizationCodeForAccessToken(authorizationCode);

        facebookApiService = new FacebookApiService();
        String email = facebookApiService.getUserEmailFromFacebook(accessToken);
        String name = facebookApiService.getUserNameFromFacebook(accessToken);
        // Chuyển hướng người dùng đến trang /home
        attributes.addFlashAttribute("name", name);
        return "home";
    }

    private String exchangeAuthorizationCodeForAccessToken(String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();

        // Định dạng URL endpoint để gửi yêu cầu trao đổi mã code
        String tokenEndpoint = "https://graph.facebook.com/v16.0/oauth/access_token";

        // Thiết lập các thông tin yêu cầu
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("code", authorizationCode);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Gửi yêu cầu POST và nhận phản hồi
        ResponseEntity<AccessTokenResponse> responseEntity = restTemplate.exchange(
                tokenEndpoint,
                HttpMethod.POST,
                requestEntity,
                AccessTokenResponse.class
        );

        // Kiểm tra phản hồi
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            AccessTokenResponse accessTokenResponse = responseEntity.getBody();
            if (accessTokenResponse != null) {
                return accessTokenResponse.getAccessToken();
            }
        }
        return null;
    }
}
