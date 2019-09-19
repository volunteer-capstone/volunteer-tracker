"use strict";
(function() {
    document.querySelector('a#openModal').addEventListener('click', function(event) {
        event.preventDefault();
        let modal = document.querySelector('.modal');  // assuming you have only 1
        let html = document.querySelector('html');
        modal.classList.add('is-active');
        html.classList.add('is-clipped');

        modal.querySelector('.modal-background').addEventListener('click', function(e) {
            e.preventDefault();
            modal.classList.remove('is-active');
            html.classList.remove('is-clipped');
        });
        modal.querySelector('.modal-close').addEventListener('click', function(e) {
            e.preventDefault();
            modal.classList.remove('is-active');
            html.classList.remove('is-clipped');
            });
        modal.querySelector('#cancel').addEventListener('click', function(e) {
            e.preventDefault();
            modal.classList.remove('is-active');
            html.classList.remove('is-clipped');
        });
    });
})();