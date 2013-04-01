function SetStatic() {
	if (document.all) {
		rcontainer.style.pixelTop = document.documentElement.scrollTop + 10;
	} else if (document.layers) {
		eval(document.rcontainer.top = eval(window.pageYOffset + 10));
	} else if (document.getElementById && !document.all) {
		document.getElementById("rcontainer").style.top = window.pageYOffset
				+ 10 + "px";
	}
}

setInterval("SetStatic()", 10);

function position() {
	if (document.all) {
		rcontainer.style.pixelLeft = document.documentElement.clientWidth - 20 - 120;
		setTimeout('rcontainer.style.visibility = "visible"', 50);
	} else if (document.layers) {
		document.rcontainer.left += window.innerWidth - 25 - 120;
		setTimeout('document.rcontainer.visibility = "visible"', 50);
	} else if (document.getElementById && !document.all) {
		document.getElementById("rcontainer").style.left = window.innerWidth
				- 25 - 120 + "px";
		setTimeout(
				'document.getElementById("rcontainer").style.visibility = "visible"',
				50);
	}
}// end function

position();

function ReplaceContentInContainer(id, content) {
	var container = document.getElementById(id);
	container.innerHTML = content;
}
