"use strict";
(function() {
    $(document).ready(function () {
        let html = document.querySelector('html');
        let allModals = getAll('.modal');
        let modalButtons = getAll('.open-modal');
        let modalCloses = getAll('.modal-background, .close');

        if (modalButtons.length > 0) {
            modalButtons.forEach(function (el, i) {
                el.addEventListener('click', function () {
                    let target = allModals[i];
                    console.log(target);
                    html.classList.add('is-clipped');
                    target.classList.add('is-active');
                });
            });
        }

        if (modalCloses.length > 0) {
            modalCloses.forEach(function (el) {
                el.addEventListener('click', function () {
                    closeModals();
                });
            });
        }

        document.addEventListener('keydown', function (event) {
            let e = event || window.event;
            if (e.keyCode === 27) {
                closeModals();
            }
        });

        function closeModals() {
            html.classList.remove('is-clipped');
            allModals.forEach(function (el) {
                el.classList.remove('is-active');
            });
        }

        function getAll(selector) {
            return Array.prototype.slice.call(document.querySelectorAll(selector), 0)
        }
    })
})();