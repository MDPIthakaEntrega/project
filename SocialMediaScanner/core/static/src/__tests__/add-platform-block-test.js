/**
 * Created by Emily on 11/8/15.
 */
var jsdom = require('jsdom');
global.document = jsdom.jsdom("<!doctype html><html><body></body></html>");
global.window = document.defaultView;
global.navigator = {userAgent: 'node.js'};
global.fetch = function () {};
global.window.document = {
    createElement: function () {}
};

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

        var platfomOps = TestUtils.scryRenderedDOMComponentsWithTag(addBlock, 'option');
        assert.equal(platfomOps.length, 2);
        assert.equal(platfomOps[0].getDOMNode().textContent, 'Twitter');
        assert.equal(platfomOps[1].getDOMNode().textContent, 'Yelp');
    });

});

/*
var platfomOps = TestUtils.scryRenderedDOMComponentsWithTag(addBlock, 'option');
        TestUtils.Simulate.click(platfomOps[0].getDOMNode());
        var selectInputNode = React.findDOMNode(addBlock.refs.selectInput);
        assert.notEqual(selectInputNode, null);
        TestUtils.Simulate.change(selectInputNode, {target: {value: 'Twitter'}});
        assert(selectInputNode.textContent, "Twitter");
        var c1 = this.config_chart.refs.c1;
        TestUtils.Simulate.change(c1.getInputDOMNode());*/
