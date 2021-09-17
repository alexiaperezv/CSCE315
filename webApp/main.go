package main

import (
	"html/template"
	"net/http"
	"net/url"
	"strings"

	parks "hikipedia/parks"
)

// ----------------
// Structs
// ----------------

// Contains data for the Tabs for the navbar
type Tab struct {
	// If this is the active tab (highlight in template)
	Active bool
	// Name used for the href (all lowercase)
	Name string
	// Name used for the button
	Bar string
}

// Contains core data common to all pages
type Page struct {
	// Name of the page
	Title string
	// Lowercase name of the page
	Lower string
	// Tab List (contained here due to templating)
	Tabs []Tab
}

// Contains ALL data for a page
// Any new structs will be put in here as necessary
type TotalPage struct {
	// Core data
	P Page
	// Trail API data
	R parks.ThingsToDo
}

// ----------------
// Global Page Template
// ----------------

// Global variable for the page
// Allows for modification without worrying about passing it everywhere
// The var is only used here and in 2 very linear functions
var GlobalPage Page

// ----------------
// Functions
// Note: Internal use lowercase, External use Capital
// ----------------

// Function: modifyGlobal
// Input: string of a page name
// Return:
// Description: modifies the global page and updates the data

func modifyGlobal(pageName string) {
	// Set the page name via splitting the name "/name.html" --> "name"
	pageName = strings.Split(pageName, ".html")[0]
	pageName = strings.Split(pageName, "/")[0]

	// Set the Lower and Title data parts
	GlobalPage.Lower = strings.ToLower(pageName)
	GlobalPage.Title = strings.Title(GlobalPage.Lower)

	// Re-update which tabe is active
	for i := range GlobalPage.Tabs {
		if GlobalPage.Tabs[i].Name == pageName {
			GlobalPage.Tabs[i].Active = true
		} else {
			GlobalPage.Tabs[i].Active = false
		}
	}
}

// Function: setupGlobal
// Input:
// Return:
// Description: creates and sets up the global variable with basic data

func setupGlobal() {
	// Default title
	GlobalPage.Title = ""

	// Here is all the names of the tabs in the nav-bar
	// Have to manually add them but only once here
	var strList []string
	strList = append(strList, "Home")
	strList = append(strList, "About")
	strList = append(strList, "Search")
	strList = append(strList, "Results")

	// Add the tabs to the global var
	for _, x := range strList {
		GlobalPage.Tabs = append(GlobalPage.Tabs, createTab(x, false))
	}
}

// Function: createTab
// Input: name and status (active/inactive) of a tab
// Return: A Tab struct
// Description: creates the website tabs

func createTab(name string, active bool) Tab {
	lower := strings.ToLower(name)
	bar := strings.ToUpper(string(lower[0])) + lower[1:]
	return Tab{Active: active, Name: lower, Bar: bar}
}

// Function: executePageTemplate
// Input: http.ResponseWriter, prefix string, filePath string, and pointer to a TotalPage
// Return:
// Description: "compiles" and executes the page template(s) (endpoint of web server)

func executePageTemplate(w http.ResponseWriter, prefix string, filePath string, pageStruct *TotalPage) {
	// Requires that base.html, headerNav.html, and <prefix>+<filePath> are part of the template
	// This "gathers" all the necessary files
	templs := template.Must(template.ParseFiles("Pages/base.html", "Pages/headerNav.html", prefix+filePath))

	// This fills out the template structure with the pageStruct and writes it out to the http.ResponseWriter
	// The name "base" is just any name but base seemed appropriate
	templs.ExecuteTemplate(w, "base", pageStruct)
}

// Function: createPath
// Input:
// Return:
// Description:

func createPath(p string) string {
	switch p {
	case "":
		p = "home.html"
		break

	case "favicon.ico":
		p = "favicon.ico"
		break
	}

	return p
}

// Function: createPrefix
// Input:
// Return:
// Description:

func createPrefix(p string) string {
	switch p {
	case "html":
		p = "Pages/"
		break

	case "js":
		p = "Scripts/"
		break

	case "jfif":
	case "jpeg":
	case "png":
	case "PNG":
	case "jpg":
	case "JPG":
	case "JPEG":
		p = "Images/"
		break

	case "css":
		p = "Styles/"
		break

	case "ico":
		p = ""
		break
	}

	return p
}

// Function: setupPageStruct
// Input:
// Return:
// Description:

func setupPageStruct(p string, state string, id string) TotalPage {
	var TP TotalPage
	TP.P = GlobalPage

	switch p {
	case "results.html":
		TP.R = parks.GetHikingToDo(state)
		break
	case "expandedResults.html":
		TP.R = parks.GetActivity(id)
		break
	default:
		break
	}

	return TP
}

// ----------------
// Handler Function
// ----------------

// -- Notes --
// r.URL.Path = "/search.html"
// r.URL.RawQuery = "state=TX&value=yeet"

// Function: handler
// Input:
// Return:
// Description:

func handler(w http.ResponseWriter, r *http.Request) {
	prefix := "Pages/"
	var TP TotalPage

	if strings.Contains(r.URL.Path, ".") {
		prefix = strings.Split(r.URL.Path, ".")[1]
	}
	prefix = createPrefix(prefix)

	filePath := createPath(r.URL.Path[1:])
	if filePath == "favicon.ico" {
		return
	}

	modifyGlobal(filePath)

	params, paramsErr := url.ParseQuery(r.URL.RawQuery)
	if paramsErr != nil {
		panic(paramsErr)
	}

	state := ""
	_, ok := params["input"]
	if ok {
		state = params["input"][0]
	}

	id := ""
	_, ok = params["id"]
	if ok {
		id = params["id"][0]
	}

	TP = setupPageStruct(r.URL.Path[1:], state, id)

	executePageTemplate(w, prefix, filePath, &TP)
}

// ----------------
// Main Function
// ----------------

func main() {
	// Create the initial Global Page
	setupGlobal()

	// These lines allow for the html files to include and reference other files with global paths
	// So instead of "../Images/etc.png" we can use "Images/etc.png" on all files
	// The exception is other Pages which, for purposes of the website, are hosted as if they were all in the root folder
	http.Handle("/Pages/", http.StripPrefix("/Pages/", http.FileServer(http.Dir("/"))))
	http.Handle("/Images/", http.StripPrefix("/Images/", http.FileServer(http.Dir("Images"))))
	http.Handle("/Styles/", http.StripPrefix("/Styles/", http.FileServer(http.Dir("Styles"))))
	http.Handle("/Scripts/", http.StripPrefix("/Scripts/", http.FileServer(http.Dir("Scripts"))))

	// For any page request (since we specify the root) use the "handler" function
	http.HandleFunc("/", handler)

	// Listen and Serve
	http.ListenAndServe(":8681", nil)
}
