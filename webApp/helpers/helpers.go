package main

// Global variables
var (
	hikingMET  = 6.0
	cyclingMET = 9.5
)

//// -------- Functions --------
// Note: Internal use lowercase, External use Capital

// Function: GetCaloriesBurned
// Input: weight in KG, time in minutes, activity number
// Returns: An integer retCal
// Description: calculates calories burned during a hike

func GetCaloriesBurned(weight float64, time float64, activity int) int {
	var met float64

	// Set the MET based on the activity
	switch activity {
	case 0:
		met = hikingMET
		break
	default:
		met = -1
		break
	}
	// Invalid activity
	if met == -1 {
		return -1
	}
	// Calculating the calories
	cals := time * met * 3.5 * weight / (200 * 60)
	retCal := int(cals)
	return retCal
}
