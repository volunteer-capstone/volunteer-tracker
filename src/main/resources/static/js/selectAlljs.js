"use strict";
function checkAll(source) {
    let checkboxes = document.getElementsByName('check');
    for(let i=0; i < checkboxes.length ; i++) {
        checkboxes[i].checked = source.checked;
    }
}