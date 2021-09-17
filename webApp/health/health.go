package main

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
)

// Edamam API Key
// DO NOT COMMIT WITH THIS STILL FILLED OUT
var edamamKey = "CONFIDENTIAL"
var edamamID = "CONFIDENTIAL"

// -------- Structs for Unmarshalling / Decoding API Responses --------
// Documentation: https://developer.edamam.com/edamam-docs-recipe-api

type RequestStruct struct {
	Hits []HitStruct `json:"hits"`
}

type HitStruct struct {
	Recipe RecipeStruct `json:"recipe"`
}

// Food recipe recommendation function
type RecipeStruct struct {
	URI          string             `json:"uri"`
	Title        string             `json:"label"`
	Image        string             `json:"image"`
	Calories     float64            `json:"yield"`
	Ingredients  []IngredientStruct `json:"ingredients"`
	DietLabels   []string           `json:"dietLabels"`
	HealthLabels []string           `json:"healthLabels"`
}

// Ingreadients called in the food recipe struct
type IngredientStruct struct {
	Quantity float64    `json:"quantity"`
	Weight   float64    `json:"weight"`
	Food     FoodStruct `json:"food"`
}

// Label for a specific food item returned by the API
type FoodStruct struct {
	Label string `json:"label"`
}

// -------- Functions --------
// Note: Internal use lowercase, External use Capital

// Function: handleJSON
// Input: query string
// Return: A RequestStruct
// Description: gets information from the Edamam API

func handleJSON(query string) RequestStruct {
	response, respErr := http.Get(query)
	if respErr != nil {
		panic(respErr)
	}
	data, dataErr := ioutil.ReadAll(response.Body)
	if dataErr != nil {
		panic(dataErr)
	}

	var f RequestStruct
	jsonErr := json.Unmarshal([]byte(data), &f)
	if jsonErr != nil {
		panic(jsonErr)
	}

	return f
}

// Function: GetRecipes
// Input: query string
// Return: A RequestStruct
// Description: gets recipes from the Edamam API

func GetRecipes(q string) RequestStruct {
	query := fmt.Sprintf("https://api.edamam.com/search?app_key=%s&app_id=%s&q=%s", edamamKey, edamamID, q)
	return handleJSON(query)
}
