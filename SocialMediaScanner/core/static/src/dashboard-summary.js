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
    getInitialState: function () {
        return {
            data: []
        };
    },

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

    componentDidMount: function () {
        this.loadDataFromServer();
    },

    loadDataFromServer: function () {
        $.ajax({
            port: 3456,
            type: 'GET',
            crossDomain: true,
            //url: 'http://35.2.73.31:3456/search?company%20name=zingerman%27s&keyword=',
            url: 'http://localhost:3456/search?company%20name=zingerman%27s&keyword=',
            //url: '/static/search.json',
            dataType: 'json',
            success: function (data) {
                var reviews = data.reviews;
                console.log(reviews);
                this.setState({
                    data: reviews
                });
                var result = this.calculateDivision(reviews);
                this.setState(result);
            }.bind(this),
            error: function (xhr, status, err) {
            }.bind(this)
        });
    },

    render: function () {
        if (this.state.data.hasOwnProperty('reviews')) {

        }
        return (
            <div>
                <ReviewDivision
                    positive={this.state.positive}
                    neutral={this.state.neutral}
                    negative={this.state.negative}/>
                <ReviewPick data={this.state.data} total={10} />
            </div>
        );
    }
});

module.exports = Summary;