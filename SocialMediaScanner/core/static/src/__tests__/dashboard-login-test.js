/**
 * Created by Emily on 11/9/15.
 */
var jsdom = require('jsdom');
global.document = jsdom.jsdom("<!doctype html><html><body></body></html>");
global.window = document.defaultView;
global.navigator = {userAgent: 'node.js'};
global.fetch = function () {};
global.window.document = { createElement: function () {} };

for (var i in require.cache) delete require.cache[i];

//var DashboardLogin = require('../dashboard-login');
var assert = require('assert');
var sinon = require('sinon');
var React = require('react/addons');
var TestUtils = React.addons.TestUtils;

describe('dashboard login', function() {
    beforeEach('set up', function () {
        require('react/lib/ExecutionEnvironment').canUseDOM = true;
        //sinon.stub(DashboardLogin.prototype.__reactAutoBindMap, 'loadDataFromServer');
    });

    afterEach('take down', function () {
        //DashboardLogin.prototype.__reactAutoBindMap.loadDataFromServer.restore();
    });

    it('should render dashboard login', function() {
        //TestUtils.renderIntoDocument(<DashboardLogin />);
    })

});