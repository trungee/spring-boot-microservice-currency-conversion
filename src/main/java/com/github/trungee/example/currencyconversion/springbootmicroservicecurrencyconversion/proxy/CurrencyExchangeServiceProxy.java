package com.github.trungee.example.currencyconversion.springbootmicroservicecurrencyconversion.proxy;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.github.trungee.example.currencyconversion.springbootmicroservicecurrencyconversion.model.CurrencyConversionBean;

@FeignClient(name="forex-service")
@RibbonClient(name="forex-service")
public interface CurrencyExchangeServiceProxy {
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyConversionBean retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to);

	@GetMapping("exchanges")
	public List<CurrencyConversionBean> retriveAll();

}
