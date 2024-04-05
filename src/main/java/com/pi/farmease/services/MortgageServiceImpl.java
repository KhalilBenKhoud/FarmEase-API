package com.pi.farmease.services;

import com.pi.farmease.dao.MortgageRepository;
import com.pi.farmease.entities.Mortgage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MortgageServiceImpl implements MortgageService {

    @Autowired
    private final UserService userService;
    private final MortgageRepository mortgageRepository;
    private final String clientId = "78qih6a4job42o";
    private final String clientSecret = "3tjxxbisUnpxCiUx";
    private static final String PERSON_URN = "78qih6a4job42o";
    private static final String API_URL = "https://api.linkedin.com/v2/ugcPosts";
    private final String redirectUri = "https://www.linkedin.com/developers/tools/oauth/redirect";
    private final String accessToken = "AQU0dbEyWUjLQIZfg9UNr_ltDrCgh5cPElO4MuWEJ_ZUqMhvZtDubv1jomby78BySmRn3i-QyTwLVIiPMuh3AhDoT1KowEPzsvFNW372Ls8ecCK8ACa8kCmnUENfpzE6x_ooDshDa6mjOjRqrpl9ebCEAnqANZZUTWyq3h3iWCuq4vpkPLTy0VjM1nS75Ium9KaNRyMBiHunqjCYiZVD7pNAHf-2YGSqqj9aNZIkhCpsqWIFXfqQ7PMku40o1PaNX827PyRJr6PfSRXQewg8Sbdwc9gkoOnOPrtsK4C4gnF-CZ6rpePjXKCYscFhxLkksmZhJQd_9kQDQgERd-m9LzfGfnusMA";

    @Override
    public void addMortgage(Mortgage requestBody) {
        // Get the values from the request body
        long durationMortgage = requestBody.getDuration_mortgage();
        double priceMortgage = requestBody.getPrice_mortgage();

        // Calculate the month_payment
        double monthPayment = priceMortgage / (durationMortgage * 12);

        // Apply conditions based on duration_mortgage
        if (durationMortgage == 1) {
            // Increase priceMortgage by 8% if durationMortgage is 1
            priceMortgage += priceMortgage * 0.08;
        } else if (durationMortgage == 2) {
            // Increase priceMortgage by 13% if durationMortgage is 2
            priceMortgage += priceMortgage * 0.13;
        } else if (durationMortgage == 3) {
            // Increase priceMortgage by 18% if durationMortgage is 3
            priceMortgage += priceMortgage * 0.18;
        }

        // Build the Mortgage object
        Mortgage mortgage = Mortgage.builder()
                .description_mortgage(requestBody.getDescription_mortgage())
                .duration_mortgage(durationMortgage)
                .prize_mortgage(priceMortgage)
                .month_payment(monthPayment)
                .category_mortgage(requestBody.getCategory_mortgage())
                .type_mortgage(requestBody.getType_mortgage())
                .price_mortgage(requestBody.getPrice_mortgage())
                .applications(requestBody.getApplications())
                .build();

        // Save the mortgage in the database
        mortgageRepository.save(mortgage);


        postOnLinkedIn("test");
    }
    @Override
    public void postOnLinkedIn(String shareCommentary) {shareCommentary="test";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("X-Restli-Protocol-Version", "2.0.0");

        // Create request body
        String requestBody = "{\n" +
                "    \"author\": \"" + PERSON_URN + "\",\n" +
                "    \"lifecycleState\": \"PUBLISHED\",\n" +
                "    \"specificContent\": {\n" +
                "        \"com.linkedin.ugc.ShareContent\": {\n" +
                "            \"shareCommentary\": {\n" +
                "                \"text\": \"" + shareCommentary + "\"\n" +
                "            },\n" +
                "            \"shareMediaCategory\": \"NONE\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"visibility\": {\n" +
                "        \"com.linkedin.ugc.MemberNetworkVisibility\": \"PUBLIC\"\n" +
                "    }\n" +
                "}";

        // Send the POST request
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        String response = restTemplate.postForObject(API_URL, entity, String.class);

        // Process the response
        System.out.println("Response: " + response);
    }



    /*private void postOnLinkedIn(String postContent) {
        String accessToken = "AQU0dbEyWUjLQIZfg9UNr_ltDrCgh5cPElO4MuWEJ_ZUqMhvZtDubv1jomby78BySmRn3i-QyTwLVIiPMuh3AhDoT1KowEPzsvFNW372Ls8ecCK8ACa8kCmnUENfpzE6x_ooDshDa6mjOjRqrpl9ebCEAnqANZZUTWyq3h3iWCuq4vpkPLTy0VjM1nS75Ium9KaNRyMBiHunqjCYiZVD7pNAHf-2YGSqqj9aNZIkhCpsqWIFXfqQ7PMku40o1PaNX827PyRJr6PfSRXQewg8Sbdwc9gkoOnOPrtsK4C4gnF-CZ6rpePjXKCYscFhxLkksmZhJQd_9kQDQgERd-m9LzfGfnusMA" ; // Replace with your actual LinkedIn access token
        LinkedIn linkedIn = new LinkedInTemplate(accessToken);
postContent="post";
        // Post the share on LinkedIn
        linkedIn.networkUpdateOperations().createNetworkUpdate(postContent);
    }*/
  /*  private void postOnLinkedIn(String postContent) {
        // Replace with your actual LinkedIn access token
        String accessToken = "AQU0dbEyWUjLQIZfg9UNr_ltDrCgh5cPElO4MuWEJ_ZUqMhvZtDubv1jomby78BySmRn3i-QyTwLVIiPMuh3AhDoT1KowEPzsvFNW372Ls8ecCK8ACa8kCmnUENfpzE6x_ooDshDa6mjOjRqrpl9ebCEAnqANZZUTWyq3h3iWCuq4vpkPLTy0VjM1nS75Ium9KaNRyMBiHunqjCYiZVD7pNAHf-2YGSqqj9aNZIkhCpsqWIFXfqQ7PMku40o1PaNX827PyRJr6PfSRXQewg8Sbdwc9gkoOnOPrtsK4C4gnF-CZ6rpePjXKCYscFhxLkksmZhJQd_9kQDQgERd-m9LzfGfnusMA";

        // Create LinkedInTemplate with the access token
        LinkedIn linkedIn = new LinkedInTemplate(accessToken);

        // Post the share on LinkedIn
        linkedIn.networkUpdateOperations().createNetworkUpdate(postContent);
    }*/
/* <dependency>
            <groupId>org.springframework.social</groupId>
            <artifactId>spring-social-linkedin</artifactId>
            <version>1.0.0.RELEASE</version> <!-- Or the latest version available -->
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-oauth2-client</artifactId>
        </dependency>*/

    @Override
    public List<Mortgage> getAllMortgages() {
        return mortgageRepository.findAll();
    }

    @Override
    public Optional<Mortgage> getMortgageById(Long id) {
        return mortgageRepository.findById(id);
    }


    @Override
    public Mortgage updateMortgage(Long id, Mortgage mortgageDetails) {
        Optional<Mortgage> mortgageOptional = mortgageRepository.findById(id);
        if (mortgageOptional.isPresent()) {
            Mortgage existingMortgage = mortgageOptional.get();
            existingMortgage.setDescription_mortgage(mortgageDetails.getDescription_mortgage());
            existingMortgage.setDuration_mortgage(mortgageDetails.getDuration_mortgage());
            existingMortgage.setPrize_mortgage(mortgageDetails.getPrize_mortgage());
            existingMortgage.setMonth_payment(mortgageDetails.getMonth_payment());
            existingMortgage.setCategory_mortgage(mortgageDetails.getCategory_mortgage());
            existingMortgage.setType_mortgage(mortgageDetails.getType_mortgage());
            existingMortgage.setPrice_mortgage(mortgageDetails.getPrice_mortgage());
            return mortgageRepository.save(existingMortgage);
        } else {
            return null; // Gérer le cas où l'identifiant n'est pas trouvé
        }
    }

    @Override
    public void deleteMortgage(Long id) {
        mortgageRepository.deleteById(id);
    }

 /*   public void sharePostOnLinkedIn(String accessToken, String linkedinUserId, String postContent) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("author", "urn:li:person:" + linkedinUserId);
        requestBody.put("lifecycleState", "PUBLISHED");

        Map<String, Object> specificContent = new HashMap<>();
        Map<String, Object> shareContent = new HashMap<>();
        shareContent.put("shareCommentary", Map.of("text", postContent));
        shareContent.put("shareMediaCategory", "NONE");
        specificContent.put("com.linkedin.ugc.ShareContent", shareContent);

        requestBody.put("specificContent", specificContent);

        Map<String, String> visibility = new HashMap<>();
        visibility.put("com.linkedin.ugc.MemberNetworkVisibility", "CONNECTIONS");
        requestBody.put("visibility", visibility);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity("https://api.linkedin.com/v2/ugcPosts", request, Map.class);
        System.out.println("Post shared successfully: " + response.getBody());
    }*/
}