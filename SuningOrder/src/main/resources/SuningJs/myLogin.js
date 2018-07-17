var page = require('webpage').create(), stepIndex = 0, loadInProgress = false;
var system = require('system');
var userName = system.args[1];
var password = system.args[2];
var clicked = false;
page.settings.userAgent = 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36';

page.onConsoleMessage = function(msg) {
	console.log(msg);
}

page.onLoadFinished = function(status) {
	if (clicked) {
		if (page.url == "https://www.suning.com/") {
			console.log("success");
			var cookies = page.cookies;
			for ( var i in cookies) {
				console.log(cookies[i].name + "=" + cookies[i].value + "="
						+ cookies[i].domain + "=" + cookies[i].path);
			}
		} else {
			console.log("failed: clicked finished, but url = " + page.url);
		}
		phantom.exit();
	}
};

page.open('https://passport.suning.com/ids/login', function(status) {
	if (status == "success") {
		page.evaluate(function(userName, password) {
			document.getElementById("userName").value = userName;
			document.getElementById("password").value = password;
			document.getElementById("submit").click();
		}, userName, password);
		clicked = true;
	} else {
		console.log('failed: open login page failed');
		phantom.exit();
	}
});

var interval = setInterval(function() {
	if (clicked) {
		var returnMsg = '';
		if (page.url == 'https://passport.suning.com/ids/login') {
			returnMsg = page.evaluate(function(obj) {
				var errorDivs = document.getElementsByClassName("login-error");
				var i;
				var msg = '';
				for (i = 0; i < errorDivs.length; i++) {
					if (errorDivs[i].style.display != "none") {
						var errorSpans = errorDivs[i].childNodes;
						var j;
						for (j = 0; j < errorSpans.length; j++) {
							if (errorSpans[j].localName == "span") {
								msg = "failed: " + errorSpans[j].innerText;
								return msg;
							}
						}
						msg = "failed";
						return msg;
					}
				}
				return msg;
			});
		}
		if (returnMsg.length > 0) {
			console.log(returnMsg);
		} else {
			console.log("failed: login timeout");
		}
		phantom.exit();
	}
}, 60000);