"use strict";

$('.pastEvents').on('click', function () {
    $('.pastEventDiv').css('display', 'inline');
    $('.plannedEventDiv').css('display', 'none');
    $('.plannedCreatedEventDiv').css('display', 'none');
    $('.pastCreatedEventDiv').css('display', 'none');
});
$('.plannedEvents').on('click', function () {
    $('.pastEventDiv').css('display', 'none');
    $('.plannedEventDiv').css('display', 'inline');
    $('.plannedCreatedEventDiv').css('display', 'none');
    $('.pastCreatedEventDiv').css('display', 'none');
});
$('.plannedEventsCreated').on('click', function () {
    $('.pastEventDiv').css('display', 'none');
    $('.plannedEventDiv').css('display', 'none');
    $('.plannedCreatedEventDiv').css('display', 'inline');
    $('.pastCreatedEventDiv').css('display', 'none');
});
$('.pastEventsCreated').on('click', function () {
    $('.pastEventDiv').css('display', 'none');
    $('.plannedEventDiv').css('display', 'none');
    $('.plannedCreatedEventDiv').css('display', 'none');
    $('.pastCreatedEventDiv').css('display', 'inline');
});

$('li.tab:first').addClass('is-active');
$('li.tab').click(function() {
    $('li.tab').removeClass('is-active');
    $(this).addClass('is-active');
    return true;
});
