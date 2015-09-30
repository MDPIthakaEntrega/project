/**
 * Created by renl on 9/17/15.
 */

var React = require('react');
var $ = require('jquery');

var CityGridReviewCard = React.createClass({
    render: function () {
        return (
            <div className="card">
                <h5>{this.props.title}</h5>
                <img src="/static/img/citygrid.png" className="img-responsive citygrid_icon" alt="" />
                <div className="card_content">
                    {this.props.content}
                </div>
                <div className="time">
                    <p>{this.props.date}</p>
                </div>
            </div>
        );
    }
});

var TweetCard = React.createClass({
    render: function () {
        return (
            <div className="card">
                <img src="img/twitter.png" className="img-responsive icon" alt="" />
                <div className="card_content">
                    I'm wearing striped knee socks and a blanket tied around my neck like a cape... Definitely turning 32 in 3 days.
                </div>
                <div className="time">
                    <p>{this.props.date}</p>
                </div>
            </div>
        );
    }
});

var YelpReviewCard = React.createClass({
    render: function () {
        return (
            <div className="card">
                <img src="/static/img/yelp.png" className="img-responsive icon" alt="" />
                <div className="card_content">
                    {this.props.content}
                </div>
                <div className="time">
                    <p>{this.props.date}</p>
                </div>
            </div>
        );
    }
});

module.exports = {
    CityGridReviewCard: CityGridReviewCard,
    YelpReviewCard: YelpReviewCard,
    TweetCard: TweetCard
};
