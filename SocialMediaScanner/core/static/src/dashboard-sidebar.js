/**
 * Created by renl on 9/16/15.
 */

var React = require('React');
var $ = require('jquery');

var SideBar = React.createClass({
    sectionClickHandler: function (sectionName) {
        this.props.sectionClickHandler(sectionName);
        $(".active-menu").removeClass("active-menu");
    },

    render: function () {
        return (
            <nav className="navbar-default navbar-side" role="navigation">
                <div className="sidebar-collapse">
                    <ul className="nav" id="main-menu">
                        <li className="circleli">
                            <div className="circle"></div>
                            <div className="circleinfo">
                                <p className="welcome">Welcome</p>
                                <p className="Mr">Mr.Obama</p>
                                <p className="status">Status: Online</p>
                            </div>
                        </li>
                        <li onClick={this.sectionClickHandler.bind(this, 'dashboard')}>
                            <a className="active-menu" href="#">
                                <div className="hoverdiv"></div>
                                <i className="fa fa-tachometer fa-3x"></i>
                                <p className="SidebarIconName">Dashboard</p>
                            </a>
                        </li>
                        <li onClick={this.sectionClickHandler.bind(this, 'review_feeds')}>
                            <a href="#">
                                <div className="hoverdiv"></div>
                                <i className="fa fa-pencil-square-o fa-3x"></i>
                                <p className="SidebarIconName">Review Feeds</p>
                            </a>
                        </li>
                        <li onClick={this.sectionClickHandler.bind(this, 'charts')}>
                            <a href="#">
                                <div className="hoverdiv"></div>
                                <i className="fa fa-pie-chart fa-3x"></i>
                                <p className="SidebarIconName"> Charts</p>
                            </a>
                        </li>
                        <li onClick={this.sectionClickHandler.bind(this, 'settings')}>
                            <a href="#">
                                <div className="hoverdiv"></div>
                                <i className="fa fa-cog fa-3x"></i>
                                <p className="SidebarIconName">Setting</p>
                            </a>
                        </li>
                        <li>
                            <a href="#">
                                <div className="hoverdiv"></div>
                                <i className="fa fa-sign-out fa-3x"></i>
                                <p className="SidebarIconName">Log out</p>
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>
        );
    }
});

module.exports = SideBar;