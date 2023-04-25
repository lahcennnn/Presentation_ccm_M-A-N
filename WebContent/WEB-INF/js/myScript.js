//set checked value on radioBox
function setCheckedValue(formId, elementId) {
	if (formId.elements[elementId].checked == true)
		formId.elements[elementId].checked = false;
	else {
		formId.elements[elementId].checked = true;
	}
}

// set selected options in listSlection
function setSelectedOptions(formId, elementId, optionIndex) {
	if (formId.elements[elementId].options[optionIndex].selected == true) {
		formId.elements[elementId].options[optionIndex].selected = false;
	} else {
		formId.elements[elementId].options[optionIndex].selected = true;
	}
}

function disablementFunction(day) {
	curDt = day.date.getDate;
	if (curDt.getTime() - day.date.getTime() > 0)
		return true;
	else
		return false;
}
