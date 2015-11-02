/**
 * Created by renl on 9/16/15.
 */

var React = require('react');
var $ = require('jquery');
var ReviewDivision = require('./dashboard-summary-review-division');
var ReviewPick = require('./dashboard-summary-review-pick');
var Grid = require('react-bootstrap').Grid;
var Row = require('react-bootstrap').Row;
var Col = require('react-bootstrap').Col;

var Summary = React.createClass({
    calculateDivision: function (data) {
        var result = {
            positive: 0,
            negative: 0,
            neutral: 0
        };
        for (var i in data) {
            result[data[i].sentiment_feeling] += 1;
        }
        return result;
    },

    render: function () {
        var sentiment_result = this.calculateDivision(this.props.reviews);
        if (this.props.error_message) {
            return (
                <div>{this.props.error_message}</div>
            );
        }
        return (
            <div>
                <ReviewDivision {...sentiment_result} />
                <ReviewPick data={this.props.reviews} total={10} />
            </div>
        );
    }
});

module.exports = Summary;