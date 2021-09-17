package main

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
)

// Weather API Key
// DO NOT COMMIT WITH THIS STILL FILLED OUT
var weatherKey = "CONFIDENTIAL"

// -------- Structs for Unmarshalling / Decoding API Responses --------
// Documentation: https://openweathermap.org/forecast16

// Top Level Structs
// -- 16 (or n day) Forecast
type Forecast struct {
	City  CityStruct  `json:"city"`
	Dates []DayStruct `json:"list"`
}

// -- 5 Day 3 hour Forecast
type FiveDayWeather struct {
	City  CityStruct        `json:"city"`
	Dates []ThreeHourStruct `json:"list"`
}

// -- Current Weather Forecast
type CurrentWeather struct {
	Weather []WeatherStruct `json:"weather"`
	Main    MainStruct      `json:"main"`
	Wind    WindStruct      `json:"wind"`
	Clouds  CloudStruct     `json:"clouds"`
}

// This contains the info about the latitude & longitude of location being requested
type CityStruct struct {
	Id   int    `json:"id"`
	Name string `json:"name"`
}

// 5 Day - 3 Hour struct
type ThreeHourStruct struct {
	DateTime     int         `json:"dt"`
	Main         MainStruct  `json:"main"`
	Clouds       CloudStruct `json:"clouds"`
	Wind         WindStruct  `json:"wind"`
	PrecipChance int         `json:"pop"`
}

// Cloud Struct for 5-Day 3-Hour
type CloudStruct struct {
	CloudPercent int `json:"all"`
}

// Wind Struct for 5-Day 3-Hour
type WindStruct struct {
	WindSpeed     float64 `json:"speed"`
	WindDirection float64 `json:"deg"`
}

// This contains all the data regarding each 3-hour forecast
type MainStruct struct {
	Temperature int `json:"temp"`
	FeelsLike   int `json:"feels_like"`
	Pressure    int `json:"pressure"`
	Humidity    int `json:"humidity"`
}

// This contains all the data regarding each day's forecast
type DayStruct struct {
	DateTime      int               `json:"dt"`
	Sunrise       int               `json:"sunrise"`
	Sunset        int               `json:"sunset"`
	Temperature   TemperatureStruct `json:"temp"`
	FeelsLike     feelsStruct       `json:"feels_like"`
	Humidity      int               `json:"humidity"`
	Weather       []WeatherStruct   `json:"weather"`
	WindSpeed     float64           `json:"speed"`
	WindDirection float64           `json:"deg"`
	CloudCover    float64           `json:"clouds"`
	PrecipChance  float64           `json:"pop"`
}

// This contains basic string info about the weather conditions
type WeatherStruct struct {
	Title       string `json:"main"`
	Description string `json:"description"`
}

// Info about the temperature
type TemperatureStruct struct {
	Low  float64 `json:"min"`
	High float64 `json:"max"`
	Day  float64 `json:"day"`
}

// Info about what it "feels like"
type feelsStruct struct {
	FeelsDay float64 `json:"day"`
}

// -------- Functions --------
// Note: Internal use lowercase, External use Capital

// Function: handleJSON
// Input: query string
// Return: A ToDoStruct that contains data (depends on calling function)
// Description: gets information from the Weather API

func handleJSON(query string) []byte {
	response, respErr := http.Get(query)
	if respErr != nil {
		panic(respErr)
	}
	data, dataErr := ioutil.ReadAll(response.Body)
	if dataErr != nil {
		panic(dataErr)
	}
	return data
}

// Funtion: GetForecastDays
// Input: number of days, latitude and logitude of location
// Return: A Forecast Struct
// Description: gets the forecast for a given number of days at a given location

func GetForecastDays(nDays int, lat string, long string) Forecast {
	query := fmt.Sprintf("https://api.openweathermap.org/data/2.5/forecast/daily?lat=%s&lon=%s&cnt=%d&appid=%s", lat, long, nDays, weatherKey)
	data := handleJSON(query)

	var f Forecast
	jsonErr := json.Unmarshal(data, &f)
	if jsonErr != nil {
		panic(jsonErr)
	}
	return f
}

// Funtion: Get5DayForecast
// Input: latitude and logitude of location
// Return: A FiveDayWeather struct
// Description: gets the weather forecast for the next 5 days at a given location

func Get5DayForecast(lat string, long string) FiveDayWeather {
	query := fmt.Sprintf("http://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&appid=%s", lat, long, weatherKey)
	data := handleJSON(query)

	var f FiveDayWeather
	jsonErr := json.Unmarshal([]byte(data), &f)
	if jsonErr != nil {
		panic(jsonErr)
	}
	return f
}

// Funtion: GetCurrentWeather
// Input: latitude and logitude of location
// Return: A CurrentWeather struct
// Description: gets the current weather at a given location

func GetCurrentWeather(lat string, long string) CurrentWeather {
	query := fmt.Sprintf("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s", lat, long, weatherKey)
	data := handleJSON(query)

	var f CurrentWeather
	jsonErr := json.Unmarshal([]byte(data), &f)
	if jsonErr != nil {
		panic(jsonErr)
	}
	return f
}
