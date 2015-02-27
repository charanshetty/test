/*$( document ).ready(function() {
    window.addEventListener("message", receiveMessage, false);
});

function receiveMessage (eve) {
var message = eve.data;
if(eve.origin == "http://localhost:8000"){
document.login.elements["customView"].value = message;
console.log(document.login.elements["customView"].value);
}
}

*/
var paramString="";
var username="";
var password="";
$( document ).ready(function() {

	var button = document.getElementById("submitButton");
	if(button.addEventListener){
	button.addEventListener("click",buttonAction);
	 
	}
	window.addEventListener("message", onMessage, false);
	
	console.log("there");
	/*if(window.postMessage){
	if (document.addEventListener) { //<%# everything that supports postMessage *should* support addEventListener -%>
	document.addEventListener("message", onMessage, false);
	window.addEventListener("message", onMessage, false);
	} else if (document.attachEvent) { //<%# given the postMessage check, no supported browser should hit this -%>
	document.attachEvent("onmessage", onMessage);
	window.attachEvent("onmessage", onMessage);
	} else {// <%# Hail Mary. No suppor
	ted browser should hit this -%>
				window.onmessage = onMessage;
			}
		}*/
		
	});



function passJson(paramString){
	//str = paramString.split("?")[0];
	//var json = JSON.stringify(param);
	//console.log(json);
	/*$.ajax({
		url: ?????,
		type: "POST",
		data: json,
		contentType: =,
		complete: callback
});*/

	//var url = JSON.stringify(str);
	//console.log("here");
	 
	 
	 //var jsonObject={"url":url,"username":username,"password":password};
	 $.ajax({
		  url: "ReqUrlServlet",
		  data: JSON.stringify(paramString),
			type: "GET",
			datatype: 'json'				
		});
		 
    
   // xhr.open('GET', 'ReqUrlServlet', true);
    console.log(paramString);
   // xhr.send(jsonObject);
    
    
	//document.getElementById('firstPane').style.display = 'none';
    //document.getElementById('form1').style.display = 'block';
	//document.getElementById('inputField').value = str;
	//console.log(document.getElementById('inputField').getAttribute('value'));
}

 

function onMessage(e){
	//if(e.origin == "http://localhost"){'
		console.log(e.origin);
		document.login.elements["customView"].value = e.data;
		 paramString=JSON.parse(document.login.elements["customView"].value);
		console.log(JSON.parse(document.login.elements["customView"].value));
	//}
		passJson(paramString);	
}


function buttonAction(){
	username = (document.getElementById('Username').value);
	password = (document.getElementById('Password').value);
	window.top.postMessage("data","http://localhost:8000");
console.log(username);
console.log(password);
}




