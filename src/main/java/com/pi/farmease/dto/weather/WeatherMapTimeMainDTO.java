package com.pi.farmease.dto.weather;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WeatherMapTimeMainDTO {

	private BigDecimal temp;

	private BigDecimal temp_min;

	private BigDecimal temp_max;

	private BigDecimal pressure;

	private BigDecimal sea_level;

	private BigDecimal grnd_level;

	private BigDecimal humidity;

	private BigDecimal temp_kf;


}
