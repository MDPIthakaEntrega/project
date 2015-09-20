/**
 * Created by Emily on 9/20/15.
 */
/**
 * Created by Emily on 9/16/15.
 */
var React = require('react');
//var $ = require('jquery');

var SentimentPieChart = React.createClass({
    getInitialState: function() {
        return {
            pie_options: {
                series: {
                    pie: {
                        show: true
                    }
                },
                legend: {
                    show: false
                }
            }
        };
    },
    render: function() {
        $.plot($("#flot-piechart"), this.props.data, this.state.pie_options);
        return(
            <div id="flot-piechart" style="width:500px;height:500px" />
        )
    }
});

var TimeChart = React.createClass({
    getInitialState: function () {
        return {
            time_options: {
                series: {
                    lines: {show: false},
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
            }
        };
    },
    slice: function (from, to) {
        var res = [];
        for (i = 0; i < this.props.data.length; i++) {
            if (this.props.data[i][0] > from && this.props.data[i][0]) {
                res.push(this.props.data[i]);
            }
        }
        return res;
    },
    render: function () {
        var plot = $.plot("#flot-timeseries", [{data: this.props.data}], this.state.time_options);

        // Create the overview plot
        var overview = $.plot("#overview", [{data: this.props.data}], time_options);

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
            var temp = this.slice(ranges.xaxis.from, ranges.xaxis.to);
            console.log(temp);
            plot = $.plot("#flot-timeseries", [{data: this.slice(ranges.xaxis.from, ranges.xaxis.to)}],
                $.extend(true, {}, time_options, {
                    xaxis: {min: ranges.xaxis.from, max: ranges.xaxis.to},
                    yaxis: {min: ranges.yaxis.from, max: ranges.yaxis.to}
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

        return (
            <div class="demo-container">
                <div id="flot-timeseries" class="demo-placeholder" style="float:left; width:650px;height:500px"></div>
                <div id="overview" class="demo-placeholder" style="float:right;width:160px; height:125px;"></div>
            </div>
        )
    }
});

var DashboardPlotApp = React.createClass({
    getInitialState: function() {
        return {
            pie_data: [{label: "Positive", data: 1, color: "#4572A7"},
                {label: "Negative", data: 1, color: "#AA4643"},
                {label: "Neutral", data: 1, color: "#80699B"}],
            time_data: [],
            allData: [],
            numberOfData: -1,
            workable: false,
            positive: 0,
            negative: 0,
            neutral: 0
        };
    },
    calculateAnalytics: function () {
        this.setState({
            neutral: 0,
            positive: 0,
            negative: 0
        });
        var date_dict = {};
        if (this.state.workable) {
            for (i = 0; i < this.state.numberOfData; ++i) {
                var type = this.state.allData[i].sentiment_type;
                var date = this.state.allData[i].review_date;

                // Count Sentiments
                if (type == 'negative') {
                    this.setState({negative: this.state.negative++});
                }
                else if (type == 'positive') {
                    this.setState({positive: this.state.positive++});
                }
                else if (type == 'neutral') {
                    this.setState({neutral: this.state.neutral++});
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

        this.setState({
            pie_data: [{label: "Positive", data: this.state.positive, color: "#4572A7"},
                {label: "Negative", data: this.state.negative, color: "#AA4643"},
                {label: "Neutral", data: this.state.neutral, color: "#80699B"}]
        });

        for (var key in date_dict) {
            if (date_dict.hasOwnProperty(key)) {
                curr_date = key;
                var count = date_dict[key];
                var curr_time_data = this.state.time_data;
                var new_time_data = curr_time_data.push([curr_date, count]);
                this.setState({time_data: new_time_data});
            }
        }
    },
    retrieveAllData: function() {
        $.ajax({
            type: 'GET',
            url: '/static/data.json',
            xhrFields: {
                withCredentials: false
            },
            dataType: 'json',
            success: function (data) {
                this.setState({
                    allData: data['reviews'],
                    numberOfData: allData.length,
                    workable: true
                });
                this.calculateAnalytics();
                console.log(this.state.time_data);
            },
            error: function (xhr, status, err) {
                console.error(xhr, status, err.toString());
            }
        });
    },
    render: function() {
        return(
            <div>
                Sentiment Pie Chart
                <SentimentPieChart data={this.state.pie_data}/>
                Number of Reviews by Month
                <TimeChart data={this.state.time_data}/>
            </div>
        );
    }
});

module.exports = DashboardPlotApp;