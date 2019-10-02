"use strict";
document.addEventListener('DOMContentLoaded', () => {
    (document.querySelectorAll('.notification .delete') || []).forEach(($delete) => {
        let $notification;
        $notification = $delete.parentNode;
        $delete.addEventListener('click', () => {
            $notification.parentNode.style.display="none";
        });
    });
});