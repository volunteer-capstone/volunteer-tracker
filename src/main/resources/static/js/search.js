
var input = '';
var posInput = '';

function searchEvents() {
    var eventList = $(".event");
    input = $('#eventSearch').val().toUpperCase();

    let posCheck = false;
    for (let i = 0; eventList.length > i; i++) {
        posCheck = false;
        var posList = $(eventList[i]).find(".positionDiv");
        if ($(eventList[i]).children().attr("class").toUpperCase().indexOf(input) > -1) {
            posCheck = true;
        } else {
            if ($('#posCheckbox').is(':checked')) {
                for (let j = 0; posList.length > j; j++) {
                    if ($(posList[j]).children().text().toUpperCase().indexOf(input) > -1) {
                        posCheck = true;
                    }
                }
            }
        }
        if (posCheck) {
            $(eventList[i]).css("display", "inline")
        } else {
            $(eventList[i]).css("display", "none")
        }
    }
    if (input === '') {
        if (posInput === '') {
            $(eventList).css("display", "inline")
        }
    }
}


