"use strict";
(function(){
    $(document).ready(function(){
        let inputs = document.querySelectorAll("#title, #start, #end, #numNeeded, #description");
        let button = document.querySelector("#create-eventDisabled");
        button.disabled=true;

        for (let i = 0; i < inputs.length; i++){
            inputs[i].addEventListener('input', () => {
                let values = [];
                inputs.forEach(v => values.push(v.value));
                button.disabled = values.includes('');
            })
        }

        function getAll(selector) {
            return Array.prototype.slice.call(document.querySelectorAll(selector), 0)
        }

        let createPositionBtns = getAll(".createPosition");
        if (createPositionBtns.length > 0) {
            createPositionBtns.forEach(function (el, i) {
                el.addEventListener('click', function () {
                    el.classList.add('is-loading');
                })
            })
        }
    })
})();