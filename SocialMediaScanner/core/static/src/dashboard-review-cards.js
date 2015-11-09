/**
 * Created by renl on 9/17/15.
 */

var React = require('react');
var $ = require('jquery');

var CityGridReviewCard = React.createClass({
    render: function () {
        return (
            <div className="card">
                <h5 ref="citygridtitle">{this.props.title}</h5>
                <img src="/static/img/citygrid.png" className="img-responsive citygrid_icon" alt="" />
                <div className="card_content" ref="citygridcontent">
                    {this.props.content}
                </div>
                <div className="time" ref="citygriddate">
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
                <img src="/static/img/twitter.png" className="img-responsive icon" alt="" />
                <div className="card_content" ref="twittercontent">
                    {this.props.content}
                </div>
                <div className="time" ref="twitterdate">
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
                <div className="card_content" ref="yelpcontent">
                    {this.props.content}
                </div>
                <div className="time" ref="yelpdate">
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
