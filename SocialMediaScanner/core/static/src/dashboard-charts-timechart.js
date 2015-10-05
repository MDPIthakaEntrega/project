var React = require('react');

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
                    "width": "425px",
                    "height": "350px"
                }}></div>
                <div id="overview" className="demo-placeholder" style={{
                    "float": "left",
                    "width": "1000px",
                    "height": "80px"
                }}></div>
            </div>
        )
    }
});

module.exports = {
    TimeChart: TimeChart
};