/**
 * Created by Emily on 11/1/15.
 */


var jsdom = require('jsdom');
global.document = jsdom.jsdom("<!doctype html><html><body></body></html>");
global.window = document.defaultView;
global.navigator = {userAgent: 'node.js'};
global.fetch = function () {};
global.window.document = { createElement: function () {} };


var assert = require('assert');
var $ = require('jquery');
var sinon = require('sinon');
var React = require('react/addons');
var SignupConfig = require('../signup-config');
var TestUtils = React.addons.TestUtils;

describe("signup-config", function() {
    describe("test-ajax", function() {
        it('call the ajax', function() {
            var signup = TestUtils.renderIntoDocument(
                React.createElement(SignupConfig, {})
            );
        })
    });
});