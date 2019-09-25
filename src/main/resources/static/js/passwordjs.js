let passwordConfirm = document.querySelector('#passwordConfirm');
let password = document.querySelector('#password');

passwordConfirm.addEventListener('keyup', function () {passCheck()});
password.addEventListener('keyup', function () {passCheck()});


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