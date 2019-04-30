function handleLoginResult(resultDataString) {
	console.log(resultDataString)
    resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // if login success, redirect to index.html page
    if (resultDataJson["status"] === "success") {
        window.location.replace("main.html");
    } else {
        console.log("show error message");
        console.log(resultDataJson["message"]);
        jQuery("#login_error_message").text(resultDataJson["message"]);
    }
}


function submitLoginForm(formSubmitEvent) {
    console.log("submit login form");

    // important: disable the default action of submitting the form
    //   which will cause the page to refresh
    //   see jQuery reference for details: https://api.jquery.com/submit/
    formSubmitEvent.preventDefault();
    
    // get the gRecapthcaResponse
    gRecapthcaResponse = grecaptcha.getResponse();
    console.log(gRecapthcaResponse);
    
    // transform the form data to a Javascript Object
    //   using the jQuery plugin: https://github.com/macek/jquery-serialize-object
    // we included this plugin in the login.html
    formDataObject = $('#login_form').serializeObject();
    
    // we need attach both the g-recaptcha-response and username/password to the request
    // combine the form data and gRecapthcaResponse into a new object
    requestDataObject = Object.assign(
    		{"g-recaptcha-response": gRecapthcaResponse},
    		formDataObject)
    
    	console.log(requestDataObject);
    
    jQuery.post(
        "api/login",
        // serialize the login form to the data sent by POST request
        requestDataObject,
        (resultDataString) => handleLoginResult(resultDataString));

}

// bind the submit action of the form to a handler function
jQuery("#login_form").submit((event) => submitLoginForm(event));