/**
 * Created by renl on 9/16/15.
 */

var React = require('React');

var SideBar = React.createClass({
    getInitialState: function () {
        return {
            section_to_name: {
                summary: 'Summary',
                review_feeds: 'Review Feeds',
                charts: 'Charts',
                settings: 'Settings',
                logout: 'Log Out'
            },
            section_to_icon: {
                summary: 'fa fa-tachometer fa-3x',
                review_feeds: 'fa fa-pencil-square-o fa-3x',
                charts: 'fa fa-pie-chart fa-3x',
                settings: 'fa fa-cog fa-3x',
                logout: 'fa fa-sign-out fa-3x'
            },
            username: '',
            company: ''
        };
    },

    componentDidMount: function () {
        this.loadUserInfoFromServer();
    },

    logoutHandler: function () {
        //document.getElementById('logoutForm').submit();
        $.ajax({
            type: 'POST',
            url: '/logout/',
            success: function (data) {
                window.location.href = "/";
            }.bind(this),
            error: function (xhr, status, err) {
            }.bind(this)
        });
    },

    loadUserInfoFromServer: function () {
        $.ajax({
            type: 'GET',
            url: '/api/user/',
            dataType: 'json',
            success: function (data) {
                console.log(data);
                this.setState({
                    username: data.username,
                    company: data.company
                });
            }.bind(this),
            error: function (xhr, status, err) {
            }.bind(this)
        });
    },

    sectionClickHandler: function (sectionName) {
        this.props.sectionClickHandler(sectionName);
    },

    render: function () {
        var sections = Object.keys(this.state.section_to_name).map((sectionName) => {
            if (sectionName !== 'logout') {
                return (
                    <li onClick={this.sectionClickHandler.bind(this, sectionName)}>
                        <a className={sectionName === this.props.sectionName ? "active-menu" : ""} href="#">
                            <div className="hoverdiv"></div>
                            <i className={this.state.section_to_icon[sectionName]}></i>
                            <p className="SidebarIconName">{this.state.section_to_name[sectionName]}</p>
                        </a>
                    </li>
                );
            } else {
                return (
                    <li onClick={this.logoutHandler}>
                        <a className={sectionName === this.props.sectionName ? "active-menu" : ""} href="#">
                            <div className="hoverdiv"></div>
                            <i className={this.state.section_to_icon[sectionName]}></i>
                            <p className="SidebarIconName">{this.state.section_to_name[sectionName]}</p>
                        </a>
                    </li>
                );
            }
        });
        return (
            <nav className="navbar-default navbar-side" role="navigation">
                <div className="sidebar-collapse">
                    <ul className="nav" id="main-menu">
                        <li className="circleli">
                            <div className="circle"></div>
                            <div className="circleinfo">
                                <p className="welcome">Welcome</p>
                                <p className="Mr">
                                    <b>{this.state.username}</b>
                                </p>
                                <p>From:
                                    <b>{this.state.company}</b>
                                </p>
                            </div>
                        </li>
                        {sections}
                    </ul>
                </div>
            </nav>
        );
    }
});

module.exports = SideBar;