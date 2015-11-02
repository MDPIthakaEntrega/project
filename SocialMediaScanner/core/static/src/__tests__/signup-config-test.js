/**
 * Created by Emily on 11/1/15.
 */
var assert = require('assert');
var $ = require('jquery');
var sinon = require('sinon');
var React = require('react/addons');
var SignupConfig = require('../signup-config');
var TestUtils = React.addons.TestUtils;

describe("signup-config", function() {
    describe("test-ajax", function() {
        var fakeData = [];


        beforeEach(function () {
            sinon.stub($, "ajax").yieldsTo("success", fakeData);
        });

        afterEach(function () {
            $.ajax.restore();
        });

        it('call the ajax', function() {
            var signup = TestUtils.renderIntoDocument(
                React.createElement(SignupConfig, {})
            );
        })

    });



});