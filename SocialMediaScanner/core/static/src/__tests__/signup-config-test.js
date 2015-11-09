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
        beforeEach(function () {
            this.callback = sinon.spy(jQuery, "ajax");
        });

        afterEach(function () {
            $.ajax.restore();
        });

    });

});