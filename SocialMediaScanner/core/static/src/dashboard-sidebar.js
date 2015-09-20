/**
 * Created by renl on 9/16/15.
 */

var React = require('React');

var SideBar = React.createClass({
    sectionClickHandler: function (sectionName) {
        this.props.sectionClickHandler(sectionName);
    },

    render: function () {
        var dotSign = (
            <li className="dot">
                <img src="/static/img/dot.png" alt="dot" />
            </li>
        );
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
                        {dotSign}
                        <li>
                            <a className="active-menu"  href="index.html"><div className="hoverdiv"></div><i className="fa fa-tachometer fa-3x"></i><p className="SidebarIconName">Dashboard</p></a>
                        </li>
                        {dotSign}
                        <li>
                            <a  href="#"><div className="hoverdiv"></div><i className="fa fa-pie-chart fa-3x"></i> <p className="SidebarIconName"> Charts</p></a>
                        </li>
                        {dotSign}
                        <li>
                            <a  href="#"><div className="hoverdiv"></div><i className="fa fa-cog fa-3x"></i> <p className="SidebarIconName">Setting</p></a>
                        </li>
                        {dotSign}
                        <li>
                            <a  href="#"><div className="hoverdiv"></div><i className="fa fa-sign-out fa-3x"></i> <p className="SidebarIconName">Setting</p></a>
                        </li>
                    </ul>
                </div>
            </nav>
        );
    }
});

module.exports = SideBar;