package com.github.trungee.example.currencyconversion.springbootmicroservicecurrencyconversion.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.github.trungee.example.currencyconversion.springbootmicroservicecurrencyconversion.model.CurrencyConversionBean;
import com.github.trungee.example.currencyconversion.springbootmicroservicecurrencyconversion.proxy.CurrencyExchangeServiceProxy;

@RestController
@RequestMapping("/rest")
public class CurrencyConversionController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CurrencyExchangeServiceProxy proxy;
	
	@PreAuthorize("hasAuthority('ADMIN_USER') or hasAuthority('STANDARD_USER')")
	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", 
				CurrencyConversionBean.class, uriVariables);
		CurrencyConversionBean responseBean = responseEntity.getBody();
		responseBean.setQuantity(quantity);
		responseBean.setTotalCalculateAmount(quantity.multiply(responseBean.getConversionMultiple())); 
		return responseBean;
	}
	
	@GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	@PreAuthorize("hasAuthority('ADMIN_USER')")
	public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
		CurrencyConversionBean response = proxy.retrieveExchangeValue(from, to);
		logger.info("{}", response);
		response.setQuantity(quantity);
		response.setTotalCalculateAmount(quantity.multiply(response.getConversionMultiple()));
		return response;
	}
	
	@GetMapping("/exchanges")
	public List<CurrencyConversionBean> getAll() {
		return proxy.retriveAll();
	}
}
