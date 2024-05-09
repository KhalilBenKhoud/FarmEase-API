package com.pi.farmease.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;




    @Service
    public class LinkedInServiceImpl implements LinkedInService {
       /* private final String token = "Bearer " + System.getProperty("bearerToken");
        private final String endPointForLinkedInUserProfile = "https://api.linkedin.com/v2/me";

        @Override
        public JSONObject submitLinkedInPost(String text) throws JSONException {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", token);

            JSONObject textShare = null;
            String id = getLinkedInProfile().getString("id");
            textShare = createLinkedInHttpHeaderForPost(id, text);

            HttpEntity<?> textShareHttpEntity = new HttpEntity<>(textShare.toString(), httpHeaders);

            ResponseEntity<String> responseEntity = restTemplate.exchange(endPointForLinkedInUserProfile, HttpMethod.POST, textShareHttpEntity, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return new JSONObject(responseEntity.getBody());
            } else {
                return null;
            }
        }

        private JSONObject createLinkedInHttpHeaderForPost(String id, String text) throws JSONException {
            JSONObject shareCommentary = new JSONObject();
            shareCommentary.put("text", text);

            JSONObject shareContent = new JSONObject();
            shareContent.put("shareCommentary", shareCommentary);
            shareContent.put("shareMediaCategory", "NONE");

            JSONObject specificContent = new JSONObject();
            specificContent.put("com.linkedin.ugc.ShareContent", shareContent);

            JSONObject visibility = new JSONObject();
            visibility.put("com.linkedin.ugc.MemberNetworkVisibility", "PUBLIC");

            JSONObject textShare = new JSONObject();
            textShare.put("author", "urn:li:person:" + id);
            textShare.put("lifecycleState", "PUBLISHED");
            textShare.put("specificContent", specificContent);
            textShare.put("visibility", visibility);

            return textShare;
        }

        @Override
        public JSONObject getLinkedInProfile() throws JSONException {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", token);

            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

            ResponseEntity<String> response = restTemplate.exchange(endPointForLinkedInUserProfile, HttpMethod.GET, entity, String.class);

            JSONObject jsonObjectGetProfile = new JSONObject(response.getBody());

            return jsonObjectGetProfile;
        }

    }

    private JSONObject createLinkedInHttpHeaderForPost(String id, String text) throws JSONException {

        JSONObject shareCommentary = new JSONObject();
        shareCommentary.put("text", text);

        JSONObject shareContent = new JSONObject();
        shareContent.put("shareCommentary", shareCommentary);
        shareContent.put("shareMediaCategory", "NONE");

        JSONObject specificContent = new JSONObject();
        specificContent.put("com.linkedin.ugc.ShareContent", shareContent);

        JSONObject visibility = new JSONObject();
        visibility.put("com.linkedin.ugc.MemberNetworkVisibility", "PUBLIC");

        JSONObject textShare = new JSONObject();
        textShare.put("author", "urn:li:person:" + id);
        textShare.put("lifecycleState", "PUBLISHED");
        textShare.put("specificContent", specificContent);
        textShare.put("visibility", visibility);

        return textShare;
    }

    public JSONObject getLinkedInProfile() throws JSONException {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(endPointForLinkedInUserProfile, HttpMethod.GET, entity, String.class);

        JSONObject jsonObjectGetProfile = new JSONObject(response.getBody());

        return jsonObjectGetProfile;
    }*/
}
