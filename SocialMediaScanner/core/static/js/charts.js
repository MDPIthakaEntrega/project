var allData = [];
var numberOfData = -1;
var workable = false;

var data = [
    [1, 130], [2, 40], [3, 80], [4, 160], [5, 159], [6, 370],
    [7, 330], [8, 350], [9, 370], [10, 400], [11, 330], [12, 350]
];

var pie_data = [
    { label: "Positive",  data: 51, color: "#4572A7"},
    { label: "Negative",  data: 13, color: "#AA4643"},
    { label: "Neutral",  data: 2, color: "#80699B"}
];

var pie_options = {
    series: {
        pie: {
            show: true
        }
    },
    legend: {
        show: false
    }
};

var line_data = [
    {
        label: "line1",
        data: data
    }
];

var line_options = {
    series: {
        lines: { show: true },
        points: {
            radius: 3,
            show: true
        }
    }
};

function calculateAnalytics() {
    var neutral = 0, positive = 0, negative = 0;
    if(workable) {
        for(i = 0; i < numberOfData; ++i) {
            var type = allData[i].sentiment_type;
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
    pie_data[0].data = positive;
    pie_data[1].data = negative;
    pie_data[2].data = neutral;

}

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
            console.log("pageData length");
            numberOfData = allData.length;
            calculateAnalytics();
            workable = true;
        },
        error: function (xhr, status, err) {
            console.error(xhr, status, err.toString());
        }
    });
}

$(document).ready(function () {
    retrieveAllData();
    $.plot($("#flot-linechart"), line_data, line_options);
    $.plot($("#flot-piechart"), pie_data, pie_options);

});