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
var Spinner = require('react-spinkit');
var $ = require('jquery');

var DashboardApp = React.createClass({
    getInitialState: function () {
        return {
            retry: true,
            error_message: '',
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
                    retry: false,
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
                if (xhr.status == 400) {
                    this.setState({
                        retry: false,
                        error_message: xhr.responseText
                    })
                } else {
                    setTimeout(this.loadDataFromServer, 20000);
                }
            }.bind(this)
        });
    },

    componentDidMount: function () {
        this.loadDataFromServer();
    },

    render: function () {
        if (this.state.retry) {
            return (
                <div>
                    <p>Preparing data, please wait...</p>
                    <Spinner spinnerName='wave' />
                </div>
            );
        }
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

module.exports = DashboardApp;

React.render(
    <DashboardApp />,
    document.getElementById('dashboardApp')
);

