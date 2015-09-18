/**
 * Created by renl on 9/17/15.
 */

var React = require('react');
var $ = require('jquery');

var CityGridReviewCard = React.createClass({
    render: function () {
        return (
            <div style={{paddingTop: '20px'}}>
                <h4>{this.props.title}</h4>
                <p>{this.props.content}</p>
            </div>
        );
    }
});

module.exports = {
    CityGridReviewCard: CityGridReviewCard
};
