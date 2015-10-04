var React = require('react');

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
        if (this.props.init) {
            this.setChartData();
        }
        return (
            <div id="flot-ratingbarchart" style={{"width": "250px", "height": "250px"}} />
        )
    }
});

module.exports = {
    RatingBarChart: RatingBarChart
};