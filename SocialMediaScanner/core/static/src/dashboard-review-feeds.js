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
            searchKeyword: '',
            data: {}
        };
    },

    handleChangeSearchKeyword: function (searchKeyword) {
        this.setState({
            searchKeyword: searchKeyword
        });
    },

    componentDidMount: function () {
        this.loadFeedsFromServer();
    },

    loadFeedsFromServer: function () {
        $.ajax({
            port: 3456,
            type: 'GET',
            crossDomain: true,
            //url: 'http://35.2.73.31:3456/search?company%20name=zingerman%27s&keyword=',
            url: 'http://localhost:3456/search?company%20name=zingerman%27s&keyword=',
            //url: '/static/search.json',
            dataType: 'json',
            success: function (data) {
                console.log(data);
                this.setState({
                    data: data
                });
            }.bind(this),
            error: function (xhr, status, err) {
            }.bind(this)
        });
    },

    render: function () {
        var reviewsCards = null;
        if (this.state.data.hasOwnProperty('reviews')) {
            var reviews = this.state.data['reviews'];
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