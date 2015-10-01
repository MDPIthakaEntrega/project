/**
 * Created by renl on 9/28/15.
 */
/**
 * Created by renl on 9/23/15.
 */
var React = require('react');
var Input = require('react-bootstrap').Input;
var Grid = require('react-bootstrap').Grid;
var Col = require('react-bootstrap').Col;
var Row = require('react-bootstrap').Row;
var Label = require('react-bootstrap').Label;
var Button = require('react-bootstrap').Button;

var ChartConfig = React.createClass({
        getInitialState: function () {
        return {
            charts: {},
            chart_config: {}
        };
    },

    componentDidMount: function () {
        this.loadChartConfigsFromServer();
    },

    handleClick: function () {
        var configs = {};
        for (var chartType in this.refs) {
            configs[chartType] = this.refs[chartType].props.checked;
        }
        this.setState({
            charts: this.state.charts,
            chart_config: configs
        });
        $.ajax({
            type: 'POST',
            url: '/api/settings/',
            data: {type: 'chart', configs: JSON.stringify(configs)},
            success: function (data) {
            }.bind(this),
            error: function (xhr, status, err) {
            }.bind(this)
        });
    },
    // this function will update the config value when you trying to type in
    handleChange: function (chartType) {
        var chart_config = this.state.chart_config;
        chart_config[chartType] = !this.refs[chartType].props.checked;
        this.setState({
            chart_config: chart_config
        });
    },

    loadChartConfigsFromServer: function () {
        $.ajax({
            type: 'GET',
            url: '/api/settings/?part=chart',
            success: function (data) {
                this.setState({
                    charts: data.charts,
                    chart_config: JSON.parse(data.configs)
                });
            }.bind(this),
            error: function (xhr, status, err) {
            }.bind(this)
        });
    },

    render: function () {
        var chart_configs = Object.keys(this.state.charts).map((label) => {
            var is_checked = false;
            if (this.state.chart_config.hasOwnProperty(label)) {
                is_checked = this.state.chart_config[label];
            }
            return (
                <Col xs={4} md={2}>
                    <Input
                        type="checkbox"
                        label={this.state.charts[label]}
                        ref={label}
                        checked={is_checked}
                        onChange={this.handleChange.bind(this, label)}
                    />
                </Col>
            );
        });
        return (
            <div className="chartConfig">
                <Grid>
                    <Row>
                        <h2>
                            <Label bsStyle="primary">Charts Config</Label>
                        </h2>
                    </Row>
                    <Row>
                        {chart_configs}
                    </Row>
                    <Row>
                        <Col xs={4} md={2}>
                            <Button onClick={this.handleClick} bsStyle="info">Save</Button>
                        </Col>
                    </Row>
                </Grid>
            </div>
        );
    }
});

module.exports = ChartConfig;
