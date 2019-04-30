function handleConfirmData(resultDataString) {
    	const result=resultDataString.split(",");
    	 console.log(result);
    	let res="";
    	    for(let i = 0; i < result.length-3; i=i+3) {
    	    	  res+= "<tr>";
    	    		// each item will be in a bullet point
    	    	 
        	        res += "<th>" + result[i] + "</th>"; 
        	        res += "<th>" + result[i+1] + "</th>"; 
        	        res += "<th>" + result[i+2] + "</th>"; 
    	    	res += "</tr>";
    	    }
    	    
    $("#confirm_table_body").html("");
    $("#confirm_table_body").append(res);
}

$.ajax({
	
    method: "GET",// Setting request method
    url: "api/confirm", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultDataString) => handleConfirmData(resultDataString) // Setting callback function to handle data returned successfully by the SingleStarServlet
});

