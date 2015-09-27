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

var PageContent = React.createClass({
    render: function() {
        var content;
        switch (this.props.sectionName) {
            case 'review_feeds':
                content = <ReviewFeeds />;
                break;
            case 'charts':
                content = <DashboardPlot />;
                break;
            case 'settings':
                content = <Settings />;
                break;
        }
        return <div id="page-wrapper">{content}</div>;
    }
});

var DashboardApp = React.createClass({
    getInitialState: function () {
        /**
         * searchKeyWord: the keyword that we want to filter from the review
         */
        return {
            sectionName: 'dashboard'
        };
    },


    changeSection: function (sectionName) {
        this.setState({
            sectionName: sectionName
        });
    },

    render: function () {
        return (
            <div>
                <NavBar />
                <SideBar
                    sectionClickHandler={this.changeSection}
                    sectionName={this.state.sectionName}
                />
                <PageContent {...this.state} />
            </div>
        );
    }
});

React.render(
    <DashboardApp />,
    document.getElementById('dashboardApp')
);