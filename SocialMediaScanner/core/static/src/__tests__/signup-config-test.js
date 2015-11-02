var assert = require('assert');
var React = require('react/addons');
var SignupConfig = require('../signup-config');
var jsdom = require('jsdom');
var sinon = require('sinon');
global.document = jsdom.jsdom("<!doctype html><html><body></body></html>");
global.window = document.defaultView;
global.navigator = {userAgent: 'node.js'};
var TestUtils = React.addons.TestUtils;

describe("signup-config", function () {
    describe("test-ajax", function () {
        it('call the ajax', function () {
            sinon.stub(SignupConfig.prototype.__reactAutoBindMap, 'requestPlatformsFromServer');
            var signup = TestUtils.renderIntoDocument(
                <SignupConfig />
            );
            signup.setState({
                platform: {'Twitter': ''}
            });
        })
    });
});