var jsdom = require('jsdom');
global.document = jsdom.jsdom("<!doctype html><html><body></body></html>");
global.window = document.defaultView;
global.navigator = {userAgent: 'node.js'};
global.fetch = function () {};
global.window.document = { createElement: function () {} };
for (var i in require.cache) delete require.cache[i];

var assert = require('assert');
var SideBar = require('../dashboard-sidebar');
var sinon = require('sinon');
var React = require('react/addons');
var TestUtils = React.addons.TestUtils;

describe('dashboard review feed', function() {
    var sandbox;
    before('set up', function () {
        require('react/lib/ExecutionEnvironment').canUseDOM = true;
        sandbox = sinon.sandbox.create();
        sandbox.stub(SideBar.prototype.__reactAutoBindMap, 'loadUserInfoFromServer');
    });

    after('take down', function () {
        sandbox.restore();
    });

    it('test render', function() {
        TestUtils.renderIntoDocument(<SideBar /> );
    });

    it('check sections exist', function() {
        var sidebar = TestUtils.renderIntoDocument(<SideBar /> );
        var summaryNode = React.findDOMNode(sidebar.refs.summary);
        assert.notEqual(summaryNode, null);
        var reviewFeedNode = React.findDOMNode(sidebar.refs.review_feeds);
        assert.notEqual(reviewFeedNode, null);
        var chartsNode = React.findDOMNode(sidebar.refs.charts);
        assert.notEqual(chartsNode, null);
        var settingsNode = React.findDOMNode(sidebar.refs.settings);
        assert.notEqual(settingsNode, null);
        var logoutNode = React.findDOMNode(sidebar.refs.logout);
        assert.notEqual(logoutNode, null);
    });

    it('check name and company display', function() {
        var sidebar = TestUtils.renderIntoDocument(<SideBar /> );
        sidebar.setState({
            username: 'testUsername',
            company: 'testCompany'
        });

        var usernameNode = React.findDOMNode(sidebar.refs.username);
        assert.equal(usernameNode.textContent, "testUsername");
        var companyNode = React.findDOMNode(sidebar.refs.company);
        assert.equal(companyNode.textContent, "testCompany");
    });

});