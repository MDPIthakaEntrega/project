/**
 * Created by Emily on 11/9/15.
 */
var jsdom = require('jsdom');
global.document = jsdom.jsdom("<!doctype html><html><body></body></html>");
global.window = document.defaultView;
global.navigator = {userAgent: 'node.js'};
global.fetch = function () {};
global.window.document = { createElement: function () {} };

var assert = require('assert');
var TweetCard = require('../dashboard-review-cards').TweetCard;
var YelpReviewCard = require('../dashboard-review-cards').YelpReviewCard;
var CityGridReviewCard = require('../dashboard-review-cards').CityGridReviewCard;
var sinon = require('sinon');
var React = require('react/addons');
var TestUtils = React.addons.TestUtils;

describe('dashboard review card', function () {
    it('create simple twitter card', function () {
        var twitter = TestUtils.renderIntoDocument(
            <TweetCard title="TwitterTestTitle" content="TwitterTestContent" date="TwitterTestDate" />
        );

        var contentNode = React.findDOMNode(twitter.refs.twittercontent);
        assert.notEqual(contentNode, null);
        assert.equal(contentNode.textContent, "TwitterTestContent");

        var dateNode = React.findDOMNode(twitter.refs.twitterdate);
        assert.notEqual(dateNode, null);
        assert.equal(dateNode.textContent, "TwitterTestDate");
    });

    it('create simple yelp card', function () {
        var yelp = TestUtils.renderIntoDocument(
            <YelpReviewCard title="YelpTestTitle" content="YelpTestContent" date="YelpTestDate" />
        );

        var contentNode = React.findDOMNode(yelp.refs.yelpcontent);
        assert.notEqual(contentNode, null);
        assert.equal(contentNode.textContent, "YelpTestContent");

        var dateNode = React.findDOMNode(yelp.refs.yelpdate);
        assert.notEqual(dateNode, null);
        assert.equal(dateNode.textContent, "YelpTestDate");
    });

    it('create simple city grid card', function () {
        var citygrid = TestUtils.renderIntoDocument(
            <CityGridReviewCard title="CityGridTestTitle" content="CityGridTestContent" date="CityGridTestDate"/>
        );

        var titleNode = React.findDOMNode(citygrid.refs.citygridtitle);
        assert.notEqual(titleNode, null);
        assert.equal(titleNode.textContent, "CityGridTestTitle");

        var contentNode = React.findDOMNode(citygrid.refs.citygridcontent);
        assert.notEqual(contentNode, null);
        assert.equal(contentNode.textContent, "CityGridTestContent");

        var dateNode = React.findDOMNode(citygrid.refs.citygriddate);
        assert.notEqual(dateNode, null);
        assert.equal(dateNode.textContent, "CityGridTestDate");
    })
});