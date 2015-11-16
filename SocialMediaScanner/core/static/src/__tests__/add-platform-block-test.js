var jsdom = require('jsdom');
global.document = jsdom.jsdom("<!doctype html><html><body></body></html>");
global.window = document.defaultView;
global.navigator = {userAgent: 'node.js'};
global.fetch = function () {};
global.window.document = { createElement: function () {} };

var assert = require('assert');
var AddPlatformBlock = require('../add-platform-block');
var sinon = require('sinon');
var React = require('react/addons');
var TestUtils = React.addons.TestUtils;

describe('add-platform-block', function () {
    var saveStub;
    var cancelStub;
    var info;

    beforeEach('set up', function () {
        require('react/lib/ExecutionEnvironment').canUseDOM = true;
        saveStub = sinon.stub();
        cancelStub = sinon.stub();
        info = {
            platforms: {'Twitter': '', 'Yelp': ''}
        };
    });

    it('should generate an add platform block', function() {
        var addBlock = TestUtils.renderIntoDocument(
            <AddPlatformBlock
                onSave={saveStub}
                onCancel={cancelStub}
                {...info}
            />
        );

        var selectInputNode = React.findDOMNode(addBlock.refs.selectInput);
        assert.notEqual(selectInputNode, null);
        var textInputNode = React.findDOMNode(addBlock.refs.textInput);
        assert.notEqual(textInputNode, null);
        var saveButtonNode = React.findDOMNode(addBlock.refs.saveButton);
        assert.notEqual(saveButtonNode, null);
        var cancelButtonNode = React.findDOMNode(addBlock.refs.cancelButton);
        assert.notEqual(cancelButtonNode, null);
    });

    it('click save button', function() {
        var addBlock = TestUtils.renderIntoDocument(
            <AddPlatformBlock
                onSave={saveStub}
                onCancel={cancelStub}
                {...info}
            />
        );

        var saveButtonNode = React.findDOMNode(addBlock.refs.saveButton);
        assert.equal(saveButtonNode.textContent, 'Save');
        assert.notEqual(saveButtonNode, null);

        assert.equal(saveStub.called, false);
        TestUtils.Simulate.click(saveButtonNode);
        assert.equal(saveStub.calledOnce, true);
    });

    it('click cancel button', function() {
        var addBlock = TestUtils.renderIntoDocument(
            <AddPlatformBlock
                onSave={saveStub}
                onCancel={cancelStub}
                {...info}
            />
        );

        var cancelButtonNode = React.findDOMNode(addBlock.refs.cancelButton);
        assert.equal(cancelButtonNode.textContent, 'Cancel');
        assert.notEqual(cancelButtonNode, null);

        assert.equal(cancelStub.called, false);
        TestUtils.Simulate.click(cancelButtonNode);
        assert.equal(cancelStub.calledOnce, true);
    });

    it('platform options', function() {
        var addBlock = TestUtils.renderIntoDocument(
            <AddPlatformBlock
                onSave={saveStub}
                onCancel={cancelStub}
                {...info}
            />
        );
        var platformOptions = addBlock.refs.selectInput.props.children;
        assert.equal(platformOptions.length, 2);
        assert.equal(platformOptions[0].props.value, 'Twitter');
        assert.equal(platformOptions[1].props.value, 'Yelp');
    });
});
