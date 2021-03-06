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
var $ = require('jquery');

var ChartConfig = React.createClass({
    updateSettings: function (configs) {
        $.ajax({
            type: 'POST',
            url: '/api/settings/',
            data: {type: 'chart', configs: JSON.stringify(configs)},
            success: function (data) {
                var noty = require('noty');
                this.props.setChartConfigFunction(configs);
                noty({
                    text: 'save chart config successfully',
                    layout: 'topRight',
                    type: 'success',
                    killer: true,
                    closeWith: ['click', 'hover']
                });
            }.bind(this),
            error: function (xhr, status, err) {
            }.bind(this)
        });
    },

    handleClick: function () {
        var configs = {};
        for (var chartType in this.refs) {
            configs[chartType] = this.refs[chartType].props.checked;
        }
        this.updateSettings(configs);
    },
    // this function will update the config value when you trying to type in
    handleChange: function (chartType) {
        var chart_config = this.props.chart_config;
        chart_config[chartType] = !this.refs[chartType].props.checked;
        this.props.setChartConfigFunction(chart_config);
    },

    render: function () {
        var chart_configs = Object.keys(this.props.charts).map((label, idx) => {
            var is_checked = false;
            if (this.props.chart_config.hasOwnProperty(label)) {
                is_checked = this.props.chart_config[label];
            }
            return (
                <Col xs={4} md={2} key={idx}>
                    <Input
                        type="checkbox"
                        label={this.props.charts[label]}
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