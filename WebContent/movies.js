/**
 * This example is following frontend and backend separation.

 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */
/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    console.log(decodeURIComponent(results[2].replace(/\+/g, " ")));
    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function handleStarResult(resultData) {
    console.log("handleStarResult: populating movie table from resultData");
    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let MovieTableBodyElement = jQuery("#movies_table_body");

    // Iterate through resultData, no more than 10 entries
    //for (let i = 0; i < Math.min(40, resultData.length); i++) {
    for (let i = 0; i <  resultData.length; i++) {
    	
        //StarIDarr = resultData[i]["star_id"].split(",");
    	//StarNamearr = resultData[i]["star_name"].split(",");
        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +="<th>"+(parseInt(resultData[i]["index"],10)+1+i)+"</th>";
        rowHTML +="<th>"+resultData[i]["id"]+"</th>";
        rowHTML +=
            "<th>" +
            // Add a link to single-movie.html with id passed with GET url parameter
            '<a href="single-movie.html?id=' + resultData[i]['id'] + '">'
            + resultData[i]["title"] +     // display star_name for the link text
            '</a>' +
            "</th>";

        rowHTML += "<th>" + resultData[i]["year"] + "</th>";
        rowHTML += "<th>" + resultData[i]["director"] + "</th>";
        rowHTML += "<th>" + resultData[i]["genreName"] + "</th>";
       // rowHTML +=" <th>" + resultData[i]["rating"] + "</th>";
        rowHTML +=" <th>" ;
        for(let j=0;j<resultData[i]["star_name"].split(",").length;j++){
        	if(j<resultData[i]["star_id"].split(",").length-1){
        		rowHTML +=
        			// Add a link to single-star.html with id passed with GET url parameter
        			'<a href="single-star.html?id=' + resultData[i]["star_id"].split(",")[j] + '">'
        			+ resultData[i]["star_name"].split(",")[j] + ", " +      // display star_name for the link text
        			'</a>' ;
        	}
        else{
        	rowHTML +=
        		// Add a link to single-star.html with id passed with GET url parameter
        		'<a href="single-star.html?id=' + resultData[i]["star_id"].split(",")[j] + '">'
        		+ resultData[i]["star_name"].split(",")[j] +      // display star_name for the link text
        		'</a>' ;
        	}
        }
        rowHTML +=" </th>" ;
        rowHTML +=" <th>" + resultData[i]["rating"] + "</th>";
        rowHTML +=
            "<th>" +
            // Add a link to single-movie.html with id passed with GET url parameter
            '<a href="cart.html?id=' + resultData[i]['id'] + '">'
            + "Add To Cart"+     // display star_name for the link text
            '</a>' +
            "</th>";
        //rowHTML += "</tr>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        MovieTableBodyElement.append(rowHTML);
    }
}

function handlepagenextResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);
    // If login succeeds, it will redirect the user to index.html
    window.location.replace(
        "movies.html?title="+title+
        "&year="+year +
        "&director="+director+
        "&starname="+stars+
        "&genre="+genre+
        "&letter="+letter+
        "&pagenum="+resultDataJson["pagenext"]+
        "&numperpage="+numperpage+
        "&sortby="+sortby+
        "&sort="+sort
    );
}

function handlepageprevResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);
    // If login succeeds, it will redirect the user to index.html
    window.location.replace(
        "movies.html?title="+title+
        "&year="+year +
        "&director="+director+
        "&starname="+stars+
        "&genre="+genre+
        "&letter="+letter+
        "&pagenum="+resultDataJson["pageprev"]+
        "&numperpage="+numperpage+
        "&sortby="+sortby+
        "&sort="+sort
    );
}

function handlesorttitleascResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);
    // If login succeeds, it will redirect the user to index.html
    window.location.replace(
        "movies.html?title="+title+
        "&year="+year +
        "&director="+director+
        "&starname="+stars+
        "&genre="+genre+
        "&letter="+letter+
        "&pagenum="+pagenum+
        "&numperpage="+numperpage+
        "&sortby="+resultDataJson["sorttitleasc"]+
        "&sort="+resultDataJson["sorttitleasc1"]
    );
}

function handlesorttitledescResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);
    // If login succeeds, it will redirect the user to index.html
    window.location.replace(
        "movies.html?title="+title+
        "&year="+year +
        "&director="+director+
        "&starname="+stars+
        "&genre="+genre+
        "&letter="+letter+
        "&pagenum="+pagenum+
        "&numperpage="+numperpage+
        "&sortby="+resultDataJson["sorttitledesc"]+
        "&sort="+resultDataJson["sorttitledesc1"]
    );
}


function handlenumperpageResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);
    // If login succeeds, it will redirect the user to index.html
    window.location.replace(
        "movies.html?title="+title+
        "&year="+year +
        "&director="+director+
        "&starname="+stars+
        "&genre="+genre+
        "&letter="+letter+
        "&pagenum="+pagenum+
        "&numperpage="+resultDataJson["numperpage"]+
        "&sortby="+sortby+
        "&sort="+sort
    );
}

function handlesortratingascResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);
    // If login succeeds, it will redirect the user to index.html
    window.location.replace(
        "movies.html?title="+title+
        "&year="+year +
        "&director="+director+
        "&starname="+stars+
        "&genre="+genre+
        "&letter="+letter+
        "&pagenum="+pagenum+
        "&numperpage="+numperpage+
        "&sortby="+resultDataJson["sortratingasc"]+
        "&sort="+resultDataJson["sortratingasc1"]
    );
}

function handlesortratingdescResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);
    // If login succeeds, it will redirect the user to index.html
    window.location.replace(
        "movies.html?title="+title+
        "&year="+year +
        "&director="+director+
        "&starname="+stars+
        "&genre="+genre+
        "&letter="+letter+
        "&pagenum="+pagenum+
        "&numperpage="+numperpage+
        "&sortby="+resultDataJson["sortratingdesc"]+
        "&sort="+resultDataJson["sortratingdesc1"]
    );
}

function submitpagenextForm(formSubmitEvent) {
    console.log("ss");
    formSubmitEvent.preventDefault();
    $.post(
        "api/movies",
        // Serialize the login form to the data sent by POST request
        $("#pagenext").serialize(),
        (resultDataString) => handlepagenextResult(resultDataString)
);
}
$("#pagenext").submit((event) => submitpagenextForm(event));

function submitpageprevForm(formSubmitEvent) {
    console.log("ss1");
    formSubmitEvent.preventDefault();
    $.post(
        "api/movies",
        // Serialize the login form to the data sent by POST request
        $("#pageprev").serialize(),
        (resultDataString) => handlepageprevResult(resultDataString)
);
}
$("#pageprev").submit((event) => submitpageprevForm(event));


function submitsortratingdescForm(formSubmitEvent) {
    console.log("ss");
    formSubmitEvent.preventDefault();
    $.post(
        "api/movies",
        // Serialize the login form to the data sent by POST request
        $("#sortratingdesc").serialize(),
        (resultDataString) => handlesortratingdescResult(resultDataString)
);
}
$("#sortratingdesc").submit((event) => submitsortratingdescForm(event));

function submitsorttitleascForm(formSubmitEvent) {
    console.log("ss");
    formSubmitEvent.preventDefault();
    $.post(
        "api/movies",
        // Serialize the login form to the data sent by POST request
        $("#sorttitleasc").serialize(),
        (resultDataString) => handlesorttitleascResult(resultDataString)
);
}
$("#sorttitleasc").submit((event) => submitsorttitleascForm(event));

function submitsorttitledescForm(formSubmitEvent) {
    console.log("ss");
    formSubmitEvent.preventDefault();
    $.post(
        "api/movies",
        // Serialize the login form to the data sent by POST request
        $("#sorttitledesc").serialize(),
        (resultDataString) => handlesorttitledescResult(resultDataString)
);
}
$("#sorttitledesc").submit((event) => submitsorttitledescForm(event));

function submitsortratingascForm(formSubmitEvent) {
    console.log("ss");
    formSubmitEvent.preventDefault();
    $.post(
        "api/movies",
        // Serialize the login form to the data sent by POST request
        $("#sortratingasc").serialize(),
        (resultDataString) => handlesortratingascResult(resultDataString)
);
}
$("#sortratingasc").submit((event) => submitsortratingascForm(event));
function submitnumperpageForm(formSubmitEvent) {
    console.log("ss1");
    formSubmitEvent.preventDefault();
    $.post(
        "api/movies",
        // Serialize the login form to the data sent by POST request
        $("#numperpage").serialize(),
        (resultDataString) => handlenumperpageResult(resultDataString)
);
}
$("#numperpage").submit((event) => submitnumperpageForm(event));

/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
let title = getParameterByName('title');
let year = getParameterByName('year');
let director = getParameterByName('director');
let stars = getParameterByName('starname');
let genre = getParameterByName('genre');
let letter = getParameterByName('letter');
let pagenum = getParameterByName('pagenum');
let numperpage = getParameterByName('numperpage');
let sortby = getParameterByName('sortby');
let sort = getParameterByName('sort');
// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/movies?title="+title +
         "&year="+year+
         "&director=" + director+
         "&starname=" + stars+
         "&genre="+genre+
         "&letter="+letter+
         "&pagenum="+pagenum+
         "&numperpage="+numperpage+
         "&sortby="+sortby+
         "&sort="+sort, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});