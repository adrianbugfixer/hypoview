// Copyright (c) 2014 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

/**
 * Get the current URL.
 *
 * @param {function(string)} callback - called when the URL of the current tab
 *   is found.
 */
WEBSITE_URL = "";
PROTOCOL_SSL = "https://";
PROTOCOL_UNS = "http://";
PROTOCOL = PROTOCOL_UNS;

PROTOCOL = PROTOCOL_UNS;

function renderComments(comments) {
	var commentsDiv = document.getElementById("comments");
	commentsDiv.innerHTML = "";
	for (var i = 0; i < comments.length; i++) {
		var commentDiv = document.createElement("div");
		commentDiv.setAttribute("id", "hv_comment_" + comments[i].id);
		commentDiv.setAttribute("class", "comment");

		var commentImg = document.createElement("img");
		commentImg.setAttribute("src", "avatar_default.png");

		commentDiv.appendChild(commentImg);

		var commentHeader = document.createElement("div");
		commentHeader.innerHTML += '<b>vonsuu</b><span class="timeAgo"> 4 hours ago</span><span style="float: right;">+ -</span>';

		commentDiv.appendChild(commentHeader);

		var commentContent = document.createTextNode(comments[i].content);

		commentDiv.appendChild(commentContent);

		commentsDiv.appendChild(commentDiv);
		//
		//var newDiv = document.createElement("div");
		//newDiv.setAttribute("id", "hv_comment_"+comments[i].id);
		//var newContent = document.createTextNode(comments[i].content);   
		//newDiv.appendChild(newContent);
		//var commentsDiv = document.getElementById("comments"); 
		//commentsDiv.appendChild(newDiv);
	}
}

function initPlugin(url) {
	var api = PROTOCOL+"localhost:8080/api/website/";
	var params = {
		"uri" : url
	}
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.open("POST", api, true);
	xmlhttp.setRequestHeader("Content-type", "application/json");
	xmlhttp.send(JSON.stringify(params));
	xmlhttp.onreadystatechange = function() { //Call a function when the state changes.
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			//alert(xmlhttp.responseText);
			var response = JSON.parse(this.responseText);
			WEBSITE_ID = response.id;
			//renderComments(response.comments);
		}
	}
}

function addComment(params) {
	var xmlhttp = new XMLHttpRequest();
	var api = PROTOCOL+"localhost:8443/api/website/" + WEBSITE_ID + "/comments";
	xmlhttp.open("POST", api, true);
	xmlhttp.setRequestHeader("Content-type", "application/json");
	xmlhttp.send(JSON.stringify(params));
	xmlhttp.onreadystatechange = function() { //Call a function when the state changes.
		if (xmlhttp.readyState == 4) {
			getComments(function(comments) {
				renderComments(comments);
			});
		}
	}
}

function getComments(callback) {
	var xmlhttp = new XMLHttpRequest();
	var api = PROTOCOL+"localhost:8443/api/website/" + WEBSITE_ID + "/comments";
	var comments = {};
	xmlhttp.open("GET", api, true);
	xmlhttp.setRequestHeader("Content-type", "application/json");
	xmlhttp.send();
	xmlhttp.onreadystatechange = function() { //Call a function when the state changes.
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			comments = JSON.parse(this.responseText);
		}
		callback(comments);
	}
}

function getComment(params,callback) {
	var xmlhttp = new XMLHttpRequest();
	var commentId = params.commentId;
	var api = PROTOCOL+"localhost:8443/api/comment/" + commentId;
	var comments = {};
	xmlhttp.open("GET", api, true);
	xmlhttp.setRequestHeader("Content-type", "application/json");
	xmlhttp.send();
	xmlhttp.onreadystatechange = function() { //Call a function when the state changes.
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			comment = JSON.parse(this.responseText);
			callback(comment);
		}
	}
}
function authenticate(callback) {
	var xmlhttp = new XMLHttpRequest();
	var api = PROTOCOL+"localhost:8443/oauth/token";
	var params = "grant_type=password&username=adrian.bugfixer%40gmail.com&password=Surykatka1!";
	xmlhttp.open("POST", api, true);
	xmlhttp.setRequestHeader("Content-type",
			"application/x-www-form-urlencoded");
	xmlhttp.send(params);

	var response;
	xmlhttp.onreadystatechange = function() { //Call a function when the state changes.
		if (xmlhttp.readyState == 4) {
			response = JSON.parse(this.responseText);
		}
		callback(response);
	}
}

function getFrameParams() {
	var params = location.href.split('?')[1];
	if (params != null) {
		params = params.split('&');
	}
	console.log("%o", params);
	data = {};
	for (x in params) {
		data[params[x].split('=')[0]] = params[x].split('=')[1];
	}
	return data;
}

function commentsClickHandler(e) {
	if (e.target !== e.currentTarget) {
		var clickedItem = e.target.id;
		var clickedElement = e.target;
		var commentId;
		var rate;
		if (clickedItem.startsWith("upvote_comment")) {
			commentId = clickedElement.getAttribute("comment_id");
			rate = "UPVOTE";
		}
		if (clickedItem.startsWith("downvote_comment")) {
			commentId = clickedElement.getAttribute("comment_id");
			rate = "DOWNVOTE";
		}
		if(rate !== null && commentId !== null) {
			var params = {
				"commentId" : commentId,
				"rate" : rate
			}
			rateComment(params);
		}
	}
	e.stopPropagation();
}


function refreshCommentRating(params) {
	getComment(params, function(comment){
		var rating = comment.rate;
		var ratingSpan = document.getElementById("rate_comment_id_"+comment.id);
		ratingSpan.innerHTML=rating;
	})
}

function rateComment(params) {
	var xmlhttp = new XMLHttpRequest();
	var api = PROTOCOL+"localhost:8443/api/comment/" + params.commentId + "?rate="
			+ params.rate;
	xmlhttp.open("GET", api, true);
	xmlhttp.setRequestHeader("Content-type", "application/json");
	xmlhttp.send();
	xmlhttp.onreadystatechange = function() { //Call a function when the state changes.
		if (xmlhttp.readyState == 4) {
			refreshCommentRating(params);
		}
	}
}

document.addEventListener('DOMContentLoaded', function() {
	var frameParams = getFrameParams();
	console.log("%o", frameParams);
	var api = PROTOCOL+"localhost:8443/api/website/";
	//var addCommentSubmit = document.getElementById('comment_submit');

	var commentsParent = document.getElementById("comments");
	commentsParent.addEventListener("click", commentsClickHandler, false);

	var commentUpvote = document.getElementById('comment_upvote');
	var commentDownvote = document.getElementById('comment_downvote');

	var url = frameParams.url;
	var authCompletedEvent = new CustomEvent("AuthCompleted", {
		"detail" : "Fires when authentication is completed."
	});

	WEBSITE_URL = url;
	document.getElementById('url').textContent = url;
	//initPlugin(WEBSITE_URL);
	// authenticate(function(response){
	//	  console.log("response %o", response);
	//token = response;
	//	  document.dispatchEvent(authCompletedEvent);
	// });

	//  document.addEventListener('AuthCompleted', function(event){
	//	  console.log("token %o", token);
	//  })
	//  
	//  commentUpvoteSubmit.addEventListener('click', function(event) {
	//    event.preventDefault();
	//    var commentId = document.getElementById('comment_content').value;
	//    var params = {
	//      "rate" : "UPVOTE",
	//      "commentId" : commentId
	//    }
	//    upvoteComment(params);
	//  });
	//addCommentSubmit.addEventListener('click', function(event) {
	//    event.preventDefault();
	//    var commentContent = document.getElementById('comment_content').value;
	//    var params = {
	//      "content" : commentContent,
	//      "websiteId" : WEBSITE_ID
	//    }
	//    addComment(params);
	//  });
});
