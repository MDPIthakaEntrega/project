var jsdom = require('jsdom');
global.document = jsdom.jsdom("<!doctype html><html><body></body></html>");
global.window = document.defaultView;
global.navigator = {userAgent: 'node.js'};
global.fetch = function () {};
global.window.document = { createElement: function () {} };

var assert = require('assert');
var Settings = require('../dashboard-settings');
var sinon = require('sinon');
var React = require('react/addons');
var TestUtils = React.addons.TestUtils;

describe('dashboard review feed', function() {
    it('should pass', function() {
        //TestUtils.renderIntoDocument(<Settings /> );
    })
});