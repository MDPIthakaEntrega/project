var jsdom = require('jsdom');
global.document = jsdom.jsdom("<!doctype html><html><body></body></html>");
global.window = document.defaultView;
global.navigator = {userAgent: 'node.js'};
global.fetch = function () {};
global.window.document = {
    createElement: function () {
    }
};

var assert = require('assert');
var React = require('react/addons');
var ConfigChart = require('../config-chart');
var sinon = require('sinon');
var TestUtils = React.addons.TestUtils;

describe('config-chart', function () {
    before(function() {
        this.charts = {
            'c1': 'chart1',
            'c2': 'chart2',
            'c3': 'chart3'
        };
        this.chart_config = {
            'c1': false,
            'c2': false,
            'c3': false
        };
        sinon.stub(ConfigChart.prototype.__reactAutoBindMap, 'updateSettings');
        var config_chart = TestUtils.renderIntoDocument(
            <ConfigChart charts={this.charts} chart_config={this.chart_config} setChartConfigFunction={sinon.stub()} />
        );
        this.config_chart = config_chart;
    });

    it('should be all checkboxes.', function () {
        for (var i in this.charts) {
            assert(this.config_chart.refs[i].props.type, 'checkbox');
        }
    });

    it('should be clickable', function () {
        var c1 = this.config_chart.refs.c1;
        TestUtils.Simulate.change(c1.getInputDOMNode());
    })
});