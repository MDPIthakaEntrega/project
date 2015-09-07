/**
 * Created by renl on 3/24/15.
 */

var newData = [];

/*
* set up the CSRF to allow ajax call
* */
function setupCSRF() {
    var csrftoken = $.cookie('csrftoken');

    function csrfSafeMethod(method) {
        // these HTTP methods do not require CSRF protection
        return (/^(GET|HEAD|OPTIONS|TRACE)$/.test(method));
    }

    $.ajaxSetup({
        beforeSend: function (xhr, settings) {
            if (!csrfSafeMethod(settings.type) && !this.crossDomain) {
                xhr.setRequestHeader("X-CSRFToken", csrftoken);
            }
        }
    });
}


/*this function will add a listener to the confirm button
* all the checked new reviews will disappear, while the data on the server will change later
* the data on the server will change by the function "modifyServerData()"
* */
function confirmReading() {
    $("#review_confirm").click(function () {
        var listToRemove = [];
        var newMessageCount = 0;
        $("#new_reviews_body").find(".panel-info").each(function () {
            var checked = $(this).find(":checkbox").is(":checked");
            if (checked) {
                var local_id = parseInt($(this).find(".local_id").text());
                listToRemove.push(local_id);
                $(this).fadeOut();
            }
            else {
                newMessageCount++;
            }
        });
        $("#reviews_num").text(newMessageCount);
        modifyServerData(listToRemove);
    });
}


/*this function will post the list of new reviews that are required to be remove
* the list is one based instead of zero based, be careful
* */
function modifyServerData(removeList) {
    var username = $("#usr").text();
    console.log(username);
    $.ajax({
        url: '/static/new_review_cookie/' + username + '_cookies.json',
        dataType: 'json',
        success: function (data) {
            newData = data["new_reviews"];
            var temp = [];
            var cursor = 0;
            for (i = 0; i < newData.length; i++) {
                if (cursor == removeList.length || i != (removeList[cursor]-1)) {
                    temp.push(newData[i]);
                }
                else {
                    cursor++;
                }
            }

            var returnData = {"new_reviews" : JSON.stringify(temp)};
            $.ajax({
                url: "/dashboard/new_reviews",
                type: "POST",
                dataType: 'json',
                data: returnData,
                success: function (data, textStatus, jqXHR) {

                },
                error: function (jqXHR, textStatus, errorThrown) {

                }
            });
        },
        error: function (xhr, status, err) {
            console.error(xhr, status, err.toString());
        }
    });
}


$(document).ready(function () {

    setupCSRF();
    confirmReading();

});

/*this function is to do pagination*/
/**
 * Created by renl on 4/5/15.
 */
/**
 * Created by renl on 4/16/15.
 */