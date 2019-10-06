"use strict";
(function(){
    $(document).ready(function(){

        let passwordConfirm = document.querySelector('#passwordConfirm');
        let password = document.querySelector('#password');
        let phoneNum = document.querySelector('#phoneNumber');

        passwordConfirm.addEventListener('keyup', function () {passCheck()});
        password.addEventListener('keyup', function () {passCheck()});
        phoneNum.addEventListener('blur', function(){phoneNumber()});

        function passCheck() {
            let password = document.querySelector('#password').value;
            if (password.length < 6 || password.length > 20) {
                document.querySelector('#password2').style.display="inline";
            } else {
                document.querySelector('#password2').style.display="none";
            }
            if (passwordConfirm.value === password) {
                document.querySelector('#passwordConfirm2').style.display = "none";
            } else if (passwordConfirm.value !== password) {
                document.querySelector('#passwordConfirm2').style.display = "inline";
            }

            if (password.length < 6 || password.length > 20 || passwordConfirm.value !== password) {
                document.querySelector('#registerDisabled').style.display = "inline";
                document.querySelector('#register').style.display = "none";
                document.querySelector('#editDisabled').style.display = "inline";
                document.querySelector('#edit').style.display = "none";
            } else {
                document.querySelector('#registerDisabled').style.display = "none";
                document.querySelector('#register').style.display = "inline";
                document.querySelector('#editDisabled').style.display = "none";
                document.querySelector('#edit').style.display = "inline";
            }
        }

        function phoneNumber() {
            let number = document.querySelector('#phoneNumber').value;
            let phoneNo = /^\s*(?:\+?(\d{1,3}))?[-. (]*(\d{3})[-. )]*(\d{3})[-. ]*(\d{4})(?: *x(\d+))?\s*$/;
            if(number.match(phoneNo)) {
                document.querySelector('#phoneNumber2').style.display="none";
                document.querySelector('#registerDisabled').style.display = "none";
                document.querySelector('#register').style.display = "inline";
                document.querySelector('#editDisabled').style.display = "none";
                document.querySelector('#edit').style.display = "inline";
            } else {
                document.querySelector('#phoneNumber2').style.display="inline";
                document.querySelector('#registerDisabled').style.display = "inline";
                document.querySelector('#register').style.display = "none";
                document.querySelector('#editDisabled').style.display = "inline";
                document.querySelector('#edit').style.display = "none";
            }
        }

        let submit = document.querySelector('#register');
        submit.addEventListener('click', function() {
            submit.classList.add('is-loading');
        });

        let editSubmit = document.querySelector('#edit');
        editSubmit.addEventListener('click', function() {
            editSubmit.classList.add('is-loading');
        });

        let deleteSubmit = document.querySelector('#delete');
        deleteSubmit.addEventListener('click', function() {
            deleteSubmit.classList.add('is-loading');
        })

    })
})();