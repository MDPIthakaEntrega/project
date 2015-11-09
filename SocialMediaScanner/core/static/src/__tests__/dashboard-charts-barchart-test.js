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
var RatingBarChart = require('../dashboard-charts-barchart');
var sinon = require('sinon');
var React = require('react/addons');
var TestUtils = React.addons.TestUtils;

describe('dashboard-charts-barchart', function() {
    it('should create barchart', function() {
        //TestUtils.renderIntoDocument( <RatingBarChart /> );
    })
});