/**
 * Created by renl on 9/16/15.
 */

var React = require('react');
var $ = require('jquery');
var SearchInput = require('react-bootstrap').Input;
var CityGridReviewCard = require('./dashboard-review-cards').CityGridReviewCard;
var YelpReviewCard = require('./dashboard-review-cards').YelpReviewCard;
var TweetCard = require('./dashboard-review-cards').TweetCard;
var searchKeywordFromData = require('./search-algorithm');

var SearchBar = React.createClass({
    handleChange: function () {
        this.props.onKeywordChange(this.refs.searchInput.getInputDOMNode().value);
    },

    render: function () {
        return (
            <SearchInput
                type='text'
                placeholder='Search...'
                ref="searchInput"
                groupClassName='group-class'
                wrapperClassName='wrapper-class'
                labelClassName='label-class'
                onChange={this.handleChange}
            />
        );
    }
});

var ReviewFeed = React.createClass({
    getInitialState: function () {
        return {
            searchKeyword: ''
        };
    },

    handleChangeSearchKeyword: function (searchKeyword) {
        this.setState({
            searchKeyword: searchKeyword
        });
    },

    render: function () {
        var reviewsCards = null;
        if (this.props.reviews.length != 0) {
            var reviews = this.props.reviews;
            var filteredReviews = searchKeywordFromData(this.state.searchKeyword, reviews);
            reviewsCards = filteredReviews.map(function (review, idx) {
                switch (review.source) {
                    case 'Citygrid':
                        return (
                            <CityGridReviewCard
                                key={idx}
                                title={review.title}
                                content={review.content}
                                date={review.date}
                            />
                        );
                    case 'ImportMagicYelp':
                        return (
                            <YelpReviewCard
                                key={idx}
                                title={review.title}
                                content={review.content}
                                date={review.date}
                            />
                        );
                    case 'Twitter':
                        return <TweetCard
                                key={idx}
                                title={review.title}
                                content={review.content}
                                date={review.date}
                            />;
                }
            });
        }
        return (
            <div className="col-xs-10">
                <div style={{padding: '20px'}}>
                    <SearchBar onKeywordChange={this.handleChangeSearchKeyword} />
                </div>
                <div>
                    {reviewsCards}
                </div>
            </div>

        );
    }
});

module.exports = ReviewFeed;