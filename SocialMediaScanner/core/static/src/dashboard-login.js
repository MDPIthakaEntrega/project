/**
 * Created by renl on 9/15/15.
 */

var React = require('react');
var Navbar = require('react-bootstrap').Navbar;
var Nav = require('react-bootstrap').Nav;
var NavItem = require('react-bootstrap').NavItem;
var SearchInput = require('react-bootstrap').Input;
var SearchBar = require('react-search-bar');

var SearchBar = React.createClass({
    handleChange: function () {
        console.log("search: " + this.refs.searchInput.getInputDOMNode().value);
        this.props.onUserInput(this.refs.searchInput.getInputDOMNode().value);
    },
    render: function () {
        return (
            <div className="navbar-form navbar-right">
                <div className="form-group">
                    <SearchInput
                        type='text'
                        placeholder='Search...'
                        ref="searchInput"
                        groupClassName='group-class'
                        wrapperClassName='wrapper-class'
                        labelClassName='label-class'
                    />
                </div>
            </div>
        );
    }
});

var SideBar = React.createClass({
    sectionClickHandler: function(sectionName) {
        this.props.sectionClickHandler(sectionName);
    },

    render: function () {
        return (
            <div className="navbar-default sidebar" role="navigation">
                <div className="sidebar-nav navbar-collapse">
                    <ul className="nav" id="side-menu">
                        <li>
                            <a href="#" onClick={this.sectionClickHandler.bind(this, 'section1')}>
                                <i className="fa fa-comment-o fa-fw"></i>
                                All Reviews</a>
                        </li>
                        <li>
                            <a href="#" onClick={this.sectionClickHandler.bind(this, 'section2')}>
                                <i className="fa fa-edit fa-fw"></i>
                                New Reviews</a>
                        </li>
                        <li>
                            <a href="#">
                                <i className="fa fa-bar-chart-o fa-fw"></i>
                                Charts
                                <span className="fa arrow"></span>
                            </a>
                            <ul className="nav nav-second-level">
                                <li>
                                    <a href="#">
                                        <i className="fa fa-line-chart"></i>
                                        Overall Charts</a>
                                </li>
                                <li>
                                    <a href="#">
                                        <i className="fa fa-pie-chart"></i>
                                        Sentimental Charts</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        );
    }
});

var DashboardApp = React.createClass({
    getInitialState: function () {
        /**
         * searchKeyWord: the keyword that we want to filter from the review
         */
        return {
            searchKeyWord: ''
        };
    },

    handleChange: function () {
        console.log("handleChange called");
    },

    changeSection: function (sectionName) {
        this.setState({
            searchKeyWord: this.state.searchKeyWord,
            sectionName: sectionName
        });
        console.log(sectionName);
    },

    render: function () {
        return (
            <div>
                <Navbar brand="Baobab">
                    <Nav left eventKey={0}> {/* This is the eventKey referenced */}
                        <SearchBar
                            type="text"
                            value={this.state.value}
                            placeholder="Search..."
                            ref="search-input"
                            groupClassName="group-class"
                            labelClassName="label-class"
                            onChange={this.handleChange} />
                    </Nav>
                    <Nav right eventKey={1}>
                        <NavItem eventKey={2} href="#">Settings</NavItem>
                        <NavItem eventKey={3} href="#">Sign Out</NavItem>
                    </Nav>
                </Navbar>
                <SideBar sectionClickHandler={this.changeSection}></SideBar>
            </div>
        );
    }
});

React.render(
    <DashboardApp />,
    document.getElementById('dashboardApp')
);