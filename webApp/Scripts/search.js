/* JavaScript for website's "search" page */

// Function: filterDropDown
// Input: NA
// Return: NA
// Description: expands the filter options under the search bar in the "search" page

function filterDropDown() {
    var x = document.getElementById("myDropdown");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}

// Function: abbreviateState
// Input: state code or state name
// Return: state code or error code "QQ"
// Description: gets the input state's 2-letter abreviation

function abbreviateState(input) {
    // Hard coded list of US states
    var states = [
        ['Arizona', 'AZ'],
        ['Alabama', 'AL'],
        ['Alaska', 'AK'],
        ['Arkansas', 'AR'],
        ['California', 'CA'],
        ['Colorado', 'CO'],
        ['Connecticut', 'CT'],
        ['Delaware', 'DE'],
        ['Florida', 'FL'],
        ['Georgia', 'GA'],
        ['Hawaii', 'HI'],
        ['Idaho', 'ID'],
        ['Illinois', 'IL'],
        ['Indiana', 'IN'],
        ['Iowa', 'IA'],
        ['Kansas', 'KS'],
        ['Kentucky', 'KY'],
        ['Louisiana', 'LA'],
        ['Maine', 'ME'],
        ['Maryland', 'MD'],
        ['Massachusetts', 'MA'],
        ['Michigan', 'MI'],
        ['Minnesota', 'MN'],
        ['Mississippi', 'MS'],
        ['Missouri', 'MO'],
        ['Montana', 'MT'],
        ['Nebraska', 'NE'],
        ['Nevada', 'NV'],
        ['New Hampshire', 'NH'],
        ['New Jersey', 'NJ'],
        ['New Mexico', 'NM'],
        ['New York', 'NY'],
        ['North Carolina', 'NC'],
        ['North Dakota', 'ND'],
        ['Ohio', 'OH'],
        ['Oklahoma', 'OK'],
        ['Oregon', 'OR'],
        ['Pennsylvania', 'PA'],
        ['Rhode Island', 'RI'],
        ['South Carolina', 'SC'],
        ['South Dakota', 'SD'],
        ['Tennessee', 'TN'],
        ['Texas', 'TX'],
        ['Utah', 'UT'],
        ['Vermont', 'VT'],
        ['Virginia', 'VA'],
        ['Washington', 'WA'],
        ['West Virginia', 'WV'],
        ['Wisconsin', 'WI'],
        ['Wyoming', 'WY'],
    ];

    // Split on the comma
    input = input.split(",");

    // If there is only 1 item (no comma) then error
    if (input.length == 1) { return "QQ"; }

    // Get the 2nd item (state) and trim the whitespace 
    input = input[1];
    input = input.trim();

    // If they entered a possible state code
    if (input.length == 2) {

        // Check to see if it matches
        input = input.toUpperCase();
        for (i = 0; i < states.length; i++) {
            if (states[i][1] == input) {
                return input;
            }
        }
        // Otherwiese return the error code
        return "QQ";
    }

    else {
        // If they entered a possible state, first convert it into title format
        input = input.toLowerCase();
        input = input.charAt(0).toUpperCase() + input.substr(1);

        // Check to see if it matches
        for (i = 0; i < states.length; i++) {
            if (states[i][0] == input) {
                return (states[i][1]);
            }
        }
        // Otherwiese return the error code
        return "QQ";
    }
}

// Function: createURLParameters
// Input: user selections (input location, chosen filters)
// Return: URL parameter(s)
// Description: passes user selections using URL parameters

function createURLParameters(input, statepark, nationalpark, intensityeasy, intensitymedium, intensityhard, searchradius) {

    // Check if input is valid
    if (abbreviateState(input) == "QQ") {
        // Redirect to a error page
        // var url = "error.html?error=search"
        // window.location.href = url
    }

    else {
        // If input is valid set up url for results.html redirection
        var url = "results.html"
        var params = ""
        var seperator = "&"

        input = input.split(",");
        input = input[1];
        input = input.trim();
        params += "?input=" + input

        // Only include the variable if true
        if (statepark == true) {
            params += seperator
            params += "statepark=true"
        }
        if (nationalpark == true) {
            params += seperator
            params += "nationalpark=true"
        }
        if (intensityeasy == true) {
            params += seperator
            params += "intensityeasy=true"
        }
        if (intensitymedium == true) {
            params += seperator
            params += "intensitymedium=true"
        }
        if (intensityhard == true) {
            params += seperator
            params += "intensityhard=true"
        }
        if (searchradius != "") {
            params += seperator
            params += "searchradius=" + searchradius
        }

        // console.log("params: " + params)
        // Create the output url and pass href
        url += params
        window.location.href = url
    }
}
