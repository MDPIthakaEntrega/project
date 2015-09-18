/**
 * Created by renl on 9/15/15.
 */

var React = require('react');
var Navbar = require('react-bootstrap').Navbar;
var Nav = require('react-bootstrap').Nav;
var NavItem = require('react-bootstrap').NavItem;
var SideBar = require('./dashboard-sidebar');
var ReviewFeeds = require('./dashboard-review-feeds');

var PageContent = React.createClass({
    render: function() {
        var content;
        switch (this.props.sectionName) {
            case 'review_feeds':
                content = <ReviewFeeds />;
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
            sectionName: 'review_feeds'
        };
    },

    changeSection: function (sectionName) {
        this.setState({
            sectionName: sectionName
        });
        console.log(sectionName);
    },

    render: function () {
        return (
            <div>
                <Navbar brand="Baobab">
                    <Nav right eventKey={1}>
                        <NavItem eventKey={2} href="#">Settings</NavItem>
                        <NavItem eventKey={3} href="#">Sign Out</NavItem>
                    </Nav>
                </Navbar>
                <SideBar sectionClickHandler={this.changeSection}></SideBar>
                <PageContent {...this.state}></PageContent>
            </div>
        );
    }
});

React.render(
    <DashboardApp />,
    document.getElementById('dashboardApp')
);