var numericRegex = /\d+\.?\d*/;
function validateNumericInput(evt, text) {
	var theEvent = evt || window.event;
	var key = theEvent.keyCode || theEvent.which;
	key = String.fromCharCode(key);
	if (key === '.') {
		theEvent.returnValue = (text.indexOf(key) < 0);
	} else {
		var regex = /\d/;
		if (!regex.test(key)) {
			theEvent.returnValue = false;
			if (theEvent.preventDefault)
				theEvent.preventDefault();
		}
	}
}