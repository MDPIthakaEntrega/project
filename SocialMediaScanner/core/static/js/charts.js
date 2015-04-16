var allData = [];
var numberOfData = -1;
var workable = false;

var positive = 0;
var negative = 0;
var neutral = 0;


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

//time_data = [[1325347200000, 60], [1328025600000, 100], [1330531200000, 15], [1333209600000, 50]];
var time_data = [];

var time_options = {
    series: {
        lines: { show: false},
        bars: {
            show: true
        }
    },
    yaxis: {},
    xaxis: {
        mode: "time",
        timeformat: "%m/%y",
        minTickSize: [1, "month"]
    },
    selection: {
        mode: "xy"
    }
};



function calculateAnalytics() {
    neutral = 0, positive = 0, negative = 0;
    var date_dict = {};
    if(workable) {
        for(i = 0; i < numberOfData; ++i){
            var type = allData[i].sentiment_type;
            var date = allData[i].review_date;

            // Count Sentiments
            if (type == 'negative') {
                negative++;
            }
            else if (type == 'positive') {
                positive++;
            }
            else if (type == 'neutral') {
                neutral++;
            }

            //Get Date counts
            date = new Date(date);
            var month = date.getMonth();
            var year = date.getYear();

            var curr_date = Date.UTC(year, month);

            if (curr_date in date_dict) {
                date_dict[curr_date] += 1;
            }
            else {
                date_dict[curr_date] = 1;
            }

        }
    }
    pie_data[0].data = positive;
    pie_data[1].data = negative;
    pie_data[2].data = neutral;

    for (var key in date_dict) {
        if (date_dict.hasOwnProperty(key)) {
            curr_date = key;
            var count = date_dict[key];
            time_data.push([curr_date, count]);
        }
    }

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
            console.log(time_data);
            $.plot($("#flot-piechart"), pie_data, pie_options);
            //$.plot($("#flot-timeseries"), [{data: time_data}], time_options);



            var plot = $.plot("#flot-timeseries", [{data: time_data}], time_options);

            // Create the overview plot

            var overview = $.plot("#overview", [{data: time_data}], time_options);

            // now connect the two

            $("#flot-timeseries").bind("plotselected", function (event, ranges) {

                // clamp the zooming to prevent eternal zoom

                if (ranges.xaxis.to - ranges.xaxis.from < 0.00001) {
                    ranges.xaxis.to = ranges.xaxis.from + 0.00001;
                }

                if (ranges.yaxis.to - ranges.yaxis.from < 0.00001) {
                    ranges.yaxis.to = ranges.yaxis.from + 0.00001;
                }

                // do the zooming
                var temp = slice(time_data, ranges.xaxis.from, ranges.xaxis.to);
                console.log(temp);
                plot = $.plot("#flot-timeseries", [{data: slice(time_data, ranges.xaxis.from, ranges.xaxis.to)}],
                    $.extend(true, {}, time_options, {
                        xaxis: { min: ranges.xaxis.from, max: ranges.xaxis.to },
                        yaxis: { min: ranges.yaxis.from, max: ranges.yaxis.to }
                    })

                );

                // don't fire event on the overview to prevent eternal loop

                overview.setSelection(ranges, true);
            });

            $("#overview").bind("plotselected", function (event, ranges) {
                plot.setSelection(ranges);
            });

            // Add the Flot version string to the footer

            $("#footer").prepend("Flot " + $.plot.version + " &ndash; ");

        },
        error: function (xhr, status, err) {
            console.error(xhr, status, err.toString());
        }
    });
}

function slice(data, from, to) {
    var res = [];
    for( i = 0; i < data.length; i++) {
        if(data[i][0] > from && data[i][0]) {
            res.push(data[i]);
        }
    }
    return res;
}

$(document).ready(function () {
    retrieveAllData();
});