var jsdom = require('jsdom');
global.document = jsdom.jsdom("<!doctype html><html><body></body></html>");
global.window = document.defaultView;
global.navigator = {userAgent: 'node.js'};
global.fetch = function () {};
global.window.document = { createElement: function () {} };
for (var i in require.cache) delete require.cache[i];

var assert = require('assert');
var Summary = require('../dashboard-summary');
var sinon = require('sinon');
var React = require('react/addons');
var TestUtils = React.addons.TestUtils;

describe('dashboard-summary', function() {
    var error = {
        error_message: "Test Error Message"
    };
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

    it('error message', function() {
        var summary = TestUtils.renderIntoDocument( <Summary {...error} />);

        var errorNode = React.findDOMNode(summary.refs.error);
        assert.notEqual(errorNode, null);
        assert.equal(errorNode.textContent, "Test Error Message");
        var noErrorNode = React.findDOMNode(summary.refs.noerror);
        assert.equal(noErrorNode, null);
    });

    it('no error message', function() {
        var summary = TestUtils.renderIntoDocument( <Summary {...settings} />);

        var noErrorNode = React.findDOMNode(summary.refs.noerror);
        assert.notEqual(noErrorNode, null);
    });
});