var React = require('react');

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
    },
    render: function () {
        if (this.props.init) {
            this.setChartData();
        }
        return (
            <div id="flot-piechart" style={{"width": "350px", "height": "350px"}} />
        )
    }
});

module.exports = {
    SentimentPieChart: SentimentPieChart
};