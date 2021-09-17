/* JavaScript code for website's "results" page */

// API Result Struct
const apiResult = [{
  title: "title1",
  description: "desc1",
  output: "out1"
}, {
  title: "title2",
  description: "desc2",
  output: "out2"
}, {
  title: "title3",
  description: "desc3",
  output: "out3"
}];

//// -------- Creation of containers to display each result on "results" page -------- ////

const container = document.getElementById('card-container');

apiResult.forEach((result, idx) => {
  // Create card element
  const card = document.createElement('div');
  card.classList = 'card-body';

  // Construct card content
  const content = `
      <div class="card">
      <div class="card-header" id="heading-${idx}">
        <h5 class="mb-0">
          <button class="btn btn-link" data-toggle="collapse" data-target="#collapse-${idx}" aria-expanded="true" aria-controls="collapse-${idx}">
          </button>
        </h5>
      </div>
  
      <div id="collapse-${idx}" class="collapse show" aria-labelledby="heading-${idx}" data-parent="#accordion">
        <div class="card-body">
  
          <h5>${result.title}</h5>
          <p>${result.description}</p>
          <p>${result.output}</p>
          ...
        </div>
      </div>
    </div>
    `;

  // Append newyly created card element to the container
  if (document.getElementById("card-container") != null) {
    container.innerHTML += content;
  }
})

// Function: createExpandedParameters
// Input: id parameter
// Return: An href to the new URL
// Description: passes id parameter to URL

function createExpandedParameters(id) {
  var url = "expandedResults.html"
  var params = ""
  var seperator = "&"

  params += "?id=" + id
  console.log("params: " + params)
  url += params
  window.location.href = url
}