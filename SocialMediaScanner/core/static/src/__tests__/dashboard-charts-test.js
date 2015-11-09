/**
 * Created by Emily on 11/9/15.
 */

var jsdom = require('jsdom');
global.document = jsdom.jsdom("<!doctype html><html><body></body></html>");
global.window = document.defaultView;
global.navigator = {userAgent: 'node.js'};
global.fetch = function () {
};
global.window.document = {
    createElement: function () {
    }
};

var assert = require('assert');
var DashboardPlotApp = require('../dashboard-charts');
var TimeChart = require('../dashboard-charts-timechart').TimeChart;
var SentimentPieChart = require('../dashboard-charts-piechart').SentimentPieChart;
var RatingBarChart = require('../dashboard-charts-barchart').RatingBarChart;
var sinon = require('sinon');
var React = require('react/addons');
var TestUtils = React.addons.TestUtils;
var $ = require('jquery');

describe('dashboard-charts', function () {
    var settings = {
        chart_config: {
            bar_chart_ratings: "bar_chart_ratings",
            num_reviews_by_month: "num_reviews_by_month",
            sentiment_pie_chart: "sentiment_pie_chart"
        },
        charts: {
            bar_chart_ratings: "bar_chart_ratings",
            num_reviews_by_month: "num_reviews_by_month",
            sentiment_pie_chart: "sentiment_pie_chart"
        },
        reviews: [{
            "cons": null,
            "review_date": "2009-07-29T03:46:00Z",
            "review_title": "Zingerman's Delicatessen is great!",
            "sentiment_type": "positive",
            "review_text": "Zingerman's is notorious for its sandwiches and it lives up to the hype.  They variety and quality of the sandwiches is great.  There's also plenty of seating.",
            "review_rating": 10,
            "public_id": "zingermans-delicatessen-ann-arbor-2",
            "listing_id": 5168887,
            "pros": null,
            "source": "INSIDERPAGES",
            "attribution_url": "http://www.insiderpages.com/",
            "review_author": "Sally L",
            "sentiment_score": "0.683687",
            "attribution_text": "Insider Pages",
            "type": "user_review",
            "review_id": "ip_10302154082",
            "review_author_url": "http://my.citysearch.com/members/public/profile/Sally+L?i=000b000003fdea9edec0224051a4233a5795939e47",
            "unhelpful_count": null,
            "impression_id": "000b000003fdea9edec0224051a4233a5795939e47",
            "attribution_logo": "http://www.insiderpages.com/images/ip_logo_88x33.jpg",
            "reference_id": null,
            "business_name": "Zingerman's Delicatessen",
            "helpful_count": null,
            "review_url": "http://www.insiderpages.com/b/13313585094/zingermans-delicatessen-ann-arbor",
            "source_id": "17"
        }]
    };

    before('setup', function () {
        require('react/lib/ExecutionEnvironment').canUseDOM = true;
        sinon.stub(TimeChart.prototype.__reactAutoBindMap, 'setChartData');
        sinon.stub(SentimentPieChart.prototype.__reactAutoBindMap, 'setChartData');
        sinon.stub(RatingBarChart.prototype.__reactAutoBindMap, 'setChartData');
    });

    after('teardown', function () {
        TimeChart.prototype.__reactAutoBindMap.setChartData.restore();
        SentimentPieChart.prototype.__reactAutoBindMap.setChartData.restore();
        RatingBarChart.prototype.__reactAutoBindMap.setChartData.restore();
    });

    it('should generate all charts', function () {
        var dbPlotApp = TestUtils.renderIntoDocument(<DashboardPlotApp {...settings} />);

        var barchartNode = React.findDOMNode(dbPlotApp.refs.barchart);
        assert.equal(barchartNode.textContent, "bar_chart_ratings");
        assert.notEqual(barchartNode, null);
        var timechartNode = React.findDOMNode(dbPlotApp.refs.timechart);
        assert.equal(timechartNode.textContent, "num_reviews_by_month");
        assert.notEqual(timechartNode, null);
        var piechartNode = React.findDOMNode(dbPlotApp.refs.piechart);
        assert.equal(piechartNode.textContent, "sentiment_pie_chart");
        assert.notEqual(piechartNode, null);
    });

    it('should generate only barchart', function () {
        settings = {
            chart_config: {bar_chart_ratings: "bar_chart_ratings"},
            charts: {
                bar_chart_ratings: "bar_chart_ratings",
                num_reviews_by_month: "num_reviews_by_month",
                sentiment_pie_chart: "sentiment_pie_chart"
            },
            reviews: [{
                "cons": null,
                "review_date": "2009-07-29T03:46:00Z",
                "review_title": "Zingerman's Delicatessen is great!",
                "sentiment_type": "positive",
                "review_text": "Zingerman's is notorious for its sandwiches and it lives up to the hype.  They variety and quality of the sandwiches is great.  There's also plenty of seating.",
                "review_rating": 10,
                "public_id": "zingermans-delicatessen-ann-arbor-2",
                "listing_id": 5168887,
                "pros": null,
                "source": "INSIDERPAGES",
                "attribution_url": "http://www.insiderpages.com/",
                "review_author": "Sally L",
                "sentiment_score": "0.683687",
                "attribution_text": "Insider Pages",
                "type": "user_review",
                "review_id": "ip_10302154082",
                "review_author_url": "http://my.citysearch.com/members/public/profile/Sally+L?i=000b000003fdea9edec0224051a4233a5795939e47",
                "unhelpful_count": null,
                "impression_id": "000b000003fdea9edec0224051a4233a5795939e47",
                "attribution_logo": "http://www.insiderpages.com/images/ip_logo_88x33.jpg",
                "reference_id": null,
                "business_name": "Zingerman's Delicatessen",
                "helpful_count": null,
                "review_url": "http://www.insiderpages.com/b/13313585094/zingermans-delicatessen-ann-arbor",
                "source_id": "17"
            }]
        };

        var dbPlotApp = TestUtils.renderIntoDocument(<DashboardPlotApp {...settings} />);

        var barchartNode = React.findDOMNode(dbPlotApp.refs.barchart);
        assert.equal(barchartNode.textContent, "bar_chart_ratings");
        assert.notEqual(barchartNode, null);
        var timechartNode = React.findDOMNode(dbPlotApp.refs.timechart);
        assert.equal(timechartNode, null);
        var piechartNode = React.findDOMNode(dbPlotApp.refs.piechart);
        assert.equal(piechartNode, null);
    });

    it('should generate only timechart', function () {
        settings = {
            chart_config: {
                num_reviews_by_month: "num_reviews_by_month"
            },
            charts: {
                bar_chart_ratings: "bar_chart_ratings",
                num_reviews_by_month: "num_reviews_by_month",
                sentiment_pie_chart: "sentiment_pie_chart"
            },
            reviews: [{
                "cons": null,
                "review_date": "2009-07-29T03:46:00Z",
                "review_title": "Zingerman's Delicatessen is great!",
                "sentiment_type": "positive",
                "review_text": "Zingerman's is notorious for its sandwiches and it lives up to the hype.  They variety and quality of the sandwiches is great.  There's also plenty of seating.",
                "review_rating": 10,
                "public_id": "zingermans-delicatessen-ann-arbor-2",
                "listing_id": 5168887,
                "pros": null,
                "source": "INSIDERPAGES",
                "attribution_url": "http://www.insiderpages.com/",
                "review_author": "Sally L",
                "sentiment_score": "0.683687",
                "attribution_text": "Insider Pages",
                "type": "user_review",
                "review_id": "ip_10302154082",
                "review_author_url": "http://my.citysearch.com/members/public/profile/Sally+L?i=000b000003fdea9edec0224051a4233a5795939e47",
                "unhelpful_count": null,
                "impression_id": "000b000003fdea9edec0224051a4233a5795939e47",
                "attribution_logo": "http://www.insiderpages.com/images/ip_logo_88x33.jpg",
                "reference_id": null,
                "business_name": "Zingerman's Delicatessen",
                "helpful_count": null,
                "review_url": "http://www.insiderpages.com/b/13313585094/zingermans-delicatessen-ann-arbor",
                "source_id": "17"
            }]
        };

        var dbPlotApp = TestUtils.renderIntoDocument(<DashboardPlotApp {...settings} />);

        var barchartNode = React.findDOMNode(dbPlotApp.refs.barchart);
        assert.equal(barchartNode, null);
        var timechartNode = React.findDOMNode(dbPlotApp.refs.timechart);
        assert.notEqual(timechartNode, null);
        var piechartNode = React.findDOMNode(dbPlotApp.refs.piechart);
        assert.equal(piechartNode, null);
    });

    it('should generate only piechart', function () {
        settings = {
            chart_config: {
                sentiment_pie_chart: "sentiment_pie_chart"
            },
            charts: {
                bar_chart_ratings: "bar_chart_ratings",
                num_reviews_by_month: "num_reviews_by_month",
                sentiment_pie_chart: "sentiment_pie_chart"
            },
            reviews: [{
                "cons": null,
                "review_date": "2009-07-29T03:46:00Z",
                "review_title": "Zingerman's Delicatessen is great!",
                "sentiment_type": "positive",
                "review_text": "Zingerman's is notorious for its sandwiches and it lives up to the hype.  They variety and quality of the sandwiches is great.  There's also plenty of seating.",
                "review_rating": 10,
                "public_id": "zingermans-delicatessen-ann-arbor-2",
                "listing_id": 5168887,
                "pros": null,
                "source": "INSIDERPAGES",
                "attribution_url": "http://www.insiderpages.com/",
                "review_author": "Sally L",
                "sentiment_score": "0.683687",
                "attribution_text": "Insider Pages",
                "type": "user_review",
                "review_id": "ip_10302154082",
                "review_author_url": "http://my.citysearch.com/members/public/profile/Sally+L?i=000b000003fdea9edec0224051a4233a5795939e47",
                "unhelpful_count": null,
                "impression_id": "000b000003fdea9edec0224051a4233a5795939e47",
                "attribution_logo": "http://www.insiderpages.com/images/ip_logo_88x33.jpg",
                "reference_id": null,
                "business_name": "Zingerman's Delicatessen",
                "helpful_count": null,
                "review_url": "http://www.insiderpages.com/b/13313585094/zingermans-delicatessen-ann-arbor",
                "source_id": "17"
            }]
        };

        var dbPlotApp = TestUtils.renderIntoDocument(<DashboardPlotApp {...settings} />);

        var barchartNode = React.findDOMNode(dbPlotApp.refs.barchart);
        assert.equal(barchartNode, null);
        var timechartNode = React.findDOMNode(dbPlotApp.refs.timechart);
        assert.equal(timechartNode, null);
        var piechartNode = React.findDOMNode(dbPlotApp.refs.piechart);
        assert.notEqual(piechartNode, null);
    });

    it('should not generate barchart', function () {
        settings = {
            chart_config: {
                num_reviews_by_month: "num_reviews_by_month",
                sentiment_pie_chart: "sentiment_pie_chart"
            },
            charts: {
                bar_chart_ratings: "bar_chart_ratings",
                num_reviews_by_month: "num_reviews_by_month",
                sentiment_pie_chart: "sentiment_pie_chart"
            },
            reviews: [{
                "cons": null,
                "review_date": "2009-07-29T03:46:00Z",
                "review_title": "Zingerman's Delicatessen is great!",
                "sentiment_type": "positive",
                "review_text": "Zingerman's is notorious for its sandwiches and it lives up to the hype.  They variety and quality of the sandwiches is great.  There's also plenty of seating.",
                "review_rating": 10,
                "public_id": "zingermans-delicatessen-ann-arbor-2",
                "listing_id": 5168887,
                "pros": null,
                "source": "INSIDERPAGES",
                "attribution_url": "http://www.insiderpages.com/",
                "review_author": "Sally L",
                "sentiment_score": "0.683687",
                "attribution_text": "Insider Pages",
                "type": "user_review",
                "review_id": "ip_10302154082",
                "review_author_url": "http://my.citysearch.com/members/public/profile/Sally+L?i=000b000003fdea9edec0224051a4233a5795939e47",
                "unhelpful_count": null,
                "impression_id": "000b000003fdea9edec0224051a4233a5795939e47",
                "attribution_logo": "http://www.insiderpages.com/images/ip_logo_88x33.jpg",
                "reference_id": null,
                "business_name": "Zingerman's Delicatessen",
                "helpful_count": null,
                "review_url": "http://www.insiderpages.com/b/13313585094/zingermans-delicatessen-ann-arbor",
                "source_id": "17"
            }]
        };

        var dbPlotApp = TestUtils.renderIntoDocument(<DashboardPlotApp {...settings} />);

        var barchartNode = React.findDOMNode(dbPlotApp.refs.barchart);
        assert.equal(barchartNode, null);
        var timechartNode = React.findDOMNode(dbPlotApp.refs.timechart);
        assert.notEqual(timechartNode, null);
        var piechartNode = React.findDOMNode(dbPlotApp.refs.piechart);
        assert.notEqual(piechartNode, null);
    });

    it('should not generate timechart', function () {
        settings = {
            chart_config: {
                bar_chart_ratings: "bar_chart_ratings",
                sentiment_pie_chart: "sentiment_pie_chart"
            },
            charts: {
                bar_chart_ratings: "bar_chart_ratings",
                num_reviews_by_month: "num_reviews_by_month",
                sentiment_pie_chart: "sentiment_pie_chart"
            },
            reviews: [{
                "cons": null,
                "review_date": "2009-07-29T03:46:00Z",
                "review_title": "Zingerman's Delicatessen is great!",
                "sentiment_type": "positive",
                "review_text": "Zingerman's is notorious for its sandwiches and it lives up to the hype.  They variety and quality of the sandwiches is great.  There's also plenty of seating.",
                "review_rating": 10,
                "public_id": "zingermans-delicatessen-ann-arbor-2",
                "listing_id": 5168887,
                "pros": null,
                "source": "INSIDERPAGES",
                "attribution_url": "http://www.insiderpages.com/",
                "review_author": "Sally L",
                "sentiment_score": "0.683687",
                "attribution_text": "Insider Pages",
                "type": "user_review",
                "review_id": "ip_10302154082",
                "review_author_url": "http://my.citysearch.com/members/public/profile/Sally+L?i=000b000003fdea9edec0224051a4233a5795939e47",
                "unhelpful_count": null,
                "impression_id": "000b000003fdea9edec0224051a4233a5795939e47",
                "attribution_logo": "http://www.insiderpages.com/images/ip_logo_88x33.jpg",
                "reference_id": null,
                "business_name": "Zingerman's Delicatessen",
                "helpful_count": null,
                "review_url": "http://www.insiderpages.com/b/13313585094/zingermans-delicatessen-ann-arbor",
                "source_id": "17"
            }]
        };

        var dbPlotApp = TestUtils.renderIntoDocument(<DashboardPlotApp {...settings} />);

        var barchartNode = React.findDOMNode(dbPlotApp.refs.barchart);
        assert.notEqual(barchartNode, null);
        var timechartNode = React.findDOMNode(dbPlotApp.refs.timechart);
        assert.equal(timechartNode, null);
        var piechartNode = React.findDOMNode(dbPlotApp.refs.piechart);
        assert.notEqual(piechartNode, null);
    });

    it('should not generate piechart', function () {
        settings = {
            chart_config: {
                bar_chart_ratings: "bar_chart_ratings",
                num_reviews_by_month: "num_reviews_by_month"
            },
            charts: {
                bar_chart_ratings: "bar_chart_ratings",
                num_reviews_by_month: "num_reviews_by_month",
                sentiment_pie_chart: "sentiment_pie_chart"
            },
            reviews: [{
                "cons": null,
                "review_date": "2009-07-29T03:46:00Z",
                "review_title": "Zingerman's Delicatessen is great!",
                "sentiment_type": "positive",
                "review_text": "Zingerman's is notorious for its sandwiches and it lives up to the hype.  They variety and quality of the sandwiches is great.  There's also plenty of seating.",
                "review_rating": 10,
                "public_id": "zingermans-delicatessen-ann-arbor-2",
                "listing_id": 5168887,
                "pros": null,
                "source": "INSIDERPAGES",
                "attribution_url": "http://www.insiderpages.com/",
                "review_author": "Sally L",
                "sentiment_score": "0.683687",
                "attribution_text": "Insider Pages",
                "type": "user_review",
                "review_id": "ip_10302154082",
                "review_author_url": "http://my.citysearch.com/members/public/profile/Sally+L?i=000b000003fdea9edec0224051a4233a5795939e47",
                "unhelpful_count": null,
                "impression_id": "000b000003fdea9edec0224051a4233a5795939e47",
                "attribution_logo": "http://www.insiderpages.com/images/ip_logo_88x33.jpg",
                "reference_id": null,
                "business_name": "Zingerman's Delicatessen",
                "helpful_count": null,
                "review_url": "http://www.insiderpages.com/b/13313585094/zingermans-delicatessen-ann-arbor",
                "source_id": "17"
            }]
        };

        var dbPlotApp = TestUtils.renderIntoDocument(<DashboardPlotApp {...settings} />);

        var barchartNode = React.findDOMNode(dbPlotApp.refs.barchart);
        assert.notEqual(barchartNode, null);
        var timechartNode = React.findDOMNode(dbPlotApp.refs.timechart);
        assert.notEqual(timechartNode, null);
        var piechartNode = React.findDOMNode(dbPlotApp.refs.piechart);
        assert.equal(piechartNode, null);
    });

    it('should not generate any chart', function () {
        settings = {
            chart_config: {test: "test"},
            charts: {
                bar_chart_ratings: "bar_chart_ratings",
                num_reviews_by_month: "num_reviews_by_month",
                sentiment_pie_chart: "sentiment_pie_chart"
            },
            reviews: [{
                "cons": null,
                "review_date": "2009-07-29T03:46:00Z",
                "review_title": "Zingerman's Delicatessen is great!",
                "sentiment_type": "positive",
                "review_text": "Zingerman's is notorious for its sandwiches and it lives up to the hype.  They variety and quality of the sandwiches is great.  There's also plenty of seating.",
                "review_rating": 10,
                "public_id": "zingermans-delicatessen-ann-arbor-2",
                "listing_id": 5168887,
                "pros": null,
                "source": "INSIDERPAGES",
                "attribution_url": "http://www.insiderpages.com/",
                "review_author": "Sally L",
                "sentiment_score": "0.683687",
                "attribution_text": "Insider Pages",
                "type": "user_review",
                "review_id": "ip_10302154082",
                "review_author_url": "http://my.citysearch.com/members/public/profile/Sally+L?i=000b000003fdea9edec0224051a4233a5795939e47",
                "unhelpful_count": null,
                "impression_id": "000b000003fdea9edec0224051a4233a5795939e47",
                "attribution_logo": "http://www.insiderpages.com/images/ip_logo_88x33.jpg",
                "reference_id": null,
                "business_name": "Zingerman's Delicatessen",
                "helpful_count": null,
                "review_url": "http://www.insiderpages.com/b/13313585094/zingermans-delicatessen-ann-arbor",
                "source_id": "17"
            }]
        };

        var dbPlotApp = TestUtils.renderIntoDocument(<DashboardPlotApp {...settings} />);

        var barchartNode = React.findDOMNode(dbPlotApp.refs.barchart);
        assert.equal(barchartNode, null);
        var timechartNode = React.findDOMNode(dbPlotApp.refs.timechart);
        assert.equal(timechartNode, null);
        var piechartNode = React.findDOMNode(dbPlotApp.refs.piechart);
        assert.equal(piechartNode, null);
    })
});