const calOptions = {
    type: 'datetime',
    color: 'primary',
    isRange: true,
    allowSameDayRange: true,
    lang: navigator.language.substring(0, 2) || "en",
    dateFormat: 'MM/DD/YYY',
    timeFormat: 'HH:mm',
    displayMode: 'default',
    position: 'auto',
    showHeader: true,
    headerPosition: 'top',
    showFooter: true,
    showButtons: true,
    showTodayButton: true,
    showClearButton: true,
    cancelLabel: 'Cancel',
    clearLabel: 'Clear',
    todayLabel: 'Today',
    nowLabel: 'Now',
    validateLabel: 'Validate',
    enableMonthSwitch: true,
    enableYearSwitch: true,
    minDate: '2018-01-01',
    maxDate: '9999-12-31',
    minuteSteps: '1',
    closeOnOverlayClick: true,
    closeOnSelect: true,
    toggleOnInputClick: true,
    onReady: null
};
// Initialize all input of date type.
const calendars = bulmaCalendar.attach('[type="date"]', calOptions);

// Loop on each calendar initialized
calendars.forEach(calendar => {
    // Add listener to date:selected event
    calendar.on('date:selected', date => {
        console.log(date);
    });
});

// To access to bulmaCalendar instance of an element
const element = document.querySelector('#cal');
if (element) {
    // bulmaCalendar instance is available as element.bulmaCalendar
    element.bulmaCalendar.on('select', datepicker => {
        let dateString = datepicker.data.value().replace(/Y/g,'');
        let array = dateString.split(" - ");
        document.getElementById('start').value = array[0];
        document.getElementById('stop').value = array[1];
        document.getElementById('end').value = array[1];
        console.log(array);
        console.log(datepicker.data.value());
    });
}