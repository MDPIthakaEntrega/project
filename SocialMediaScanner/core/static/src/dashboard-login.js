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
    handleChange: function() {
        console.log("search: " + this.refs.searchInput.getInputDOMNode().value);
        this.props.onUserInput(this.refs.searchInput.getInputDOMNode().value);
    },
    render: function() {
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

    render: function () {
        return (
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
        );
    }
});

React.render(
    <DashboardApp />,
    document.getElementById('dashboardApp')
);