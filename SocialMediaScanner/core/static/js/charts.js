var allData = [];
var numberOfData = -1;
var workable = false;
var calculated = false;
var positive = 0;
var negative = 0;
var neutral = 0;

var data = [
    [1, 130], [2, 40], [3, 80], [4, 160], [5, 159], [6, 370],
    [7, 330], [8, 350], [9, 370], [10, 400], [11, 330], [12, 350]
];

var pie_data = [
    { label: "Positive",  data: 1, color: "#4572A7"},
    { label: "Negative",  data: 1, color: "#AA4643"},
    { label: "Neutral",  data: 1, color: "#80699B"}
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
    neutral = 0, positive = 0, negative = 0;
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
            withCredentials: false
          },
        dataType: 'json',
        success: function (data) {
            allData = data['reviews'];
            numberOfData = allData.length;
            workable = true;
            calculateAnalytics();

            $.plot($("#flot-piechart"), pie_data, pie_options);
        },
        error: function (xhr, status, err) {
            console.error(xhr, status, err.toString());
        }
    });
}

$(document).ready(function () {
    retrieveAllData();

});