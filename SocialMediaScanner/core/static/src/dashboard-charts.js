/**
 * Created by Emily on 9/20/15.
 */
var React = require('react');
var Grid = require('react-bootstrap').Grid;
var Col = require('react-bootstrap').Col;
var Row = require('react-bootstrap').Row;
var TimeChart = require('./dashboard-charts-timechart').TimeChart;
var SentimentPieChart = require('./dashboard-charts-piechart').SentimentPieChart;
var RatingBarChart = require('./dashboard-charts-barchart').RatingBarChart;


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
            workable: true,
            positive: 0,
            negative: 0,
            neutral: 0,
            initialized: false
        };
    },
    calculateAnalytics: function (data) {
        this.setState({
            neutral: 0,
            positive: 0,
            negative: 0
        });
        var date_dict = {};
        var rating_dict = {0: 0, 1: 0, 2: 0, 3: 0, 4: 0, 5: 0};
        var sentiment_result = {
            negative: 0,
            positive: 0,
            neutral: 0
        };
        for (var i = 0; i < data.length; ++i) {
            var sentiment = data[i].sentiment_feeling;
            var rating = data[i].sentiment_score;
            var date = data[i].date;
            sentiment_result[sentiment] += 1;
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
        this.setState(sentiment_result);
        var pie_data = [{label: "Positive", data: sentiment_result.positive, color: "#4572A7"},
            {label: "Negative", data: sentiment_result.negative, color: "#AA4643"},
            {label: "Neutral", data: sentiment_result.neutral, color: "#80699B"}];

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

    componentDidMount: function () {
        var tempData = this.props.reviews;
        this.setState({
            allData: tempData,
            numberOfData: tempData.length
        });
        var chart_data = this.calculateAnalytics(tempData);

        this.setState({
            pie_data: chart_data[0],
            time_data: chart_data[1],
            ratingBarChartData: chart_data[2],
            initialized: true
        });
    },

    filterChart: function (chart_config) {
        var result = [];
        for (var chartType in chart_config) {
            if (chart_config[chartType]) result.push(chartType);
        }
        return result;
    },

    render: function () {
        var filtered_chart = this.filterChart(this.props.chart_config, this.props.charts);
        var selected_charts = filtered_chart.map((chartType) => {
            switch (chartType) {
                case 'bar_chart_ratings':
                    return (
                        <Col xs={6} className="ChartSection">
                            <h1 ref="barchart">{this.props.charts[chartType]}</h1>
                            <RatingBarChart data={this.state.ratingBarChartData} init={this.state.initialized}/>
                        </Col>
                    );
                case 'num_reviews_by_month':
                    return (
                        <Col xs={6} className="ChartSection">
                            <h1 ref="timechart">{this.props.charts[chartType]}</h1>
                            <TimeChart data={this.state.time_data} init={this.state.initialized}/>
                        </Col>
                    );
                case 'sentiment_pie_chart':
                    return (
                        <Col xs={6} className="ChartSection">
                            <h1 ref="piechart">{this.props.charts[chartType]}</h1>
                            <SentimentPieChart data={this.state.pie_data} init={this.state.initialized}/>
                        </Col>
                    );
            }
        });
        return (
            <div style={{padding: '20px'}}>
                <Grid>
                    <Row>
                        {selected_charts}
                    </Row>
                </Grid>
            </div>
        );
    }
});

module.exports = DashboardPlotApp;