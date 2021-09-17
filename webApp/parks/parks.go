package parks

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
)

// National Parks API Key
// DO NOT COMMIT WITH THIS STILL FILLED OUT
var parkKey = "CONFIDENTIAL"

// -------- Structs for Unmarshalling / Decoding API Responses --------
// Documentation: https://www.nps.gov/subjects/developer/api-documentation.htm#/

// Top Level Struct
type ThingsToDo struct {
	Total string       `json:"total"`
	Data  []ToDoStruct `json:"data"`
}

// Contains most data about the actual "thing to do"
type ToDoStruct struct {
	Location      string `json:"location"`
	SeasonDesc    string `json:"seasonDescription"`
	Accessibility string `json:"accessibilityInformation"`
	Longitude     string `json:"longitude"`

	RelatedParks []ParkStruct `json:"relatedParks"`

	ReservationRequired string `json:"isReservationRequired"`
	AgeDesc             string `json:"ageDescription"`
	PetsDesc            string `json:"petsDescription"`
	TimeOfDayDesc       string `json:"timeOfDayDescription"`

	Images []ImageStruct

	Fees         string           `json:"feeDescription"`
	Activities   []ActivityStruct `json:"activities"`
	ActivityDesc string           `json:"activityDescription"`
	LocationDesc string           `json:"locationDescription"`

	Season       []string `json:"season"`
	DurationDesc string   `json:"durationDescription"`
	TimeOfDay    []string `json:"timeOfDay"`
	Title        string   `json:"title"`
	Latitude     string   `json:"latitude"`
	ShortDesc    string   `json:"shortDescription"`
	// Long Description of the "thing to do" might be unecessary
	LongDesc string `json:"longDescription"`
	Duration string `json:"duration"`
	ID       string `json:"id"`
}

// Contains info and links to images for each "things to do" item
type ImageStruct struct {
	Credit  string `json:"credit"`
	URL     string `json:"url"`
	AltText string `json:"altText"`
	Title   string `json:"title"`
	Caption string `json:"caption"`
}

// Contains info about parks where the "thing to do" is located
type ParkStruct struct {
	States      string `json:"string"`
	FullName    string `json:"fullName"`
	Designation string `json:"designation"`
}

// Contains info about other things to do while doing the "thing to do"
type ActivityStruct struct {
	Name string `json:"name"`
}

// -------- Functions --------
// Note: Internal use lowercase, External use Capital

// Function: handleJSON
// Input: query string
// Return: A ToDoStruct that contains data (depends on calling function)
// Description: gets information from the National Parks API

func handleJSON(query string) ThingsToDo {
	response, respErr := http.Get(query)
	if respErr != nil {
		panic(respErr)
	}
	data, dataErr := ioutil.ReadAll(response.Body)
	if dataErr != nil {
		panic(dataErr)
	}

	var f ThingsToDo
	jsonErr := json.Unmarshal([]byte(data), &f)
	if jsonErr != nil {
		panic(jsonErr)
	}

	return f
}

// Funtion: GetHikingToDo
// Input: State code (all caps)
// Return: A ToDoStruct that contains data on things to do (hiking)
// Description: gets hikes to do from the National Parks API

func GetHikingToDo(state string) ThingsToDo {
	query := fmt.Sprintf("https://developer.nps.gov/api/v1/thingstodo?stateCode=%s&q=hiking&api_key=%s", state, parkKey)
	return handleJSON(query)
}

// Funtion: GetActivity
// Input: Activity ID string
// Return: A ToDoStruct that contains data on specific activity
// Description: gets activities to do from the National Parks API

func GetActivity(id string) ThingsToDo {
	query := fmt.Sprintf("https://developer.nps.gov/api/v1/thingstodo?id=%s&q=hiking&api_key=%s", id, parkKey)
	return handleJSON(query)
}
