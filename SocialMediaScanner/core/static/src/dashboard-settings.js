/**
 * Created by renl on 9/23/15.
 */
var React = require('react');
var Grid = require('react-bootstrap').Grid;
var ConfigAccount = require('./config-account');
var ConfigChart = require('./config-chart');

var Settings = React.createClass({
    render: function () {
        return (
            <div>
                <ConfigAccount></ConfigAccount>
                <ConfigChart></ConfigChart>
            </div>
        );
    }
});

module.exports = Settings;
