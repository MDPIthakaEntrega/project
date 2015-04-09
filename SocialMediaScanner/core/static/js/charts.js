var allData = [];
var pageData = [];
var numberOfData = -1;
var workable = false;
var reviewPerpage = 10;
var totalPagesNum = 0;

var data = [
    [1, 130], [2, 40], [3, 80], [4, 160], [5, 159], [6, 370],
    [7, 330], [8, 350], [9, 370], [10, 400], [11, 330], [12, 350]
];

var dataset = [
    {
        label: "line1",
        data: data
    }
];

var options = {
    series: {
        lines: { show: true },
        points: {
            radius: 3,
            show: true
        }
    }
};


function retrieveAllData() {
    $.ajax({
        type: 'GET',
        url: '/static/data.json',
        xhrFields: {
            // The 'xhrFields' property sets additional fields on the XMLHttpRequest.
            // This can be used to set the 'withCredentials' property.
            // Set the value to 'true' if you'd like to pass cookies to the server.
            // If this is enabled, your server must respond with the header
            // 'Access-Control-Allow-Credentials: true'.
            withCredentials: false
          },
        //url: 'http://35.2.138.121:3456/search?company%20name=zingerman%27s&keyword=',
        dataType: 'json',
        success: function (data) {
            console.log(data);
            allData = data['reviews'];
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

$(document).ready(function () {
            $.plot($("#flot-linechart"), dataset, options);
        });