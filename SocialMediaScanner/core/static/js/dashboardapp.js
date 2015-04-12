/**
 * Created by renl on 3/24/15.
 */

var allData = [];
var newData = [];
var pageData = [];
var numberOfData = -1;
var workable = false;
var modifiable = false;
var reviewPerpage = 10;
var totalPagesNum = 0;

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


function retrieveAllData() {
    $.ajax({
        port: 3456,
        type: 'GET',
        //url: '/static/data.json',
        xhrFields: {
            // The 'xhrFields' property sets additional fields on the XMLHttpRequest.
            // This can be used to set the 'withCredentials' property.
            // Set the value to 'true' if you'd like to pass cookies to the server.
            // If this is enabled, your server must respond with the header
            // 'Access-Control-Allow-Credentials: true'.
            withCredentials: false
        },
        crossDomain: true,
        url: 'http://35.2.156.78:3456/search?company%20name=zingerman%27s&keyword=',
        dataType: 'json',
        success: function (data) {
            allData = data['reviews'];
            console.log(allData.length);
            pageData = allData;
            totalPagesNum = Math.ceil(pageData.length / reviewPerpage);
            console.log("pageData length");
            console.log();
            numberOfData = allData.length;
            workable = true;
            Pagination(totalPagesNum);
            calculateAnalytics();
            renderAllReviews();
            setupSearchListener();
        },
        error: function (xhr, status, err) {
            console.error(xhr, status, err.toString());
        }
    });
}

//this function will add a listener to the confirm button
//all the checked new reviews will disappear, while the data on the server will change later
//the data on the server will change by the function "modifyServerData()"
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


//this function will post the list of new reviews that are required to be remove
//the list is one based instead of zero based, be careful
function modifyServerData(removeList) {
    var username = $("#usr").text();
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

function calculateAnalytics() {
    var neutral = 0, positive = 0, negative = 0;
    if(workable) {
        for(i = 0; i < numberOfData; ++i) {
            var type = allData[i].sentiment;
            if (type == 'negative') {
                negative++;
            }
            else if (type == 'positive') {
                positive++;
            }
            else if (type == 'neutral') {
                neutral++;
            }
        }
    }
    $("#pos_review_num").html(positive);
    $("#neu_review_num").html(neutral);
    $("#neg_review_num").html(negative);
}

function setupSearchListener() {
   $("#filter").keyup(function () {
        var filter = $(this).val();
        if (workable) {
            var words = filter.split(" ");
            var totalWords = words.length;
            var rankMap = {};
            var output = '';
            for (i = 0; i < numberOfData; i++) {
                var str = allData[i].content;
                var count = 0;
                for (j = 0; j < totalWords; j++) {
                    if (str.search(words[j]) != -1) {
                        count++;
                    }
                }
                if (count != 0) {
                    if (!(count in rankMap)) {
                        rankMap[count] = [];
                    }
                    rankMap[count].push(i);
                }
            }
            pageData = []
            for (m = totalWords; m > 0; m--) {
                if (m in rankMap) {
                    var positionList = rankMap[m];
                    for (n = 0; n < positionList.length; n++) {
                        pageData.append(allData[positionList[n]]);
                        /*output += '<div class="panel panel-default"><div class="panel-heading"><h3 class="panel-title">';
                        output += allData[positionList[n]].title;
                        output += '</div><div class="panel-body">';
                        output += allData[positionList[n]].content;
                        output += '</div></div>';*/
                    }
                }
            }
            $('#results').html(output);
        }
    });
}

function renderAllReviews() {
    var output = '';
    for (n = 0; n < Math.min(allData.length, reviewPerpage); n++) {
        output += '<div class="panel panel-default"><div class="panel-heading"><h3 class="panel-title">';
        output += allData[n].title;
        output += '</div><div class="panel-body">';
        output += allData[n].content;
        output += '</div></div>';
    }
    $('#results').html(output);
}

function renderReviews(inputData) {
   var output = '';
    for (n = 0; n < inputData.length; n++) {
        output += '<div class="panel panel-default"><div class="panel-heading"><h3 class="panel-title">';
        output += inputData[n].title;
        output += '</div><div class="panel-body">';
        output += inputData[n].content;
        output += '</div></div>';
    }
    $('#results').html(output);
}

$(document).ready(function () {
    setupCSRF();
    retrieveAllData();
    confirmReading();
});

function slice(page) {
    var slicedData = pageData.slice((page-1) * reviewPerpage, (page) * reviewPerpage);
    return slicedData;
}

function Pagination(total) {
    $('#pagination-demo').twbsPagination({
        data: pageData,
        totalPages: total,
        visiblePages: 5,
        onPageClick: function (event, page) {
            var temp = slice(page);
            renderReviews(temp);
        }
    });
}


/**
 * Created by renl on 4/5/15.
 */
