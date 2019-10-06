"use strict";
(function(){
    $(document).ready(function(){
        function checkAll(source) {
            let checkboxes = document.getElementsByName('check');
            for(let i=0; i < checkboxes.length ; i++) {
                checkboxes[i].checked = source.checked;
            }
        }

        function getAll(selector) {
            return Array.prototype.slice.call(document.querySelectorAll(selector), 0)
        }

        let volunteerBtns = getAll(".changeBtn");
        if (volunteerBtns.length > 0) {
            volunteerBtns.forEach(function (el, i) {
                el.addEventListener('click', function () {
                    el.classList.add('is-loading');
                })
            })
        }
    })
})();