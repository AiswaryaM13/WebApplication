/**
 * Handle the items in item list 

 * @param resultDataString jsonObject, needs to be parsed to html 
 */
var results={}
function handleGenreResult(resultData) {
	console.log("handlesearch_formResult: populating movie table with genre");
	let GenreBodyElement = jQuery("#genre_list");
        let rowHTML = "";
        for(let i=0;i<resultData.length-3;i++){
        	rowHTML += "<tr>";
        	for(let j=i; j<i+5;j++){
            	rowHTML += "<th>";
            	rowHTML+= '<a href="movies.html?title=&year=&director=&starname=&genre='+resultData[j]["name"] +'&letter=&pagenum=1&numperpage=20&sortby=p.rating&sort=DESC">'+ resultData[j]["name"] +    
                '</a>';
            	rowHTML+="</th>";
            }        	 
        	rowHTML+="</tr>";
        	i=i+5;
        }
    	rowHTML += "<tr>";
        for(let j=20; j<23;j++){
        	rowHTML += "<th>";
        	rowHTML+='<a href="movies.html?title=&year=&director=&starname=&genre='+resultData[j]["name"] +'&letter=&pagenum=1&numperpage=20&sortby=p.rating&sort=DESC">'+ resultData[j]["name"] +    
            '</a>';
        	rowHTML+= "</th>"; 
        }
    	rowHTML+="</tr>";
            // Append the row created to the table body, which will refresh the page
            GenreBodyElement.append(rowHTML);
      
}

function handlesearchform(resultDataString) {
	console.log("handlesearch_formResult: populating movie table from resultData");

    resultDataJson = JSON.parse(resultDataString);
    
    // if (resultDataJson["present"] === "1") {
         window.location.replace("movies.html?title="+ resultDataJson["title"] +
         "&year="+ resultDataJson["year"]+
         "&director=" + resultDataJson["director"]+
         "&starname=" + resultDataJson["stars"]+
         "&genre="+""+
         "&letter="+""+
         "&pagenum="+"1"+
         "&numperpage="+"20"+
         "&sortby="+"p.rating"+
         "&sort="+"DESC");
 //}
}

function handlebrowseletterform(resultDataString) {
	console.log("handlesearch_formResult: populating movie table with genre");
	
    resultDataJson = JSON.parse(resultDataString);
    console.log(resultDataJson[0]["letter"])
    console.log(resultDataString)
 //    if (resultDataJson["present"] === "1") {
    window.location.replace("movies.html?title="+""+
            "&year="+""+
            "&director=" +""+
            "&starname=" +""+
            "&genre="+""+
            "&letter="+resultDataJson[0]["letter"]+
            "&pagenum="+"1"+
            "&numperpage="+"20"+
            "&sortby="+"p.rating"+
            "&sort="+"DESC");
 }

function submitSearchForm(formSubmitEvent) {
    console.log("submit search form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.get(
        "api/search",
        // Serialize the login form to the data sent by POST request
        $("#search").serialize(),
        (resultDataString) => handlesearchform(resultDataString)
    );
}

function submitBrowseletterForm(formSubmitEvent) {
    console.log("submit genre form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.get(
        "api/search",
        // Serialize the login form to the data sent by POST request
        $("#letter").serialize(),
        (resultDataString) => handlebrowseletterform(resultDataString)
    );
}

/*
 * This function is called by the library when it needs to lookup a query.
 * 
 * The parameter query is the query string.
 * The doneCallback is a callback function provided by the library, after you get the
 *   suggestion list from AJAX, you need to call this function to let the library know.
 */
function handleLookup(query, doneCallback) {
	console.log("autocomplete initiated")
	var term=query;
	// TODO: if you want to check past query results first, you can do it here
	if(term in results){
		console.log("fetch from cache")
		doneCallback(results[term]);
		return;
	}
	console.log("sending AJAX request to backend Java Servlet")
	// sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
	// with the query data
	jQuery.ajax({
		"method": "GET",
		// generate the request url from the query.
		// escape the query string to avoid errors caused by special characters 
		"url": "api/search?query=" + escape(query),
		"success": function(data) {
			// pass the data, query, and doneCallback function into the success handler
			handleLookupAjaxSuccess(data, query, doneCallback) 
		},
		"error": function(errorData) {
			console.log("lookup ajax error")
			console.log(errorData)
		}
	})
}


/*
 * This function is used to handle the ajax success callback function.
 * It is called by our own code upon the success of the AJAX request
 * 
 * data is the JSON data string you get from your Java Servlet
 * 
 */
function handleLookupAjaxSuccess(data, query, doneCallback) {
	console.log("lookup ajax successful")
	console.log(data)
	console.log(query)
	// parse the string into JSON
	var jsonData = JSON.parse(data);
	console.log(jsonData)
	results[query]={ suggestions: jsonData};
	console.log(results)
	// TODO: if you want to cache the result into a global variable you can do it here

	// call the callback function provided by the autocomplete library
	// add "{suggestions: jsonData}" to satisfy the library response format according to
	//   the "Response Format" section in documentation
	doneCallback(results[query]);
}

/*
 * This function is the select suggestion handler function. 
 * When a suggestion is selected, this function is called by the library.
 * 
 * You can redirect to the page you want using the suggestion data.
 */
function handleSelectSuggestion(suggestion) {
	// if (resultDataJson["present"] === "1") {
	 window.location.replace("single-movie.html?id="+suggestion["data"]["id_q"]);
    
	console.log("you selected" + suggestion["value"]+','+ suggestion["data"]["id_q"])
}

$('#autocomplete').autocomplete({
	// documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
    		handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
    		handleSelectSuggestion(suggestion)
    },
    // set delay time
    deferRequestBy: 300,
    minChars:3,
    noCache:false
    // there are some other parameters that you might want to use to satisfy all the requirements
    // TODO: add other parameters, such as minimum characters
});

function handleNormalSearch(query) {
	console.log("doing normal search with query: " + query);
	// if (resultDataJson["present"] === "1") {
	window.location.replace("movies.html?title="+query+
	         "&year="+""+
	         "&director=" +""+
	         "&starname=" +""+
	         "&genre="+""+
	         "&letter="+""+
	         "&pagenum="+"1"+
	         "&numperpage="+"20"+
	         "&sortby="+"p.rating"+
	         "&sort="+"DESC");
}
// bind pressing enter key to a handler function
$('#autocomplete').keypress(function(event) {
	// keyCode 13 is the enter key
	if (event.keyCode == 13) {
		// pass the value of the input box to the handler function
		handleNormalSearch($('#autocomplete').val())
	}
})

//Bind the submit action of the form to a event handler function
$("#search").submit((event) => submitSearchForm(event));
$("#browse").submit((event) => submitBrowsegenreForm(event));
$("#letter").submit((event) => submitBrowseletterForm(event));
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "POST",// Setting request method
    url: "api/search", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleGenreResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});