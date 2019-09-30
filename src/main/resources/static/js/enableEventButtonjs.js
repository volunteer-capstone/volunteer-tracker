"use strict";

let inputs = document.querySelectorAll("#title, #start, #stop, #venue, #address, #description");
let button = document.querySelector("#create-event");
button.disabled=true;

for (let i = 0; i < inputs.length; i++){
    inputs[i].addEventListener('input', () => {
        let values = [];
        inputs.forEach(v => values.push(v.value));
        button.disabled = values.includes('');
    })
}