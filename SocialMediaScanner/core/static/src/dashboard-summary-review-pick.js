/**
 * Created by renl on 9/16/15.
 */

var React = require('react');
var shuffle = require('knuth-shuffle').knuthShuffle;
var Grid = require('react-bootstrap').Grid;
var Row = require('react-bootstrap').Row;
var Col = require('react-bootstrap').Col;
var CityGridReviewCard = require('./dashboard-review-cards').CityGridReviewCard;
var YelpReviewCard = require('./dashboard-review-cards').YelpReviewCard;
var TweetCard = require('./dashboard-review-cards').TweetCard;

var SummaryReviewPick = React.createClass({
    render: function () {
        var total = this.props.total;
        var randomReviews = shuffle(this.props.data.slice()).slice(0, total);
        var reviewCards = randomReviews.map(function (review, idx) {
            switch (review.source) {
                case 'Citygrid':
                    return (
                        <div className="item" key={idx}>
                            <CityGridReviewCard
                                title={review.title}
                                content={review.content}
                                date={review.date}
                            />
                        </div>
                    );
                case 'ImportMagicYelp':
                    return (
                        <div className="item" key={idx}>
                            <YelpReviewCard
                                title={review.title}
                                content={review.content}
                                date={review.date}
                            />
                        </div>
                    );
                case 'Twitter':
                    return (
                        <div className="item" key={idx}>
                            <TweetCard
                                title={review.title}
                                content={review.content}
                                date={review.date}
                            />
                        </div>
                    );
            }
        });
        return (
            <div className="summaryRandomPick">
                <div className="row">
                    <div className="col-lg-9 col-md-9">
                        <h1>Review Digest</h1>
                        <div className="customRow">
                            {reviewCards}
                        </div>
                    </div>
                </div>
            </div>
        );
    }
});

module.exports = SummaryReviewPick;
