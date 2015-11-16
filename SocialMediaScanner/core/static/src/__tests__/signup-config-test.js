/**
 * Created by Emily on 11/1/15.
 */

var jsdom = require('jsdom');
global.document = jsdom.jsdom("<!doctype html><html><body></body></html>");
global.window = document.defaultView;
global.navigator = {userAgent: 'node.js'};
global.fetch = function () {};
global.window.document = { createElement: function () {} };

for (var i in require.cache) delete require.cache[i];

var assert = require('assert');
var SignupConfig = require('../signup-config');
var sinon = require('sinon');
var React = require('react/addons');
var TestUtils = React.addons.TestUtils;

describe("signup-config", function () {
    beforeEach('set up', function () {
        require('react/lib/ExecutionEnvironment').canUseDOM = true;
        sinon.stub(SignupConfig.prototype.__reactAutoBindMap, 'requestPlatformsFromServer');
    });

    afterEach('take down', function () {
        SignupConfig.prototype.__reactAutoBindMap.requestPlatformsFromServer.restore();
    });

    it('should generate empty signup config form', function() {
        var signup = TestUtils.renderIntoDocument(
            <SignupConfig />
        );

        var button = React.findDOMNode(signup.refs.addButton);
        assert.equal(button, null);
    });

    it('test button text', function () {
        var signup = TestUtils.renderIntoDocument(
            <SignupConfig />
        );

        signup.setState({
            platforms: {'Twitter': ''}
        });

        var button = TestUtils.scryRenderedDOMComponentsWithTag(signup, 'Button');
        assert.equal(button.length, 1);

        assert.equal(signup.getDOMNode().textContent, "Add a new platform account (optional)");

    });

    it('test add button click for 1 platform (Twitter)', function () {
        var addPlatformSpy = sinon.spy(SignupConfig.prototype.__reactAutoBindMap, 'addOnePlatform');

        var signup = TestUtils.renderIntoDocument(
            <SignupConfig />
        );

        signup.setState({
            platforms: {'Twitter': ''}
        });

        var btnNode = React.findDOMNode(signup.refs.addButton);
        TestUtils.Simulate.click(btnNode);
        assert(addPlatformSpy.calledOnce, true);

        var addNode = React.findDOMNode(signup.refs.addBlock);
        assert.equal(addNode.textContent, "TwitterSaveCancel");

        addPlatformSpy.restore();
    });
});
