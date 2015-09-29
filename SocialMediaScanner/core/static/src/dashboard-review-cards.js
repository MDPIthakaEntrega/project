/**
 * Created by renl on 9/17/15.
 */

var React = require('react');
var $ = require('jquery');

var CityGridReviewCard = React.createClass({
    render: function () {
        return (
            <div className="card">
                <div className="user">
                    <p className="name">Lane Collins
                        <span>@lane</span>
                    </p>
                </div>
                <img src="/static/img/citygrid.png" className="img-responsive citygrid_icon" alt="" />
                <div className="card_content">
                    {this.props.content}
                </div>
                <div className="time">
                    <p>9:12 PM - 6 Mar 2014</p>
                </div>
            </div>
        );
    }
});

var TweetCard = React.createClass({
    render: function () {
        return (
            <div className="card">
                <div className="user">
                    <img src="img/obama.jpg" alt="" />
                    <p className="name">Lane Collins
                        <span>@lane</span>
                    </p>
                </div>
                <img src="img/twitter.png" className="img-responsive twitter_icon" alt="" />
                <div className="card_content">
                    I'm wearing striped knee socks and a blanket tied around my neck like a cape... Definitely turning 32 in 3 days.
                </div>
                <div className="time">
                    <p>9:12 PM - 6 Mar 2014</p>
                </div>
            </div>
        );
    }
});

module.exports = {
    CityGridReviewCard: CityGridReviewCard
};
