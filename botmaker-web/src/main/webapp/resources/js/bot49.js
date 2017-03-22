/**
 * http://www.javascriptkit.com/javatutors/copytoclipboard.shtml
 */

function selectElementText(el) {
	var range = document.createRange();
	range.selectNodeContents(el);
	var selection = window.getSelection();
	selection.removeAllRanges();
	selection.addRange(range);
}

function copySelectionText() {
	var copysuccess;
	try {
		copysuccess = document.execCommand("copy");
	} catch (e) {
		copysuccess = false;
	}
	return copysuccess;
}

function clickCopy(element) {
	element.addEventListener('mouseup', function(e) {
		var e = e || event
		var target = e.target || e.srcElement
		if (target.className == 'console') {
			selectElementText(target);
			var copysuccess = copySelectionText();
		}
	}, false);
}
