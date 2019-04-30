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



function handleSessionData(resultDataString) {

    const resultArray = resultDataString.split(",");

    console.log(resultArray.length);
    console.log(resultArray);
    let n=resultArray.length;
    let br = "<br>";
    if(n>2) {
        let res = "<ol>";
        for (let i = 2; i < n; i+=2) {
            // each item will be in a bullet point
            res += "<li>" + resultArray[i]+ "</li>";//even number is id odd is title
            res += " Movie Count "+resultArray[i+1]+"&nbsp";
           
            res += "<input type='text' placeholder='Enter quantity' name=" + i + ">";
            res += "</li>";
        }
        res += "</ol>";
        res += "<input type='submit' value='Update Quantity'>";

        // clear the old array and show the new array in the frontend
        $("#movie_list").html("");
        $("#movie_list").append(br);
        $("#movie_list").append(res);

    }
    else{
        let res = "<ul>";
        res += "<li>" ;//even number is id odd is title
        res += "Nothing in cart";


        res += "</li>";
        res += "</ul>";
        $("#movie_list").html("");
        $("#movie_list").append(res);
    }
}

/**
 * Handle the items in item list
 * @param resultDataString jsonObject, needs to be parsed to html
 */

function handleCartInfo(cartEvent) {
    console.log("submit cart form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    cartEvent.preventDefault();

    $.get(
        "api/cart",
        // Serialize the cart form to the data sent by POST request
        $("#movie_list").serialize(),
        (resultDataString) => handleSessionData(resultDataString)
);
}
let id=getParameterByName("id");


$.ajax({
    type: "GET",
    url: "api/cart?id="+id,
    success: (resultDataString) => handleSessionData(resultDataString)
});



$("#movie_list").submit((event) => handleCartInfo(event));
