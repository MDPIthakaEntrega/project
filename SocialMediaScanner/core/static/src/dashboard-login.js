/**
 * Created by renl on 9/15/15.
 */

var React = require('react/addons');
var Navbar = require('react-bootstrap').Navbar;
var Nav = require('react-bootstrap').Nav;
var NavItem = require('react-bootstrap').NavItem;
var SideBar = require('./dashboard-sidebar');
var NavBar = require('./dashboard-navbar');
var ReviewFeeds = require('./dashboard-review-feeds');
var DashboardPlot = require('./dashboard-charts');
var Settings = require('./dashboard-settings');
var Summary = require('./dashboard-summary');

var DashboardApp = React.createClass({
    getInitialState: function () {
        /**
         * searchKeyWord: the keyword that we want to filter from the review
         */
        return {
            sectionName: 'summary',
            chart_config: {},
            charts: {},
            reviews: [],
            api_config: {},
            username: '',
            company: ''
        };
    },

    changeSection: function (sectionName) {
        this.setState({
            sectionName: sectionName
        });
    },

    setChartConfig: function (chart_config) {
        this.setState({
            chart_config: chart_config
        });
    },

    loadDataFromServer: function () {
        $.ajax({
            type: 'GET',
            url: '/api/data/pack',
            success: function (data) {
                this.setState({
                    charts: data.charts,
                    chart_config: JSON.parse(data.chart_config),
                    apis: data.apis,
                    api_config: JSON.parse(data.api_config),
                    reviews: JSON.parse(data.reviews).reviews,
                    username: data.username,
                    company: data.company
                });
            }.bind(this),
            error: function (xhr, status, err) {
            }.bind(this)
        });
    },

    componentDidMount: function () {
        this.loadDataFromServer();
    },

    render: function () {
        var content;
        switch (this.state.sectionName) {
            case 'summary':
                content = <Summary {...this.state} ></Summary>;
                break;
            case 'review_feeds':
                content = <ReviewFeeds {...this.state} />;
                break;
            case 'charts':
                content = <DashboardPlot {...this.state} />;
                break;
            case 'settings':
                content =
                    <Settings
                        setChartConfigFunction={this.setChartConfig}
                        {...this.state}
                    />;
                break;
        }
        return (
            <div>
                <NavBar />
                <SideBar
                    sectionClickHandler={this.changeSection}
                    {...this.state}
                />
                <div id="page-wrapper">{content}</div>
            </div>
        );
    }
});

React.render(
    <DashboardApp />,
    document.getElementById('dashboardApp')
);