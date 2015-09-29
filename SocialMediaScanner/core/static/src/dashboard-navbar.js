/**
 * Created by renl on 9/20/15.
 */

var React = require('React');

var NavBar = React.createClass({
    render: function () {
        return (
            <nav className="navbar navbar-default navbar-cls-top" role="navigation" style={{marginBottom: '0px'}}>
                <div className="navbar-header logo_bg">
                    <button type="button" className="navbar-toggle" data-toggle="collapse" data-target=".sidebar-collapse">
                        <span className="sr-only">Toggle navigation</span>
                        <span className="icon-bar"></span>
                        <span className="icon-bar"></span>
                        <span className="icon-bar"></span>
                    </button>
                    <a className="navbar-brand" href="index.html">
                        <img className="main_logo"src="/static/img/logo.png" alt="logo"/>
                    </a>
                </div>
                <div style={{padding: '10px 20px 5px 15px', float: 'left', fontSize: '15px'}}>
                    <span className="fa-stack fa-lg">
                        <i className="fa fa-circle fa-stack-2x refresh_circle"></i>
                        <i className="fa fa-refresh fa-stack-1x fa-inverse"></i>
                    </span>
                </div>
            </nav>
        );
    }
});

module.exports = NavBar;