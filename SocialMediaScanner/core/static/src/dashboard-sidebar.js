/**
 * Created by renl on 9/16/15.
 */

var React = require('React');

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
                            <a href="#" onClick={this.sectionClickHandler.bind(this, 'review_feeds')}>
                                <i className="fa fa-comment-o fa-fw"></i>
                                Review Feeds</a>
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

module.exports = SideBar;