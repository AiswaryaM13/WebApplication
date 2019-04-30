/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */

function handleAddStarResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);
    // If login succeeds, it will redirect the user to index.html
    if (resultDataJson["status"] === "success") {
    	 $("#dashboard_error_message").text(resultDataJson["message1"]);
    } else {
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#dashboard_error_message").text(resultDataJson["message"]);
    }
}

function handleMovieAddResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);
	console.log(resultDataJson["message1"]);
    // If login succeeds, it will redirect the user to index.html
    if (resultDataJson["status"] === "success") {
    	$("#addmovie_error_message").text(resultDataJson["message"]);
    } else {
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#addmovie_error_message").text(resultDataJson["message"]);
    }
}


/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitAddStarForm(formSubmitEvent) {
    console.log("submit checkout form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.post(
        "api/dashboard",
        // Serialize the login form to the data sent by POST request
        $("#dashboard_form").serialize(),
        (resultDataString) => handleAddStarResult(resultDataString)
    );
}

function submitMovieForm(formSubmitEvent) {
    console.log("adding movie details");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.get(
        "api/dashboard",
        // Serialize the login form to the data sent by POST request
        $("#add_movie").serialize(),
        (resultDataString) => handleMovieAddResult(resultDataString)
    );
}


// Bind the submit action of the form to a handler function
$("#dashboard_form").submit((event) => submitAddStarForm(event));
$("#add_movie").submit((event) => submitMovieForm(event));
