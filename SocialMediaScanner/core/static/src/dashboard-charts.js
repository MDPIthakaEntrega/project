/**
 * Created by Emily on 9/20/15.
 */
var React = require('react');
var Grid = require('react-bootstrap').Grid;
var Col = require('react-bootstrap').Col;
var Row = require('react-bootstrap').Row;

var RatingBarChart = React.createClass({
    getInitialState: function () {
        return {
            options: {
                xaxis: {
                    ticklength: 0,
                    ticks: [[0, "0"], [1, "1"], [2, "2"], [3, "3"], [4, "4"], [5, "5"]]
                }
            }
        }
    },
    setChartData: function () {
        $.plot($("#flot-ratingbarchart"), [
            {
                data: this.props.data,
                bars: {
                    show: true,
                    align: "center",
                    barWidth: 0.5
                }
            }
        ], this.state.options);
    },
    componentDidMount: function () {
        this.setChartData();
    },
    render: function () {
        console.log(this.props.data);
        if (this.props.init) {
            this.setChartData();
        }
        return (
            <div id="flot-ratingbarchart" style={{"width": "500px", "height": "500px"}} />
        )
    }
});

var SentimentPieChart = React.createClass({
    getInitialState: function () {
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
    setChartData: function () {
        $.plot($("#flot-piechart"), this.props.data, this.state.pie_options);
    },
    componentDidMount: function () {
        this.setChartData();
        console.log(this.props.data);
    },
    render: function () {
        if (this.props.init) {
            this.setChartData();
        }
        return (
            <div id="flot-piechart" style={{"width": "500px", "height": "500px"}} />
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
    getDefaultProps: function () {
        return {
            data: []
        }
    },
    setChartData: function () {
        var plot = $.plot("#flot-timeseries", [{data: this.props.data}], this.state.time_options);

        // Create the overview plot
        var overview = $.plot("#overview", [{data: this.props.data}], this.state.time_options);
        var tempdata = this.props.data;
        var temp_options = this.state.time_options;

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
            var temp = [];
            for (i = 0; i < tempdata.length; i++) {
                if (tempdata[i][0] > ranges.xaxis.from && tempdata[i][0]) {
                    temp.push(tempdata[i]);
                }
            }
            console.log(temp);
            plot = $.plot("#flot-timeseries", [{data: temp}],
                $.extend(true, {}, temp_options, {
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
        console.log(this.props.data);
    },
    componentDidMount: function () {
        this.setChartData();
    },
    render: function () {
        if (this.props.init) {
            this.setChartData();
        }
        return (
            <div className="demo-container">
                <div id="flot-timeseries" className="demo-placeholder" style={{
                    "float": "left",
                    "width": "650px",
                    "height": "500px"
                }}></div>
                <div id="overview" className="demo-placeholder" style={{
                    "float": "right",
                    "width": "160px",
                    "height": "125px"
                }}></div>
            </div>
        )
    }
});

var DashboardPlotApp = React.createClass({
    getInitialState: function () {
        return {
            pie_data: [
                {label: "Positive", data: 1, color: "#4572A7"},
                {label: "Negative", data: 1, color: "#AA4643"},
                {label: "Neutral", data: 1, color: "#80699B"}
            ],
            time_data: [],
            ratingBarChartData: [[0, 0], [1, 0], [2, 0], [3, 0], [4, 0], [5, 0]],
            allData: [],
            numberOfData: -1,
            workable: false,
            positive: 0,
            negative: 0,
            neutral: 0,
            initialized: false
        };
    },
    calculateAnalytics: function () {
        this.setState({
            neutral: 0,
            positive: 0,
            negative: 0
        });
        var date_dict = {};
        var rating_dict = {0: 0, 1: 0, 2: 0, 3: 0, 4: 0, 5: 0};
        if (this.state.workable) {
            for (i = 0; i < this.state.numberOfData; ++i) {
                var sentiment = this.state.allData[i].sentiment_feeling;
                var rating = this.state.allData[i].sentiment_score;
                var date = this.state.allData[i].date;

                // Count Sentiments
                if (sentiment == 'negative') {
                    this.setState({negative: this.state.negative + 1});
                }
                else if (sentiment == 'positive') {
                    this.setState({positive: this.state.positive + 1});
                }
                else if (sentiment == 'neutral') {
                    this.setState({neutral: this.state.neutral + 1});
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

                //Get Ratings Counts
                rating = Math.round(((rating + 1) / 2) * 5);
                rating_dict[rating] += 1;


            }
        }
        var pie_data = [{label: "Positive", data: this.state.positive, color: "#4572A7"},
            {label: "Negative", data: this.state.negative, color: "#AA4643"},
            {label: "Neutral", data: this.state.neutral, color: "#80699B"}];

        var time_data = [];
        for (var key in date_dict) {
            if (date_dict.hasOwnProperty(key)) {
                curr_date = key;
                var count = date_dict[key];
                time_data.push([curr_date, count]);
            }
        }

        var ratingBarChartData = [[0, rating_dict[0]], [1, rating_dict[1]], [2, rating_dict[2]], [3, rating_dict[3]], [4, rating_dict[4]], [5, rating_dict[5]]];
        return [pie_data, time_data, ratingBarChartData];
    },
    retrieveAllData: function () {
        $.ajax({
            port: 3456,
            type: 'GET',
            crossDomain: true,
            //url: 'http://35.2.73.31:3456/search?company%20name=zingerman%27s&keyword=',
            //url: 'http://localhost:3456/search?company%20name=zingerman%27s&keyword=',
            url: '/static/search.json',
            dataType: 'json',
            success: function (data) {
                var tempData = data['reviews'];
                this.setState({
                    allData: tempData,
                    numberOfData: tempData.length,
                    workable: true
                });
                var chart_data = this.calculateAnalytics();

                this.setState({
                    pie_data: chart_data[0],
                    time_data: chart_data[1],
                    ratingBarChartData: chart_data[2],
                    initialized: true
                });
                console.log(this.state.pie_data);
                console.log(this.state.time_data.length);
                console.log(this.state.ratingBarChartData);
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(xhr, status, err.toString());
            }.bind(this)
        });
    },
    componentDidMount: function () {
        this.retrieveAllData();
    },
    render: function () {
        return (
            <div style={{padding: '20px'}}>
                <Grid>
                    <Row>
                        <Col xs={6}>
                            Sentiment Pie Chart
                            <SentimentPieChart data={this.state.pie_data} init={this.state.initialized}/>
                        </Col>
                        <Col xs={6}>
                            Bar Chart Ratings
                            <RatingBarChart data={this.state.ratingBarChartData} init={this.state.initialized}/>
                        </Col>
                        <Col xs={10} xsOffset={1}>
                            Number of Reviews by Month
                            <TimeChart data={this.state.time_data} init={this.state.initialized}/>
                        </Col>
                    </Row>
                </Grid>
            </div>
        );
    }
});

module.exports = DashboardPlotApp;