package com.example.demo.resource;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Ip;
import com.example.demo.model.TopStories;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@RestController
@RequestMapping("/consume")
public class RestPoint1 {

	@GetMapping("/ip")
	public String resttemplate() {
		
		SimpleClientHttpRequestFactory clientHttpReq = new SimpleClientHttpRequestFactory();
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.tcs.com", 8080));
		clientHttpReq.setProxy(proxy);
		 
		RestTemplate rt = new RestTemplate(clientHttpReq);
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(headers);
		String url="http://ip.jsontest.com/";
		ResponseEntity<Ip> response = rt.exchange(url, HttpMethod.GET, request, Ip.class);
		
		Ip r=rt.getForObject(url, Ip.class);
		
		return r.getIp();	
	}
	
	@GetMapping("/topstories")
	public Object getTopStories() throws JsonParseException, JsonMappingException, IOException {
		
		SimpleClientHttpRequestFactory clientHttpReq = new SimpleClientHttpRequestFactory();
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.tcs.com", 8080));
		clientHttpReq.setProxy(proxy);
		 
		RestTemplate rt = new RestTemplate(clientHttpReq);
		HttpHeaders headers=new HttpHeaders();
		headers.add("apikey", "c6ee6b49dab4478cbec00efa79149879");
		//headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(headers);
		String url="https://api.nytimes.com/svc/topstories/v2/home.json";
		
		
		ResponseEntity<Object> response = rt.exchange(url, HttpMethod.GET, request, Object.class);
		
		ObjectMapper om=new ObjectMapper();
		//om.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(response.getBody());
		

		
		JsonNode jsonnode=om.readTree(json);
		
		JsonNode results=jsonnode.path("results");
		
		List<TopStories> list=new ArrayList<>();
		
		if(results.isArray()) {
			for(JsonNode result:results) {
				TopStories ts = new TopStories();
				ts.setSection(result.path("section").asText());
				ts.setTitle(result.path("title").asText());
				list.add(ts);
			}
		}
		
		
		return list;
		
	}
	
	
	@GetMapping("/courses")
	public String basicSecurity() {
		System.out.println("enter");
		/*SimpleClientHttpRequestFactory clientHttpReq = new SimpleClientHttpRequestFactory();
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.tcs.com", 8080));
		clientHttpReq.setProxy(proxy);*/
		 
		RestTemplate rt = new RestTemplate();
		String url="http://localhost:9000/api/categories/courses";
		
		String notEncoded = "Basic username:password";
	    String encodedAuth = Base64.getEncoder().encodeToString(notEncoded.getBytes());
	    System.out.println("code: "+encodedAuth);
	    
	    HttpHeaders headers=new HttpHeaders();
	    headers.add("Authorization", "Basic dXNlcm5hbWU6cGFzc3dvcmQ=");
	    
	    HttpEntity<String> request = new HttpEntity<String>(headers);
	    
	    ResponseEntity<String> response = rt.exchange(url, HttpMethod.GET, request, String.class);
		
		
		return response.getBody();
	}
}
