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
        return {};
    },
    componentDidMount: function () {
    },
    handleClick: function () {
    },
    handleChange: function () {
    },

    render: function () {

        return (
            <div className="chartConfig">
                <Grid>
                    <Row>
                        <h2>
                            <Label bsStyle="primary">Charts Config</Label>
                        </h2>
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
