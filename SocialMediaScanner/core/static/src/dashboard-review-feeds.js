/**
 * Created by renl on 9/16/15.
 */

var React = require('react');
var $ = require('jquery');
var SearchInput = require('react-bootstrap').Input;
var CityGridReviewCard = require('./dashboard-review-cards').CityGridReviewCard;

function searchKeywordFromData(searchKeyword, data) {
    var words = searchKeyword.split(" ");
    var totalWords = words.length;
    var rankMap = {};
    for (var i = 0; i < data.length; i++) {
        var content = data[i].content;
        var count = 0;
        for (var j = 0; j < totalWords; j++) {
            if (content.search(words[j]) != -1) {
                count++;
            }
        }
        if (count != 0) {
            if (!(count in rankMap)) {
                rankMap[count] = [];
            }
            rankMap[count].push(i);
        }
    }
    var filteredData = [];
    for (var m = totalWords; m > 0; m--) {
        if (m in rankMap) {
            var positionList = rankMap[m];
            for (var n = 0; n < positionList.length; n++) {
                filteredData.push(data[positionList[n]]);
            }
        }
    }
    return filteredData;
}


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
            //url: 'http://localhost:3456/search?company%20name=zingerman%27s&keyword=',
            url: '/static/search.json',
            dataType: 'json',
            success: function (data) {
                this.setState({
                    data: data
                });
                console.log(data);
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
                return (<CityGridReviewCard
                    key={idx}
                    title={review.title}
                    content={review.content}
                />);
            });
        }
        return (
            <div>
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